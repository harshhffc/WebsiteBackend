package com.homefirstindia.homefirstwebsite.helper
import com.homefirstindia.homefirstwebsite.manager.CredsManager
import com.homefirstindia.homefirstwebsite.manager.EnCredType
import com.homefirstindia.homefirstwebsite.manager.EnPartnerName
import com.homefirstindia.homefirstwebsite.model.Creds
import com.homefirstindia.homefirstwebsite.prop.AppProperty
import com.homefirstindia.homefirstwebsite.utils.LoggerUtils
import com.homefirstindia.homefirstwebsite.utils.decryptAnyKey
import jakarta.mail.MessagingException
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


@Configuration
class MailConfig (
    @Autowired val credentialManager: CredsManager
) {
    companion object {
        private var _gDnrCred: Creds? = null
    }

    private fun gDnrCred(): Creds? {
        if (null == _gDnrCred) {
            _gDnrCred = credentialManager.fetchCredentials(
                EnPartnerName.GOOGLE_DNR,
                EnCredType.PRODUCTION
            )

            _gDnrCred?.apply {
                username = decryptAnyKey(username!!)
                password = decryptAnyKey(password!!)
            }

        }
        return _gDnrCred
    }

    @Bean
    fun getJavaMailSender(): JavaMailSender {

        val gCred = _gDnrCred ?: gDnrCred()

        val mailSender = JavaMailSenderImpl()
        mailSender.host = "smtp.gmail.com"
        mailSender.port = 587
        mailSender.username = gCred?.username
        mailSender.password = gCred?.password

        val props: Properties = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = true
        props["mail.smtp.starttls.enable"] = true
        props["mail.debug"] = true

        return mailSender

    }
}


@Component
class MailHelper (
    @Autowired  val mailSender: JavaMailSender,
    @Autowired val appProperty: AppProperty
) {
    private fun log(value: String) {
        LoggerUtils.log("MailHelper.$value")
    }

    fun sendSimpleMessage(
        to: String,
        subject: String,
        text: String,
        isHtml: Boolean = false
    ): Boolean {
        return try {
            val message = mailSender.createMimeMessage()
            MimeMessageHelper(message, false, "UTF-8").apply {
                setFrom(appProperty.senderEmail)
                setTo(to)
                setSubject(subject)
                setText(text, isHtml)
            }
            mailSender.send(message)
            true
        } catch (e: MailException) {
            log("sendSimpleMessage - Error: ${e.message}")
            false
        }
    }

    fun sendMimeMessage(
        to: Array<String>,
        subject: String,
        body: String,
        isHtml: Boolean = false,
        files: ArrayList<MFile>? = null,
        cc: Array<String>? = null
    ): Boolean {
        return try {
            val message = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true)
            helper.setFrom(appProperty.senderEmail)
            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText(body, isHtml)

            cc?.let {
                if (it.isNotEmpty()) {
                    helper.setCc(it)
                }
            }

            files?.forEach {
                val file = FileSystemResource(File(it.path))
                helper.addAttachment(it.name, file)
            }

            mailSender.send(message)
            true
        } catch (e: MessagingException) {
            log("sendMimeMessage - Error: ${e.message}")
            false
        }
    }

    @Throws(MessagingException::class)
    fun sendMimeMessageWithAttachment(
        subject: String,
        text: String,
        attachmentPath: String,
        recipientEmails: Array<String>,
        cc: Array<String>? = null
    ) {
//        if (!appProperty.isProduction()){
//            return
//        }

        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setFrom(appProperty.senderEmail)
        helper.setTo(recipientEmails)
        helper.setSubject(subject)
        helper.setText(text)

        if (cc?.isNotEmpty() == true)
            helper.setCc(cc)

        val file = File(attachmentPath)
        helper.addAttachment(file.name, file)

        mailSender.send(message)

        log("sendMimeMessageWithAttachment - Mail sent successfully!")
    }
}

class MFile(
    val name: String,
    val path: String
)
