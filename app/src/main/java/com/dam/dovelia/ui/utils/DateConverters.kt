package com.dam.dovelia.ui.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

fun localDateToString(date: LocalDate?): String {
    if (date == null) return ""
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return date.format(formatter)
}

fun milisToLocalDate(milis: Long?): LocalDate? {
    return milis?.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}

fun localDateToMilis(localDate: LocalDate?): Long? {
    return localDate?.atStartOfDay(ZoneId.systemDefault())
        ?.toInstant()
        ?.toEpochMilli()
}

fun dateToHour(isoString: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("UTC")
        val date = parser.parse(isoString) ?: return ""
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        formatter.format(date)
    } catch (e: Exception) {
        ""
    }
}