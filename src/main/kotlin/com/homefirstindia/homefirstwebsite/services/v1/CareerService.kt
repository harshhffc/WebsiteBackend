package com.homefirstindia.homefirstwebsite.services.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.homefirstindia.homefirstwebsite.clients.AmazonClient
import com.homefirstindia.homefirstwebsite.clients.EnS3BucketPath
import com.homefirstindia.homefirstwebsite.helper.BackgroundProcessHelper
import com.homefirstindia.homefirstwebsite.helper.MailHelper
import com.homefirstindia.homefirstwebsite.model.InternApplication
import com.homefirstindia.homefirstwebsite.model.JobApplication
import com.homefirstindia.homefirstwebsite.model.JobCity
import com.homefirstindia.homefirstwebsite.model.JobList
import com.homefirstindia.homefirstwebsite.prop.AppProperty
import com.homefirstindia.homefirstwebsite.repository.CareersMasterRepository
import com.homefirstindia.homefirstwebsite.repository.CommonMasterRepository
import com.homefirstindia.homefirstwebsite.security.AuthRequest
import com.homefirstindia.homefirstwebsite.utils.*
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

@Service
class CareerService(
    @Autowired val oneResponse: OneResponse,
    @Autowired val careersMasterRepository: CareersMasterRepository,
    @Autowired val commonMasterRepository: CommonMasterRepository,
    @Autowired val amazonClient:AmazonClient,
    @Autowired val mailHelper: MailHelper,
    @Autowired val backgroundProcessHelper: BackgroundProcessHelper,
    @Autowired val appProperty: AppProperty,
    @Autowired val cryptoUtils: CryptoUtils,
    @Autowired val objectMapper:ObjectMapper,
    @Autowired val verifyCaptcha: VerifyCaptcha
) {

        private fun log(value: String) = LoggerUtils.log("v1/${this.javaClass.simpleName}.$value")

        @Throws(Exception::class)
        fun addJobList(jobList: JobList) : ResponseEntity<String> {

           jobList.mandatoryFieldsCheck().let {
                        if (!it.isSuccess) {
                                log("addJobList - ${it.message}")
                                return oneResponse.invalidData(it.message)
                        }
           }

           val job = careersMasterRepository.jobListRepository.save(jobList)

            val cities = commonMasterRepository.cityRepository.findByNameIn(job.city)

            val jobCity = ArrayList<JobCity>()

           cities?.forEach {
               jobCity.add(JobCity().apply {
                   this.job = job
                   city = it
                   state = it.state
                   startDatetime = job.startDatetime
                   endDatetime = job.endDatetime
                   isActive = job.isActive
               })
           }


            careersMasterRepository.jobCityRepository.saveAll(jobCity)

            return oneResponse.getSuccessResponse(
                    JSONObject().put(MESSAGE, "Job Detail added successfully.")
            )

        }


        @Throws(Exception::class)
        fun getAllJobList() : ResponseEntity<String> {

                val result = careersMasterRepository.jobCityRepository.findAll()

            return oneResponse.getSuccessResponse(
                        JSONObject().put("JobList",result)
                )

        }

    @Throws(Exception::class)
    fun getJobListWithJobType(jobType:String): ResponseEntity<String> {

        if(jobType.isInvalid()){
            return oneResponse.invalidData("Invalid Job Type")
        }

        val result = careersMasterRepository.jobCityRepository.findByJobType(jobType)

        val responseObject = JSONObject()
        responseObject.put("JobList", result)

        return OneResponse().getSuccessResponse(responseObject)
    }

    @Throws(Exception::class)
    fun applyJobApplication(jobApplication: JobApplication, resumeFile: MultipartFile) : ResponseEntity<String> {

        if (!verifyCaptcha.isValid(jobApplication.token!!)) {
            log("addSubscriber - Failed to verify Captcha")
            return OneResponse().getFailureResponse(
                LocalResponse()
                    .setMessage("Failed to verify Captcha")
                    .setError(Errors.OPERATION_FAILED.value)
                    .setAction(Actions.RETRY.value)
                    .toJson()
            )
        }

        // Validate file extension
        val fileExtension = resumeFile.originalFilename?.substringAfterLast(".")?.lowercase(Locale.getDefault())

        if (fileExtension == null || !FileTypesExtentions.isValidExtension(".$fileExtension")) {
            log("applyJobApplication - Invalid file extension")
            return oneResponse.invalidData("Invalid file extension. Allowed extensions: ${FileTypesExtentions.entries.map { it.ext }}")
        }


        jobApplication.mandatoryFieldsCheck().let {
            if (!it.isSuccess) {
                log("applyJobApplication - ${it.message}")
                return oneResponse.invalidData(it.message)
            }
        }

//       val jobListData = careersMasterRepository.jobListRepository.findById(jobListId!!).let {
//            if (!it.isPresent) {
//                return oneResponse.invalidData("job list id not found")
//            }
//           it.get()
//        }

//        careersMasterRepository.jobApplicationRepository.findByEmail(jobApplication.email!!).let{
//            if (it.isPresent) {
//                return oneResponse.invalidData("email already exists")
//            }
//        }


        // Save resume file locally
        val localFile = saveMultipartFileLocally(resumeFile)

        val status = amazonClient.uploadFile(
            resumeFile.originalFilename!!,localFile, EnS3BucketPath.RESUME)


        if (!status) {
            log("applyJobApplication - failed to upload resume to S3")
            return oneResponse.invalidData("Failed to upload resume")
        }

//        localFile.delete()

//        jobApplication.jobLists = jobListData
        jobApplication.fileName = resumeFile.originalFilename

        careersMasterRepository.jobApplicationRepository.save(jobApplication)

        backgroundProcessHelper.sendApplicationToHR(jobApplication, null, localFile)

        return oneResponse.getSuccessResponse(
            JSONObject().put(MESSAGE, "Job application applied successfully.")
        )

    }

    @Throws(Exception::class)
    fun applyInternJobApplication(internApplication: InternApplication, resumeFile: MultipartFile) : ResponseEntity<String> {

        if (!verifyCaptcha.isValid(internApplication.token!!)) {
            log("applyInternJobApplication - Failed to verify Captcha")
            return OneResponse().getFailureResponse(
                LocalResponse()
                    .setMessage("Failed to verify Captcha")
                    .setError(Errors.OPERATION_FAILED.value)
                    .setAction(Actions.RETRY.value)
                    .toJson()
            )
        }

        // Validate file extension
        val fileExtension = resumeFile.originalFilename?.substringAfterLast(".")?.lowercase(Locale.getDefault())

        if (fileExtension == null || !FileTypesExtentions.isValidExtension(".$fileExtension")) {
            log("applyInternJobApplication - Invalid file extension")
            return oneResponse.invalidData("Invalid file extension. Allowed extensions: ${FileTypesExtentions.values().map { it.ext }}")
        }


        internApplication.mandatoryFieldsCheck().let {
            if (!it.isSuccess) {
                log("applyInternJobApplication - ${it.message}")
                return oneResponse.invalidData(it.message)
            }
        }

        // Save resume file locally
        val localFile = saveMultipartFileLocally(resumeFile)

        val status = amazonClient.uploadFile(
            resumeFile.originalFilename!!,localFile, EnS3BucketPath.RESUME)


        if (!status) {
            log("applyInternJobApplication - failed to upload resume to S3")
            return oneResponse.invalidData("Failed to upload resume")
        }

        internApplication.fileName = resumeFile.originalFilename

        careersMasterRepository.internJobApplicationRepository.save(internApplication)

        backgroundProcessHelper.sendApplicationToHR(null, internApplication, localFile)

        return oneResponse.getSuccessResponse(
            JSONObject().put(MESSAGE, "Intern Job application applied successfully.")
        )

    }

    @Throws(Exception::class)
    fun getAllJobApplication() : ResponseEntity<String> {

        val jobApplicationLists = careersMasterRepository.jobApplicationRepository.findAll()

        return oneResponse.getSuccessResponse(
            JSONObject().put("JobApplication",jobApplicationLists)
        )

    }

//    @Throws(Exception::class)
//    fun getAllJobApplicationWithJobListId(jobListId: String): ResponseEntity<String> {
//
//        val jobList = careersMasterRepository.jobListRepository.findById(jobListId).let {
//            if (!it.isPresent) {
//                return oneResponse.invalidData("job list id not found")
//            }
//            it.get()
//        }
//        val jobApplicationLists = careersMasterRepository.jobApplicationRepository.findByJobLists(jobList)
//
//        return oneResponse.getSuccessResponse(
//            JSONObject().put("JobApplication",jobApplicationLists)
//        )
//
//    }


    private fun saveMultipartFileLocally(file: MultipartFile): File {
        val uploadDir = appProperty.filePath
        val localFile = File(uploadDir, file.originalFilename!!)
        file.transferTo(localFile)
        return localFile
    }
}