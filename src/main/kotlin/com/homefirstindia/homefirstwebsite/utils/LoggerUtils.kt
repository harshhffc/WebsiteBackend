package com.homefirstindia.homefirstwebsite.utils


import java.util.logging.Logger

object LoggerUtils {

    private val logger: Logger = Logger.getLogger(LoggerUtils::class.java.simpleName)

    fun log(value: String) {
        logger.info("\n\nWEB - Value --> $value\n\n")
    }

    fun logBody(body: String) {
        logger.info("\n\nWEB - Body --> $body\n\n")
    }

    fun logMethodCall(value: String) {
        logger.info("\nWEB -\n----------------------\n  Method --> $value  \n----------------------\n\n")
    }

    fun printLog(value: String) {
        println("\n\nWEB - Value --> $value\n\n")
    }

}