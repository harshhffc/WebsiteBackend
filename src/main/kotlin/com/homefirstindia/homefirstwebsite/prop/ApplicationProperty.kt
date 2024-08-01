package com.homefirstindia.homefirstwebsite.prop

import com.homefirstindia.homefirstwebsite.manager.CredsManager
import com.homefirstindia.homefirstwebsite.utils.THREAD_POOL_TASK_EXECUTOR
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import java.util.*
import java.util.concurrent.Executor


enum class EnvProfile(
    val value : String
    ) {

    DEV("dev"),
    STAGING("staging"),
    UAT("uat"),
    PROD("prod")

}



@Configuration
@EnableAsync
class AppProperty(
    @Autowired val credentialManager: CredsManager
) {


    @Value("\${spring.profiles.active}")
    lateinit var activeProfile : String

    @Value("\${application.path.files}")
    lateinit var  filePath: String

    @Value("\${application.key.mamasSpaghetti}")
    lateinit var  mamasSpaghetti: String


    @Value("\${application.key.mamasSalt}")
    lateinit var  mamasSalt: String


    @Value("\${application.s3Bucket.name}")
    lateinit var s3BucketName: String

    @Value("\${application.s3Bucket.region}")
    lateinit var s3BucketRegion: String

    @Value("\${spring.mail.username}")
    lateinit var senderEmail: String

    fun isProduction() = activeProfile == EnvProfile.PROD.value

//    fun isLocalProduction() = activeProfile == EnvProfile.LOCAL_PROD.value
    fun isUAT() = activeProfile == EnvProfile.UAT.value
    fun isStaging() = activeProfile == EnvProfile.STAGING.value
    fun isDev() = activeProfile == EnvProfile.DEV.value


    @Bean(name = [THREAD_POOL_TASK_EXECUTOR])
    fun threadPoolTaskExecutor(): Executor {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 1
            maxPoolSize = 20
            queueCapacity = 100
        }
    }
}


