package com.homefirstindia.homefirstwebsite.utils

import com.homefirstindia.homefirstwebsite.prop.AppProperty

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.security.Key
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


@Component
class CryptoUtils(
    @Autowired val appProperty: AppProperty,
) {

    private fun log(value : String) = LoggerUtils.log("CryptoUtils.$value")


//    fun getSHA512Hash(value: String): String = DigestUtils.sha512Hex(value)



    fun encodeBase64(value: String) : String = Base64.getEncoder().encodeToString(value.toByteArray())

    fun decodeBase64(value: String) : String = String(Base64.getDecoder().decode(value))

    fun getFileIdentifier() = encodeBase64("" + System.currentTimeMillis() + Math.random())


    private var secretKey: SecretKeySpec? = null
    private var cipher: Cipher? = null

    companion object {
        private const val ALGO = "AES"
        private const val PBKDF2WithHMACSHA256 = "PBKDF2WithHmacSHA256"
    }

    @Throws(Exception::class)
    private fun initCipherAndKey() {

        if (null == cipher || null == secretKey) {

            try {

                val factory = SecretKeyFactory.getInstance(PBKDF2WithHMACSHA256)

                val spec: KeySpec = PBEKeySpec(
                    appProperty.mamasSpaghetti.toCharArray(),
                    appProperty.mamasSalt.toByteArray(),
                    65536,
                    256
                )

                val tmp = factory.generateSecret(spec)
                secretKey = SecretKeySpec(tmp.encoded, ALGO)
                cipher = Cipher.getInstance(ALGO)

            } catch (e: Exception) {
                log("initCipherAndKey - Error while initiating KeyBearer object: ${e.message}")
                e.printStackTrace()
                throw e
            }

        }

    }

    @Throws(Exception::class)
    fun encryptAes(plainText: String): String {

        initCipherAndKey()

        val plainTextByte = plainText.toByteArray()
        cipher!!.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedByte = cipher!!.doFinal(plainTextByte)
        val encoder = Base64.getEncoder()
        return encoder.encodeToString(encryptedByte)

    }

    @Throws(Exception::class)
    fun decryptAes(encryptedText: String?): String {

        initCipherAndKey()

        val decoder = Base64.getMimeDecoder()
        val encryptedTextByte = decoder.decode(encryptedText)
        cipher!!.init(Cipher.DECRYPT_MODE, secretKey)
        val decryptedByte = cipher!!.doFinal(encryptedTextByte)
        return String(decryptedByte)

    }

    fun validateSourcePasscode(encryptedPasscode: String): Boolean {

        if (encryptedPasscode.isInvalid()) {
            return false
        }

        return try {
            val decryptedPasscode = decryptAes(encryptedPasscode)

            decryptedPasscode == SOURCE_PASSCODE_KEY

        } catch (e: Exception) {
            false
        }
    }
}

private const val ALGO = "AES"
private val keyValue = byteArrayOf(
    'T'.code.toByte(),
    'T'.code.toByte(),
    '%'.code.toByte(),
    '*'.code.toByte(),
    '5'.code.toByte(),
    '7'.code.toByte(),
    'R'.code.toByte(),
    'S'.code.toByte(),
    'e'.code.toByte(),
    'y'.code.toByte(),
    'r'.code.toByte(),
    '*'.code.toByte(),
    '('.code.toByte(),
    '{'.code.toByte(),
    '}'.code.toByte(),
    'h'.code.toByte()
)

private fun generateKey(): Key {
    return SecretKeySpec(keyValue, ALGO)
}

fun encryptAnyKey(str: String): String {
    val key: Key = generateKey()
    val c = Cipher.getInstance(ALGO)
    c.init(Cipher.ENCRYPT_MODE, key)
    val encVal = c.doFinal(str.toByteArray())
    return Base64.getEncoder().encodeToString(encVal)
}

fun decryptAnyKey(encryptedData: String): String? {
    return try {
        val key: Key = generateKey()
        val c = Cipher.getInstance(ALGO)
        c.init(Cipher.DECRYPT_MODE, key)
        val decodedValue: ByteArray = Base64.getDecoder().decode(encryptedData)
        val decValue = c.doFinal(decodedValue)
        String(decValue)
    } catch (e : Exception) {
        LoggerUtils.log("decrypt - error while decrypting : $encryptedData")
        null
    }
}



