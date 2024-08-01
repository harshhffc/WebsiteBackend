package com.homefirstindia.homefirstwebsite.services.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.homefirstindia.homefirstwebsite.model.IRSubscriber
import com.homefirstindia.homefirstwebsite.repository.IRSubscriberDetails
import com.homefirstindia.homefirstwebsite.utils.*
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class IRSubscriberService(
    @Autowired val irSubscriberRepo: IRSubscriberDetails,
    @Autowired val cryptoUtils: CryptoUtils,
    @Autowired val objectMapper: ObjectMapper,
    @Autowired val oneResponse: OneResponse,
    @Autowired val verifyCaptcha: VerifyCaptcha
) {
    private fun log(value: String) = LoggerUtils.log("v${this.javaClass.simpleName}.$value")
    private fun logMethod(methodName: String) {
        LoggerUtils.logMethodCall("NewsletterSubscribers:  - $methodName")
    }

    @Throws(Exception::class)
    fun  addSubscriber(
        irSubscribers: IRSubscriber
    ) : ResponseEntity<String> {

        irSubscribers.mandatoryFieldsCheck().let {
            if (!it.isSuccess) {
                log("addSubscriber - ${it.message}")
                return oneResponse.invalidData(it.message)
            }
        }

        if (!verifyCaptcha.isValid(irSubscribers.captcha!!)) {
            log("addSubscriber - Failed to verify Captcha")
            return OneResponse().getFailureResponse(
                LocalResponse()
                    .setMessage("Failed to verify Captcha")
                    .setError(Errors.OPERATION_FAILED.value)
                    .setAction(Actions.RETRY.value)
                    .toJson()
            )
        }

        irSubscriberRepo.findByEmail(irSubscribers.email!!).let {
            if ( null == it ){
                irSubscriberRepo.save(irSubscribers)

                val responseJson = JSONObject()
                responseJson.put("status", "success")
                responseJson.put("message", "Subscribed successfully")

                log("addSubscriber - New subscription, email :  ${irSubscribers.email}")

                return oneResponse.getSuccessResponse(responseJson)
            } else {

                log("addSubscriber - Subscriber already present in db with email : ${irSubscribers.email}")

                if (!it.isSubscribed) {
                    log("addSubscriber - Subscriber already present in db, but is unsubscribed. Subscribing again | email : ${irSubscribers.email}")
                    irSubscribers.id = it.id
                    irSubscribers.isSubscribed = true
                    irSubscribers.updateDatetime = DateTimeUtils.getCurrentDateTimeInIST()
                    irSubscriberRepo.save(irSubscribers)
                    val responseJson = JSONObject()
                    responseJson.put("status", "success")
                    responseJson.put("message", "Subscribed successfully ")
                    log("addSubscriber - Subscriber subscribed again successfully | email : ${irSubscribers.email}")
                    return oneResponse.getSuccessResponse(responseJson)
                }

                return oneResponse.getFailureResponse(LocalResponse().setMessage("Already Subscribed").toJson())
            }
        }




    }

    @Throws(Exception::class)
    fun unsubscribeByEmail(requestObject: JSONObject): ResponseEntity<String> {
        val email = requestObject.optString("email", NA)
        val captcha = requestObject.optString("captcha", NA)



        if (!email.isNotNullOrNA() && !captcha.isNotNullOrNA()) {
            val msg = "Invalid email : $email"
            log("unsubscribeByEmail - Invalid email : $email")
            return oneResponse.getFailureResponse(
                LocalResponse()
                    .setMessage(msg)
                    .setError(Errors.INVALID_DATA.value)
                    .setAction(Actions.FIX_RETRY.value)
                    .toJson()
            )
        }

        if (!verifyCaptcha.isValid(captcha!!)) {
            log("unsubscribe - Failed to verify Captcha")
            return OneResponse().getFailureResponse(
                LocalResponse()
                    .setMessage("Failed to verify Captcha")
                    .setError(Errors.OPERATION_FAILED.value)
                    .setAction(Actions.RETRY.value)
                    .toJson()
            )
        }

        val subscriber = irSubscriberRepo.findByEmail(email)

        if (null == subscriber) {
            log("unsubscribeByEmail - No subscriber found for email : $email")
            return OneResponse().getFailureResponse(
                LocalResponse()
                    .setMessage("No subscriber found for email : $email")
                    .setError(Errors.RESOURCE_NOT_FOUND.value)
                    .setAction(Actions.CANCEL.value)
                    .toJson()
            )
        }

        if ( !subscriber.isSubscribed ){
            log("unsubscribeByEmail - Already Unsubscribed : $email")
            return OneResponse().getFailureResponse(LocalResponse().setMessage("Already Unsubscribed").toJson())
        }

        subscriber.isSubscribed = false
        subscriber.updateDatetime = DateTimeUtils.getCurrentDateTimeInIST()
        irSubscriberRepo.saveAndFlush(subscriber)

        return OneResponse().getSuccessResponse(LocalResponse().setMessage("Unsubscribed successfully").toJson())
    }

}