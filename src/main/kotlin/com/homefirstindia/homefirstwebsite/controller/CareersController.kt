package com.homefirstindia.homefirstwebsite.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.homefirstindia.homefirstwebsite.model.InternApplication
import com.homefirstindia.homefirstwebsite.model.JobApplication
import com.homefirstindia.homefirstwebsite.model.JobList
import com.homefirstindia.homefirstwebsite.security.AuthRequest
import com.homefirstindia.homefirstwebsite.services.v1.CareerService
import com.homefirstindia.homefirstwebsite.utils.*
import jakarta.servlet.http.HttpServletRequest
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api")
class CareersController(
    @Autowired val oneResponse: OneResponse,
    @Autowired val careerService: CareerService,
    @Autowired val objectMapper: ObjectMapper

) {

    private fun logMethod(value: String) = LoggerUtils.logMethodCall("/TestimonialController$value")

    private fun log(value: String) = LoggerUtils.log("v${this.javaClass.simpleName}.$value")


    @PostMapping(
        "/jobList.add",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun addJobList(
        @RequestBody jobList: JobList,
    ): ResponseEntity<String>? {

        logMethod("addJobList")

        return try {
            careerService.addJobList(jobList)

        } catch (e: Exception) {
            log("addJobList - Error : ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }

    }

    @GetMapping(
        "/jobList.getAll",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllJobList(): ResponseEntity<String>? {

        return try {
            careerService.getAllJobList()

        } catch (e: Exception) {
            log("getAllJobList - Error : ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }
    }

    @GetMapping(
        "/jobList.getAll/{jobType}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getJobListWithJobType(@PathVariable(JOB_TYPE) jobType: String): ResponseEntity<String>? {

        return try {
            careerService.getJobListWithJobType(jobType)

        } catch (e: Exception) {
            log("getAllJobList - Error : ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }
    }


    @PostMapping(
        "/jobApplication.apply",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun applyJobApplication(
        @RequestParam("jobApplication") jobApplication: JSONObject,
        @RequestParam("resumeFile") resumeFile: MultipartFile

    ): ResponseEntity<String>? {

        logMethod("applyJobApplication")

        return try {
            val rJobApplication = objectMapper.readValue(
                jobApplication.toString(), JobApplication::class.java)

            careerService.applyJobApplication(rJobApplication,resumeFile)

        } catch (e: Exception) {
            log("applyJobApplication - Error : ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }

    }

    @PostMapping(
        "/internApplication.apply",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun applyInternApplication(
        @RequestParam("internApplication") internApplication: JSONObject,
        @RequestParam("resumeFile") resumeFile: MultipartFile

    ): ResponseEntity<String>? {

        logMethod("applyInternApplication")

        return try {
            val rJobApplication = objectMapper.readValue(
                internApplication.toString(), InternApplication::class.java)

            careerService.applyInternJobApplication(rJobApplication,resumeFile)

        } catch (e: Exception) {
            log("applyInternApplication - Error : ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }

    }

    @GetMapping(
        "/jobApplication.getAll",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getAllJobApplication(): ResponseEntity<String>? {

        return try {
            careerService.getAllJobApplication()

        } catch (e: Exception) {
            log("getAllJobApplication - Error : ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }
    }

//    @GetMapping(
//        "/jobApplication.getAll/{jobListId}",
//        produces = [MediaType.APPLICATION_JSON_VALUE]
//    )
//    fun getAllJobApplicationWithJobListId(@PathVariable(JOB_LIST_ID) jobListId: String): ResponseEntity<String>? {
//
//        return try {
//            careerService.getAllJobApplicationWithJobListId(jobListId)
//
//        } catch (e: Exception) {
//            log("getAllJobApplicationWithJobListId - Error : ${e.message}")
//            e.printStackTrace()
//            oneResponse.defaultFailureResponse
//        }
//    }



}