package com.homefirstindia.homefirstwebsite.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.homefirstindia.homefirstwebsite.model.Faq
import com.homefirstindia.homefirstwebsite.model.MetaDataKeyword
import com.homefirstindia.homefirstwebsite.model.SearchMetaData
import com.homefirstindia.homefirstwebsite.model.TestimonialDetail
import com.homefirstindia.homefirstwebsite.security.AuthRequest
import com.homefirstindia.homefirstwebsite.services.v1.PublicService
import com.homefirstindia.homefirstwebsite.utils.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/public/v1")
class PublicController (
    @Autowired val oneResponse: OneResponse,
    @Autowired val publicService: PublicService,
    @Autowired val objectMapper: ObjectMapper

) {

    private fun log(value: String) = LoggerUtils.log("v1/${this.javaClass.simpleName}.$value")
    private fun logMethod(value: String) = LoggerUtils.logMethodCall("/public/v1/$value")

    @GetMapping(
        "/test",
        produces = [MediaType.TEXT_HTML_VALUE]
    )
    fun sayHello(): String? {
        return ("<html> " + "<title>" + "Homefirst Website Spring Server" + "</title>" + "<body><h1>"
                + "Welcome to Homefirst Website Public Services V1!!" + "</h1></body>" + "</html> ")
    }

    @PostMapping(
        "/testimonial.add",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createTestimonialDetails(
        @RequestBody testimonialDetail: TestimonialDetail,
    ): ResponseEntity<String>? {

        logMethod("createTestimonialDetails")
//        println(" request ${objectMapper.writeValueAsString(AuthRequest(request))}")
//        println(" sourcePasscode ${AuthRequest(request).sourcePasscode}")

        return try {
            publicService.createTestimonialDetails(testimonialDetail)
        } catch (e: Exception) {
            log("createTestimonialDetails - Error : ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }

    }

    @GetMapping(
        "/testimonial.detail",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getTestimonialDetails(): ResponseEntity<String>? {

        return try {
            publicService.getTestimonialDetails()

        } catch (e: Exception) {
            log("getTestimonialDetails - Error : ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }
    }

    @GetMapping(
        "/testimonial.getAll/{categoryName}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getTestimonialDetailsWithCategoryName(@PathVariable(CATEGORY_NAME) categoryName: String): ResponseEntity<String>? {

        return try {
            publicService.getTestimonialDetailsWithCategoryName(categoryName)

        } catch (e: Exception) {
            log("getTestimonialDetailsWithCategoryName - Error : ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }
    }


    @PostMapping(
        "/faq.add",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createFaqDetails(
        @RequestBody faq: Faq,
    ): ResponseEntity<String>? {

        logMethod("createFaqDetails")

        return try {
            publicService.createFaqDetails(faq)
        } catch (e: Exception) {
            log("createFaqDetails - Error : ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }

    }

    @GetMapping(
        "/faq.detail",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getFaqDetails(): ResponseEntity<String>? {

        return try {
            publicService.getFaqDetails()

        } catch (e: Exception) {
            log("getFaqDetails - Error : ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }
    }

    @GetMapping(
        "/faq.getAll/{faqCategoryName}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getFaqDetailsWithFaqCategoryName(@PathVariable(FAQ_CATEGORY_NAME) faqCategoryName: String): ResponseEntity<String>? {

        return try {
            publicService.getFaqDetailsWithFaqCategoryName(faqCategoryName)

        } catch (e: Exception) {
            log("getFaqDetailsWithFaqCategoryName - Error : ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }
    }



    @GetMapping(
        "/searchMetaData",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun searchMetaDataKeyword(@RequestParam(KEYWORD) keyword: String): ResponseEntity<String>? {

        return try {
            publicService.searchMetaDataKeyword(keyword)

        } catch (e: Exception) {
            log("searchMetaDataKeyword - Error : ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }
    }

    @PostMapping(
        "/searchMetaData.add",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createSearchMetaData(
        @RequestBody searchMetaData: SearchMetaData,
    ): ResponseEntity<String>? {

        logMethod("createSearchMetaData")

        return try {
            publicService.createSearchMetaData(searchMetaData)
        } catch (e: Exception) {
            log("createSearchMetaData - Error : ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }

    }

    @GetMapping(
        "/searchMetaData.getAll",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllSearchMetaData(): ResponseEntity<String>? {

        logMethod("getAllSearchMetaData")

        return try {
            publicService.getAllSearchMetaData()
        } catch (e: Exception) {
            log("getAllSearchMetaData - Error : ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }

    }

    @PostMapping(
        "/searchMetaData.update/{id}",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateSearchMetaData(
        @PathVariable(ID) id: String,
        @RequestBody searchMetaData: SearchMetaData,
    ): ResponseEntity<String>? {

        logMethod("updateSearchMetaData")

        return try {
            publicService.updateSearchMetaData(id,searchMetaData)
        } catch (e: Exception) {
            log("updateSearchMetaData - Error : ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }

    }

    @PostMapping(
        "/metaDataKeyword.add/{searchMetaDataId}",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createMetaDataKeyword(
        @PathVariable("searchMetaDataId") searchMetaDataId: String,
        @RequestBody metaDataKeyword: MetaDataKeyword,
    ): ResponseEntity<String>? {

        logMethod("createMetaDataKeyword")

        return try {
            publicService.createMetaDataKeyword(searchMetaDataId,metaDataKeyword)
        } catch (e: Exception) {
            log("createMetaDataKeyword - Error : ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }

    }

}