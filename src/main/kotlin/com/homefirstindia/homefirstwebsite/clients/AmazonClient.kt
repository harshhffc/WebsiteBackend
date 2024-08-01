package com.homefirstindia.homefirstwebsite.clients


import com.amazonaws.AmazonServiceException
import com.amazonaws.SdkClientException
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.PutObjectRequest
import com.homefirstindia.homefirstwebsite.manager.CredsManager
import com.homefirstindia.homefirstwebsite.manager.EnCredType
import com.homefirstindia.homefirstwebsite.manager.EnPartnerName
import com.homefirstindia.homefirstwebsite.model.Creds
import com.homefirstindia.homefirstwebsite.prop.AppProperty
import com.homefirstindia.homefirstwebsite.utils.CryptoUtils
import com.homefirstindia.homefirstwebsite.utils.LoggerUtils.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.ByteArrayInputStream
import java.io.File
import java.net.URL
import java.util.*


@Configuration
class AmazonClient(
    @Autowired val appProperty: AppProperty,
    @Autowired val cryptoUtils: CryptoUtils,
    @Autowired private val credsManager: CredsManager,
) {

    private var _amazonCred: Creds? = null

    private val BUCKET_NAME_PROD = "homefirstindia-s3bucket"
    private val BUCKET_NAME_TEST = "hffc-teststaging-s3"

    @Throws(Exception::class)
    private fun amazonCreds(): Creds {
        if (_amazonCred == null) {
            _amazonCred = credsManager.fetchCredentials(
                EnPartnerName.AMAZON, EnCredType.PRODUCTION
            )
            _amazonCred ?: throw Exception("Failed to get amazon credentials.")
        }
        return _amazonCred!!
    }

    @Bean
    fun s3(): AmazonS3 {

        if (amazonCreds().isEncrypted) {
            amazonCreds().username = cryptoUtils.decryptAes(amazonCreds().username)
            amazonCreds().password = cryptoUtils.decryptAes(amazonCreds().password)
        }

        val awsCredentials: AWSCredentials = BasicAWSCredentials(amazonCreds().username,
            amazonCreds().password)
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(appProperty.s3BucketRegion)
                .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
                .build()
    }

    @Throws(Exception::class)
    fun uploadFile(fileName: String, file: File, bucketPath: EnS3BucketPath): Boolean {
        try {
            log("==> File saving in S3 with Name: $fileName")

            println("appProperty.s3BucketName==== ${appProperty.s3BucketName}")
            println("appProperty.==== ${bucketPath.stringValue}/$fileName}")

            val putObjectRequest = PutObjectRequest(appProperty.s3BucketName,
                "${bucketPath.stringValue}/$fileName", file)
            s3().putObject(putObjectRequest)

            log("==> File saved successfully in S3 with Name: $fileName")

            return true
        } catch (e: AmazonServiceException) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace()
        } catch (e: SdkClientException) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace()
        }
        return false
    }


//    fun getPublicURL(fileName: String, bucketPath: EnS3BucketPath, minutes: Int): String {
//
//        var publicUrl: String = NA
//
//        try {
//            val expiration = Date()
//            var expTimeMillis = expiration.time
//            expTimeMillis += (1000 * 60 * minutes).toLong()
//            expiration.setTime(expTimeMillis)
//            val generatePresignedUrlRequest = GeneratePresignedUrlRequest(
//                getBucketName(),
//                bucketPath.stringValue + "/" + fileName
//            ).withMethod(HttpMethod.GET).withExpiration(expiration)
//
//            val url= s3().generatePresignedUrl(generatePresignedUrlRequest)!!
//            publicUrl = url.toString()
//        } catch (e: AmazonServiceException) {
//            // The call was transmitted successfully, but Amazon S3 couldn't process
//            // it, so it returned an error response.
//            e.printStackTrace()
//        } catch (e: SdkClientException) {
//            // Amazon S3 couldn't be contacted for a response, or the client
//            // couldn't parse the response from Amazon S3.
//            e.printStackTrace()
//        }
//        return publicUrl
//    }

//    fun getBucketName(): String {
//      return if (appProperty.isProduction()) BUCKET_NAME_PROD else BUCKET_NAME_TEST
//    }



}

enum class EnS3BucketPath(val stringValue: String) {
    RESUME("HomeFirstWeb/Resume")



}