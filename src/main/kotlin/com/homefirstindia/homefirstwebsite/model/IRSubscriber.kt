package com.homefirstindia.homefirstwebsite.model

import com.homefirstindia.homefirstwebsite.utils.*
import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.GenericGenerator
import kotlin.jvm.Transient

@Entity
@Table(name = "NewsletterSubscriber")
class IRSubscriber {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, updatable = false)
    var id: String? = null

    var firstName: String? = null
    var lastName: String? = null
    var organisation: String? = null
    var email: String? = null
    var phone: String? = null
    var investorFunction: String? = null

    @ColumnDefault("1")
    var isSubscribed = true

    @Transient
    var captcha: String? = null

    @Column(columnDefinition = "DATETIME", updatable = false, nullable = false)
    var createDatetime: String = DateTimeUtils.getCurrentDateTimeInIST()

    @Column(columnDefinition = "DATETIME")
    var updateDatetime: String = DateTimeUtils.getCurrentDateTimeInIST()

    fun mandatoryFieldsCheck() : LocalResponse {

        val localResponse = LocalResponse()
            .setError(Errors.INVALID_DATA.value)
            .setAction(Actions.FIX_RETRY.value)



        when {
            firstName.isNullOrEmpty() -> localResponse.message = "Invalid firstName."
            lastName.isNullOrEmpty() -> localResponse.message = "Invalid lastName."
            email.isNullOrEmpty() -> localResponse.message = "Invalid email."
            captcha.isNullOrEmpty() -> localResponse.message = "Invalid captcha."
            phone.isNullOrEmpty() -> localResponse.message = "Invalid Phone number."
            organisation.isNullOrEmpty() -> localResponse.message = "Invalid organisation."
            investorFunction.isNullOrEmpty() -> localResponse.message = "Invalid investorFunction."

            else -> {
                localResponse.apply {
                    message = NA
                    error = NA
                    action = NA
                    isSuccess = true
                }
            }
        }

        return localResponse
    }
}