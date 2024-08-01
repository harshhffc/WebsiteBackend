package com.homefirstindia.homefirstwebsite.utils

fun String?.isValidEmail(): Boolean {
    val regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$".toRegex()
    return this.isNotNullOrNA() && this!!.matches(regex)
}

fun String?.isNotNullOrNA(): Boolean {
    return (
            null != this
                    && !this.equals(NA, ignoreCase = true)
                    && !this.equals("Null", ignoreCase = true)
                    && this.isNotEmpty()
            )
}

fun String?.isInvalid() = !this.isNotNullOrNA()

fun String?.isValidMobileNumber(): Boolean {
    val regex = "[0-9]{10}\$".toRegex()
    return this.isNotNullOrNA() && this!!.matches(regex)
}