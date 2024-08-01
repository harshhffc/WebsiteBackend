package com.homefirstindia.homefirstwebsite.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.homefirstindia.homefirstwebsite.utils.*
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "`Faq`")
class Faq {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    var id: String? = null

    var tag: String? = null
    var title: String? = null
    var faqNo: String? = null //for search or

    var question: String? = null

    @Column(columnDefinition = "TEXT")
    var answer: String? = null

    var faqCategoryName: String? = null
    var sectionName: String? = null
    var languageCode: String? = null

    @Column(columnDefinition = "DATETIME", updatable = false, nullable = false)
    var createDatetime: String = DateTimeUtils.getCurrentDateTimeInIST()

    @Column(columnDefinition = "DATETIME")
    var updateDatetime = DateTimeUtils.getCurrentDateTimeInIST()


    fun mandatoryFieldsCheck() : LocalResponse {

        val localResponse = LocalResponse()
            .setError(Errors.INVALID_DATA.value)
            .setAction(Actions.FIX_RETRY.value)

        when {
            question.isNullOrEmpty() -> localResponse.message = "Invalid question"
            faqCategoryName.isNullOrEmpty() -> localResponse.message = "Invalid faq category name"
            answer.isNullOrEmpty() -> localResponse.message = "Invalid answer"
            tag.isNullOrEmpty() -> localResponse.message = "Invalid tag"
            title.isNullOrEmpty() -> localResponse.message = "Invalid title"
            faqNo.isNullOrEmpty() -> localResponse.message = "Invalid faq no"
            sectionName.isNullOrEmpty() -> localResponse.message = "Invalid section name"

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
