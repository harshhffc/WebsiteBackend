package com.homefirstindia.homefirstwebsite.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.homefirstindia.homefirstwebsite.dto.CaptchaResponse
import com.homefirstindia.homefirstwebsite.networking.v1.CommonNetworkingClient
import com.homefirstindia.homefirstwebsite.prop.AppProperty
import netscape.javascript.JSObject
import org.apache.http.client.methods.RequestBuilder
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class VerifyCaptcha(
    @Autowired val objectMapper: ObjectMapper,
    @Autowired val commonNetworkingClient: CommonNetworkingClient,
    @Autowired val appProperty: AppProperty
) {

    private fun log(value: String) = LoggerUtils.log("v${this.javaClass.simpleName}.$value")

    @Throws(Exception::class)
    fun isValid(token: String): Boolean{

        if (!appProperty.isProduction())
            return true

        val url = "$captchaURL?secret=$captchaSecretKey&response=$token"

        val httpResponse = commonNetworkingClient
            .NewRequest()
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .getCall(url)
            .send()

        if ( !httpResponse.isSuccess ) {
            return false
        }

        val captchaResponse = JSONObject(httpResponse.stringEntity)
        val isSuccess = captchaResponse.optBoolean("success", false)
        val score = captchaResponse.optDouble("score", 0.0)
        return isSuccess && score >= 0.1 // make it 0.5 require more security
    }

}