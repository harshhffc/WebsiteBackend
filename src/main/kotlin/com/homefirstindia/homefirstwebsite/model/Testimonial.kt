package com.homefirstindia.homefirstwebsite.model

import com.homefirstindia.homefirstwebsite.utils.Actions
import com.homefirstindia.homefirstwebsite.utils.DateTimeUtils.getCurrentDateTimeInIST
import com.homefirstindia.homefirstwebsite.utils.Errors
import com.homefirstindia.homefirstwebsite.utils.LocalResponse
import com.homefirstindia.homefirstwebsite.utils.NA
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator


@Entity
@Table(name = "`TestimonialDetail`")
class TestimonialDetail {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    var id: String? = null
    var type: String? = null
    var name: String? = null
    var description: String? = null
    var image: String? = null
    var label: String? = null
    var cssClass: String? = null
    var testimonialCategoryName: String? = null
    var video: String? = null


//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "categoryId")
//    var testimonialCategory: TestimonialCategory? = null

    @Column(columnDefinition = "DATETIME", updatable = false, nullable = false)
    var createDatetime: String = getCurrentDateTimeInIST()

    @Column(columnDefinition = "DATETIME")
    var updateDatetime = getCurrentDateTimeInIST()


    fun mandatoryFieldsCheck() : LocalResponse {

        val localResponse = LocalResponse()
                .setError(Errors.INVALID_DATA.value)
                .setAction(Actions.FIX_RETRY.value)



        when {
//            description.isNullOrEmpty() -> localResponse.message = "Invalid description."
            testimonialCategoryName.isNullOrEmpty() -> localResponse.message = "Invalid testimonial category name."
            image.isNullOrEmpty() -> localResponse.message = "Invalid image."
            label.isNullOrEmpty() -> localResponse.message = "Invalid label "

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