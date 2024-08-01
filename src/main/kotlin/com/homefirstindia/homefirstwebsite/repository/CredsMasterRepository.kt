package com.homefirstindia.homefirstwebsite.repository

import com.homefirstindia.homefirstwebsite.model.Creds
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository


@Component
class CredsMasterRepository(
    @Autowired val  credsRepository:CredsRepository
)

    @Repository
    interface CredsRepository: JpaRepository<Creds, String> {

        @Query("from Creds where partnerName = :partnerName and credType = :credType and isValid = true")
        fun findByPartnerNameAndCredType(partnerName: String, credType: String): Creds?

    }
