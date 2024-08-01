package com.homefirstindia.homefirstwebsite.repository

import com.homefirstindia.homefirstwebsite.model.IRSubscriber
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IRSubscriberDetails : JpaRepository<IRSubscriber, String> {

    fun findByEmail(email: String): IRSubscriber?
}