package com.homefirstindia.homefirstwebsite.helper

import com.homefirstindia.homefirstwebsite.model.InternApplication
import com.homefirstindia.homefirstwebsite.model.JobApplication
import com.homefirstindia.homefirstwebsite.utils.*
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.io.File

@Component
class BackgroundProcessHelper(
     val mailHelper: MailHelper
) {
    @Async(THREAD_POOL_TASK_EXECUTOR)
    fun sendApplicationToHR(applicantName: JobApplication?, applicantNameIntern: InternApplication?, resumeFilePath: File) {

        val sb = StringBuilder()
        if (applicantNameIntern == null){

            sb.append("Dear HR,")
            sb.append("\n\nA new job application has been received from email - ${applicantName?.email}.")
            sb.append("\n\nApplicants name - ${applicantName?.name}.")
            sb.append("\n\nPosition applied for - ${applicantName?.position}.")
            sb.append("\n\nApplicants Experience - ${applicantName?.experience}.")
            sb.append("\n\nApplicants State - ${applicantName?.state}.")
            sb.append("\n\nApplicants City - ${applicantName?.city}.")



        } else {

            sb.append("Dear HR,")
            sb.append("\n\nA new job application has been received from email - ${applicantNameIntern.email}.")
            sb.append("\n\nApplicants name - ${applicantNameIntern.name}.")
            sb.append("\n\nPosition applied for - ${applicantNameIntern.type}.")
            sb.append("\n\nApplicants State - ${applicantNameIntern.state}.")
            sb.append("\n\nApplicants City - ${applicantNameIntern.city}.")

        }

        sb.append("\n\nPlease find attached the applicant's resume.")
        sb.append("\n\n\nThis is an auto-generated notification. Please do not reply.")
        sb.append("\n- Homefirst HR Team")

        mailHelper.sendMimeMessageWithAttachment(
            if(applicantNameIntern != null){
                "Application Type - ${applicantNameIntern.type} - ${applicantNameIntern.email}"
            } else {
                "New Job Application - ${applicantName?.email} - ${applicantName?.name}"
            },
            sb.toString(),
            resumeFilePath.path,
            arrayOf(KUNDAN_SINGH_EMAIL),
            arrayOf(ANUJ_BHELKAR_EMAIL)
        )

        println("Job application sent to HR successfully!")
        resumeFilePath.delete()
    }
}

