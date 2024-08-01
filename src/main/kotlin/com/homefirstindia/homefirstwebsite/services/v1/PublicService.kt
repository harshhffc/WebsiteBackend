package com.homefirstindia.homefirstwebsite.services.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.homefirstindia.homefirstwebsite.model.*
import com.homefirstindia.homefirstwebsite.repository.PublicRepository
import com.homefirstindia.homefirstwebsite.security.AuthRequest
import com.homefirstindia.homefirstwebsite.utils.*
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class PublicService (
    @Autowired val oneResponse: OneResponse,
    @Autowired val publicRepository: PublicRepository,
    @Autowired val objectMapper: ObjectMapper,
    @Autowired val cryptoUtils: CryptoUtils,

    ) {

    private fun log(value: String) = LoggerUtils.log("v1/${this.javaClass.simpleName}.$value")

    private fun logMethod(value: String) = LoggerUtils.logMethodCall("/public/v1/$value")

    @Throws(Exception::class)
    fun getTestimonialDetails(): ResponseEntity<String> {

        val result = publicRepository.testimonialDetailRepository.findAll()
        val responseObject = JSONObject()
        responseObject.put("testimonialDetails", result)

        return OneResponse().getSuccessResponse(responseObject)
    }


    @Throws(Exception::class)
    fun createTestimonialDetails(testimonialDetail: TestimonialDetail) : ResponseEntity<String> {

        testimonialDetail.mandatoryFieldsCheck().let {
            if (!it.isSuccess) {
                log("createTestimonialDetails - ${it.message}")
                return oneResponse.invalidData(it.message)
            }
        }

        publicRepository.testimonialDetailRepository.save(testimonialDetail)

        return oneResponse.getSuccessResponse(
            JSONObject().put(MESSAGE, "Testimonial Detail added successfully.")
        )

    }



        @Throws(Exception::class)
    fun getTestimonialDetailsWithCategoryName(categoryName:String): ResponseEntity<String> {

        if(categoryName.isInvalid()){
            return oneResponse.invalidData("Invalid category name")
        }

        var result = publicRepository.testimonialDetailRepository.findByTestimonialCategoryName(categoryName)

         //by default if there will be no data in db from the asked category name then send the data of home-loan
         if(result.isNullOrEmpty()){
            result = publicRepository.testimonialDetailRepository.findByTestimonialCategoryName(HOME_LOAN)
         }

        val responseObject = JSONObject()
        responseObject.put("testimonialDetails", result)

        return OneResponse().getSuccessResponse(responseObject)
    }


    @Throws(Exception::class)
    fun createFaqDetails(faq: Faq) : ResponseEntity<String> {

        faq.mandatoryFieldsCheck().let {
            if (!it.isSuccess) {
                log("createFaqDetails - ${it.message}")
                return oneResponse.invalidData(it.message)
            }
        }

        publicRepository.faqDetailRepository.save(faq)

        return oneResponse.getSuccessResponse(
            JSONObject().put(MESSAGE, "Faq Detail added successfully.")
        )

    }

    @Throws(Exception::class)
    fun getFaqDetails(): ResponseEntity<String> {

        val faqs = publicRepository.faqDetailRepository.findAll()
        val responseObject = JSONObject()
        responseObject.put("Faqs", faqs)

        return OneResponse().getSuccessResponse(responseObject)
    }

    @Throws(Exception::class)
    fun getFaqDetailsWithFaqCategoryName(faqCategoryName:String): ResponseEntity<String> {

        if(faqCategoryName.isInvalid()){
            return oneResponse.invalidData("Invalid faq category name")
        }

        val responseObject = JSONObject()
        var faqs = publicRepository.faqDetailRepository.findByFaqCategoryName(faqCategoryName)

        //by default if there will be no data in db from the asked category name then send the data of home-loan
        if(faqs.isNullOrEmpty()){
            faqs = publicRepository.faqDetailRepository.findByFaqCategoryName(HOME_LOAN)
            responseObject.put("isRelevant", false)
        }

        responseObject.put("faqs", faqs)

        return OneResponse().getSuccessResponse(responseObject)
    }

    @Throws(Exception::class)
    fun searchMetaDataKeyword(keyword:String): ResponseEntity<String> {

        if(keyword.isInvalid()){
            return oneResponse.invalidData("Invalid keyword")
        }

        val searchResults = publicRepository.searchMetaDataRepository.findByKeyword(keyword)

        if (searchResults.isEmpty()) {
            return oneResponse.invalidData("No result found")
        }

        return oneResponse.getSuccessResponse(
            JSONObject().put("searchResult", JSONArray(objectMapper.writeValueAsString(searchResults)))
        )


    }


    @Throws(Exception::class)
    fun createSearchMetaData(searchMetaData: SearchMetaData) : ResponseEntity<String> {

        searchMetaData.mandatoryFieldsCheck().let {
            if (!it.isSuccess) {
                log("createSearchMetaData - ${it.message}")
                return oneResponse.invalidData(it.message)
            }
        }

        publicRepository.searchMetaDataRepository.findByWebPageUrl(searchMetaData.webPageUrl!!).let {
            if(it?.webPageUrl == searchMetaData.webPageUrl){
                return oneResponse.duplicateRecord("same web page url not allowed")
            }
        }


        searchMetaData.metaDataKeywords?.forEach { metaDataKeyword ->
                publicRepository.metaDataKeywordRepository.save(metaDataKeyword)
        }

        publicRepository.searchMetaDataRepository.save(searchMetaData)

        return oneResponse.getSuccessResponse(
            JSONObject().put(MESSAGE, " search meta data added successfully.")
        )
    }

    @Throws(Exception::class)
    fun updateSearchMetaData(id: String, updatedSearchMetaData: SearchMetaData): ResponseEntity<String> {

        val searchMetaDataData = publicRepository.searchMetaDataRepository.findById(id)
        if (!searchMetaDataData.isPresent) {
            return oneResponse.invalidData("SearchMetaData not found")
        }

        val existingSearchMetaData = searchMetaDataData.get()

        // Update existing SearchMetaData fields with the values provided in the updatedSearchMetaData object
        existingSearchMetaData.apply {
            webPageUrl = updatedSearchMetaData.webPageUrl
            description = updatedSearchMetaData.description
            title = updatedSearchMetaData.title
        }

        // Save any new MetaDataKeyword objects

        updatedSearchMetaData.metaDataKeywords?.forEach { metaDataKeyword ->
            val existingMetaDataKeyword = existingSearchMetaData.metaDataKeywords?.find { it.keywordValue == metaDataKeyword.keywordValue }
            if (existingMetaDataKeyword == null) {
                publicRepository.metaDataKeywordRepository.save(metaDataKeyword)
            }
        }

        // Associate new MetaDataKeyword objects with existing SearchMetaData
        (existingSearchMetaData.metaDataKeywords as MutableCollection<MetaDataKeyword>).addAll(updatedSearchMetaData.metaDataKeywords?.filter {
            existingSearchMetaData.metaDataKeywords?.none { existing -> existing.keywordValue == it.keywordValue } ?: true
        } ?: emptyList())


        val diffFilteredList = existingSearchMetaData.metaDataKeywords?.filter { updatedKeyword ->
            updatedSearchMetaData.metaDataKeywords!!.none { it.keywordValue == updatedKeyword.keywordValue }
        }
        (existingSearchMetaData.metaDataKeywords as MutableCollection<MetaDataKeyword>).removeAll(diffFilteredList!!.toSet())
        // Save the updated SearchMetaData
        publicRepository.searchMetaDataRepository.save(existingSearchMetaData)
        println("existingSearchMetaData ${objectMapper.writeValueAsString(existingSearchMetaData)}")



        if(diffFilteredList.size > 1)
            publicRepository.metaDataKeywordRepository.deleteAll(diffFilteredList)


        return oneResponse.getSuccessResponse(
            JSONObject().put(MESSAGE, "Search metadata updated successfully.")
        )
    }

    @Throws(Exception::class)
    fun createMetaDataKeyword(searchMetaDataId: String, metaDataKeyword: MetaDataKeyword): ResponseEntity<String> {

        if (metaDataKeyword.keywordValue.isInvalid()) {
            return oneResponse.invalidData("Invalid keyword")
        }

        val searchMetaDataOptional = publicRepository.searchMetaDataRepository.findById(searchMetaDataId)
        if (!searchMetaDataOptional.isPresent) {
            return oneResponse.invalidData("Web page not found")
        }

        val searchMetaData = searchMetaDataOptional.get()

        // Check if the keyword already exists in the metaDataKeywords collection
        val existingKeyword = searchMetaData.metaDataKeywords?.find { it.keywordValue == metaDataKeyword.keywordValue }
        if (existingKeyword != null) {
            return oneResponse.invalidData("Keyword '${metaDataKeyword.keywordValue}' already exists")
        }


        // Save the metaDataKeyword first
        val savedMetaDataKeyword = publicRepository.metaDataKeywordRepository.save(metaDataKeyword)

        // Save the searchMetaData if it's not yet saved
        if (searchMetaData.id == null) {
            publicRepository.searchMetaDataRepository.save(searchMetaData)
        }

        // Get the existing metaDataKeywords or initialize an empty list
        val existingMetaDataKeywords = searchMetaData.metaDataKeywords?.toMutableList() ?: mutableListOf()

        // Add the saved metaDataKeyword to the list of existing metaDataKeywords
        existingMetaDataKeywords.add(savedMetaDataKeyword)

        // Set the updated list of metaDataKeywords to the searchMetaData
        searchMetaData.metaDataKeywords = existingMetaDataKeywords

        // Update the association on the other side
        savedMetaDataKeyword.searchMetaData = listOf(searchMetaData)

        // Save the updated searchMetaData
        val savedSearchMetaData = publicRepository.searchMetaDataRepository.save(searchMetaData)

        // Return success response
        return oneResponse.getSuccessResponse(
            JSONObject().put(MESSAGE, "Meta data keyword added successfully.")
        )
    }


    @Throws(Exception::class)
    fun getAllSearchMetaData(): ResponseEntity<String> {

        val searchMetaData = publicRepository.searchMetaDataRepository.findAll()

        println("getAllSearchMetaData ${objectMapper.writeValueAsString(searchMetaData)}")


        return oneResponse.getSuccessResponse(
            JSONObject().put("searchMetaData", JSONArray(objectMapper.writeValueAsString(searchMetaData)))
        )

    }



}