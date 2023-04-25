package com.example.common.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtil {
    fun longToDateString(time: Long, dateFormat: String): String {
        val dateFormatObj = SimpleDateFormat(dateFormat, Locale.getDefault())
        return dateFormatObj.format(time)
    }
}