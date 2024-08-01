package com.homefirstindia.homefirstwebsite.model

import com.homefirstindia.homefirstwebsite.model.common.City
import com.homefirstindia.homefirstwebsite.model.common.State
import com.homefirstindia.homefirstwebsite.utils.*
import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.GenericGenerator
import kotlin.jvm.Transient


@Entity
@Table(name = "`JobList`")
class JobList {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    var id: String? = null

    var position: String? = null //title
    var department: String? = null
    var description: String? = null
    var responsibility: String? = null //What you will do
    var expectations: String? = null //Who you are
    var qualification: String? = null //
    var jobType: String? = null
    var workModel: String? = null
    var sendEmailTo: String? = null

    @Transient
    var city: ArrayList<String>? = null

    @Transient
    var startDatetime: String? = null

    @Transient
    var endDatetime: String? = null

    @Transient
    var isActive: Boolean? = null

    @Column(columnDefinition = "DATETIME", updatable = false, nullable = false)
    var createDatetime: String = DateTimeUtils.getCurrentDateTimeInIST()

    @Column(columnDefinition = "DATETIME")
    var updateDatetime = DateTimeUtils.getCurrentDateTimeInIST()

    fun mandatoryFieldsCheck() : LocalResponse {

        val localResponse = LocalResponse()
            .setError(Errors.INVALID_DATA.value)
            .setAction(Actions.FIX_RETRY.value)



        when {
            position.isNullOrEmpty() -> localResponse.message = "Invalid position."
            department.isNullOrEmpty() -> localResponse.message = "Invalid department."
            description.isNullOrEmpty() -> localResponse.message = "Invalid description."
//            responsibility.isNullOrEmpty() -> localResponse.message = "Invalid responsibility "
            qualification.isNullOrEmpty() -> localResponse.message = "Invalid qualification details "
            jobType.isNullOrEmpty() -> localResponse.message = "Invalid job type"
            workModel.isNullOrEmpty() -> localResponse.message = "Invalid work model "

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

@Entity
@Table(name = "`JobApplication`")
class JobApplication {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    var id: String? = null

    var name: String? = null

    @Column(unique = true)
    var email: String? = null

    var mobile: String? = null
    var fileName: String? = null
    var position: String? = null

    @Transient
    var token: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jobCityId")
    var jobCity: JobCity? = null

    var experience: String? = null

    var city: String? = null
    var state: String? = null
    var address: String? = null

    @Column(columnDefinition = "DATETIME", updatable = false, nullable = false)
    var createDatetime: String = DateTimeUtils.getCurrentDateTimeInIST()

    @Column(columnDefinition = "DATETIME")
    var updateDatetime = DateTimeUtils.getCurrentDateTimeInIST()

    fun mandatoryFieldsCheck() : LocalResponse {

        val localResponse = LocalResponse()
            .setError(Errors.INVALID_DATA.value)
            .setAction(Actions.FIX_RETRY.value)



        when {
            name.isNullOrEmpty() -> localResponse.message = "Invalid name."
            address.isNullOrEmpty() -> localResponse.message = "Invalid address."
            !email.isValidEmail() -> localResponse.message = "Invalid email id."
            !mobile.isValidMobileNumber() -> localResponse.message = "Invalid mobile."


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

@Entity
class JobCity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    var id: String? = null

    @ManyToOne
    @JoinColumn(name = "job_id")
    var job : JobList? = null

    @ManyToOne
    @JoinColumn(name = "city_id")
    var city: City? = null

    @ManyToOne
    @JoinColumn(name = "state_id")
    var state: State? = null

    @ColumnDefault("0")
    var isActive: Boolean? = null

    var startDatetime: String? = null
    var endDatetime: String? = null

    @Column(columnDefinition = "DATETIME", updatable = false, nullable = false)
    var createDatetime: String = DateTimeUtils.getCurrentDateTimeInIST()

    @Column(columnDefinition = "DATETIME")
    var updateDatetime = DateTimeUtils.getCurrentDateTimeInIST()
}

@Entity
@Table(name = "`InternApplication`")
class InternApplication {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    var id: String? = null

    var name: String? = null

    @Column(unique = true)
    var email: String? = null

    var mobile: String? = null
    var fileName: String? = null
    var address: String? = null
    var type: String? = null

    var city: String? = null
    var state: String? = null

    @Transient
    var token: String? = null


    @Column(columnDefinition = "DATETIME", updatable = false, nullable = false)
    var createDatetime: String = DateTimeUtils.getCurrentDateTimeInIST()

    @Column(columnDefinition = "DATETIME")
    var updateDatetime = DateTimeUtils.getCurrentDateTimeInIST()

    fun mandatoryFieldsCheck() : LocalResponse {

        val localResponse = LocalResponse()
            .setError(Errors.INVALID_DATA.value)
            .setAction(Actions.FIX_RETRY.value)



        when {
            name.isNullOrEmpty() -> localResponse.message = "Invalid name."
            address.isNullOrEmpty() -> localResponse.message = "Invalid address."
            !email.isValidEmail() -> localResponse.message = "Invalid email id."
            !mobile.isValidMobileNumber() -> localResponse.message = "Invalid mobile."


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

