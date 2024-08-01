package com.homefirstindia.homefirstwebsite.manager


import com.homefirstindia.homefirstwebsite.model.Creds
import com.homefirstindia.homefirstwebsite.repository.CredsMasterRepository
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

enum class EnCredType(val value: String) {
    PRODUCTION("PRODUCTION"),
    UAT("UAT"),
    PRE_PROD("PRE_PROD");
}

enum class EnPartnerName(val value: String) {
    AMAZON("AWS-RABIT"),
    GOOGLE_DNR("Google_DNR"),
    Google_DNR_ROHIT("Google_DNR_ROHIT");
}

@Component
class CredsManager(
    @Autowired private val credsMasterRepository: CredsMasterRepository,
    @Autowired private val entityManager: EntityManager
) {

    fun fetchCredentials(
        partnerName: EnPartnerName,
        credType: EnCredType
    ): Creds? {


        val cred = credsMasterRepository.credsRepository.findByPartnerNameAndCredType(
            partnerName.value,
            credType.value
        )?.apply {
            entityManager.detach(this)
        }

        return cred

    }

}




