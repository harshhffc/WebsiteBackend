package com.homefirstindia.homefirstwebsite.security

import com.homefirstindia.homefirstwebsite.utils.AUTHORIZATION
import com.homefirstindia.homefirstwebsite.utils.NA
import com.homefirstindia.homefirstwebsite.utils.SOURCE_PASSCODE
import jakarta.servlet.http.HttpServletRequest

class AuthRequest() {
    var authorization = NA
    var sourcePasscode = NA
    var requestUri = NA
    constructor(request: HttpServletRequest): this() {

        request.run {

        //    authorization = getHeader(AUTHORIZATION)
            getHeader(SOURCE_PASSCODE)?.let {
                sourcePasscode = it
            }

            requestUri = requestURI


        }

    }
}