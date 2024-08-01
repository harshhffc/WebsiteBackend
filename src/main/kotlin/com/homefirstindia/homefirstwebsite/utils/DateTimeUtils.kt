package com.homefirstindia.homefirstwebsite.utils

import java.text.SimpleDateFormat
import java.util.*


enum class DateTimeFormat(val value: String) {
    yyyy_MM_dd_HH_mm_ss_SSS_GMT_z("yyyy-MM-dd HH:mm:ss.SSS z"),
    yyyy_MM_dd_T_HH_mm_ss_SSSZ("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
    yyyy_MM_dd("yyyy-MM-dd"),
    yyyy_MM("yyyy-MM"),
    MMM_dd_yyyy("MMM dd, yyyy"),
    E("E"),
    hh_mm("hh:mm"),
    HH_mm("HH:mm"),
    MMM("MMM"),
    MM("MM"),
    dd("dd"),
    yyyy("yyyy"),
    EEE_MMM_d("EEE, MMM d"),
    d_MMM("d MMM"),
    MMM_d("MMM d"),
    MMM_d_hh_mm_a("MMM d, hh:mm a"),
    MMM_d_yyyy("MMM d, yyyy"),
    MMM_yyyy("MMM-yyyy"),
    hh_mm_a("hh:mm a"),
    MMM_dd_yyyy_h_mm_a("MMM dd, yyyy h:mm a"),
    yyyy_MM_dd_HH_mm("yyyy-MM-dd HH:mm"),
    h_mm_a("h:mm a"),
    dd_MM_yyyy("dd-MM-yyyy"),
    dd_MM_yyyy_slash("dd/MM/yyyy"),
    dd_M_yyyy_slash("dd/M/yyyy"),
    yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss"),
    dd_MM_yyyy_HH_mm_ss("dd-MM-yyyy HH:mm:ss"),
    dd_MM_yyyy_hh_mm_a("dd-MM-yyyy hh:mm a"),
    d_EEE_yyyy("d MMM, yyyy"),
    d_MMM_yyyy_hh_mm_a("d MMM, yyyy hh:mm a"),
    ddMMyyyy("ddMMyyyy"),
    d_EEE_yyyy_hh_mm_a("d MMM, yyyy hh:mm a")
}

enum class DateTimeZone(val value: String) {
    IST("IST"), GMT("GMT");
}

object DateTimeUtils {
    fun getCurrentDateTimeInIST(): String = getCurrentDateTimeInIST(DateTimeFormat.yyyy_MM_dd_HH_mm_ss)

    fun getCurrentDateTimeInIST(dateTimeFormat: DateTimeFormat): String {
        val dt = Date()
        val sdf = SimpleDateFormat(dateTimeFormat.value)
        sdf.timeZone = TimeZone.getTimeZone(DateTimeZone.IST.value)
        return sdf.format(dt)
    }
}