package com.homefirstindia.homefirstwebsite.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.homefirstindia.homefirstwebsite.utils.*
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator

@Entity
class SearchMetaData {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")

    @Column(updatable = false, nullable = false)
    var id: String? = null

    var webPageId: String? = null

    @Column(unique = true)
    var webPageUrl: String? = null
    var description: String? = null
    var title: String? = null
    var parent: String? = null


   // @JsonIgnore
    @ManyToMany(cascade = [CascadeType.PERSIST,CascadeType.MERGE])
//    @ManyToMany
    @JoinTable(
        name = "search_meta_data_meta_data_keyword",
        joinColumns = [JoinColumn(name = "search_meta_data_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "meta_data_keyword_id", referencedColumnName = "id")]
    )
    var metaDataKeywords: Collection<MetaDataKeyword>? = null

    @JsonIgnore
    @Column(columnDefinition = "DATETIME", updatable = false, nullable = false)
    var createDatetime: String = DateTimeUtils.getCurrentDateTimeInIST()

    @JsonIgnore
    @Column(columnDefinition = "DATETIME")
    var updateDatetime = DateTimeUtils.getCurrentDateTimeInIST()



    fun mandatoryFieldsCheck() : LocalResponse {

        val localResponse = LocalResponse()
            .setError(Errors.INVALID_DATA.value)
            .setAction(Actions.FIX_RETRY.value)

        when {
            description.isNullOrEmpty() -> localResponse.message = "Invalid description."
            webPageUrl.isNullOrEmpty() -> localResponse.message = "Invalid web page url."
            title.isNullOrEmpty() -> localResponse.message = "Invalid title."
            parent.isNullOrEmpty() -> localResponse.message = "Invalid parent."
            metaDataKeywords.isNullOrEmpty() -> localResponse.message = "Invalid keyword value."

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
 class MetaDataKeyword {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    var id: String? = null

    var keywordValue: String? = null

    @JsonIgnore
    @ManyToMany(mappedBy = "metaDataKeywords")
    var searchMetaData: Collection<SearchMetaData>? = null

    @Column(columnDefinition = "DATETIME", updatable = false, nullable = false)
    var createDatetime: String = DateTimeUtils.getCurrentDateTimeInIST()

    @Column(columnDefinition = "DATETIME")
    var updateDatetime = DateTimeUtils.getCurrentDateTimeInIST()


}

data class SearchMetaDataDTO(
    val id: String?,
    val webPageId: String?,
    val webPageUrl: String?,
    val description: String?,
    val title: String?,
    val parent: String?,

)
