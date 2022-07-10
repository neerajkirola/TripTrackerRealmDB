package com.emt.tracker.util

import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

object DateFormater {

    const val TIME_STAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    const val DATE_FORMAT = "dd MMM yyyy HH:mm:ss"

    fun getFormattedTimeStamp(timeStamp: Long): String {

        return formatTimeStamp(timeStamp, TIME_STAMP_FORMAT)
    }

    fun getFormattedDate(timeStamp: Long): String {
        return formatTimeStamp(timeStamp, DATE_FORMAT)
    }

    private fun formatTimeStamp(timeStamp: Long, format: String): String {
        val date = Date(timeStamp)
        val format: Format = SimpleDateFormat(format)
        return format.format(date)
    }

    fun getFormattedDate(timeStamp: String): String {
        val inputFormat = SimpleDateFormat(TIME_STAMP_FORMAT)
        val outputFormat = SimpleDateFormat(DATE_FORMAT)
        try {
            val date = inputFormat.parse(timeStamp)
            return outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}