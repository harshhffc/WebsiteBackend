package com.homefirstindia.homefirstwebsite.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.homefirstindia.homefirstwebsite.model.IRSubscriber
import com.homefirstindia.homefirstwebsite.security.AuthRequest
import com.homefirstindia.homefirstwebsite.services.v1.CareerService
import com.homefirstindia.homefirstwebsite.services.v1.IRSubscriberService
import com.homefirstindia.homefirstwebsite.utils.LoggerUtils
import com.homefirstindia.homefirstwebsite.utils.OneResponse
import jakarta.servlet.http.HttpServletRequest
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger


@RestController
@RequestMapping("/api")
class IRSubscriberController(
    @Autowired val oneResponse: OneResponse,
    @Autowired val careerService: CareerService,
    @Autowired val objectMapper: ObjectMapper,
    @Autowired val irSubscriberService: IRSubscriberService
) {
    private fun log(value: String) = LoggerUtils.log("v${this.javaClass.simpleName}.$value")
    private fun logMethod(methodName: String) {
        LoggerUtils.logMethodCall("NewsletterSubscribers:  - $methodName")
    }

    @PostMapping(
        "/newsletter/subscribe",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun addSubscriber(
        @RequestBody irSubscribers: IRSubscriber
    ): ResponseEntity<String>? {
        log("addSubscriber")
        return try {
            irSubscriberService.addSubscriber(irSubscribers)
        } catch (e: Exception) {
            log("Error while addSubscriber: ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }
    }

    @PostMapping(
        "/newsletter/unsubscribe",
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun removeSubscriber(@RequestBody body: String?): ResponseEntity<String> {
        log("removeSubscriber")
        return try {
            irSubscriberService.unsubscribeByEmail(JSONObject(body))
        } catch (e: Exception) {
            LoggerUtils.log("Error while removeSubscriber: ${e.message}")
            e.printStackTrace()
            oneResponse.defaultFailureResponse
        }
    }
}
