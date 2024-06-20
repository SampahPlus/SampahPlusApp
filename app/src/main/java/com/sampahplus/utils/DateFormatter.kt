package com.sampahplus.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateFormatter {
    @SuppressLint("SimpleDateFormat")
    private val originalFormatDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    @SuppressLint("SimpleDateFormat")
    private val changeFormatDate = SimpleDateFormat("EEEE, dd MMMM yyyy | HH:mm", Locale.getDefault())

    fun formatDate(date: String): String? {
        return originalFormatDate.parse(date)?.let { changeFormatDate.format(it) }
    }

    fun parseDate(date: String): Date? {
        return originalFormatDate.parse(date)
    }
}
