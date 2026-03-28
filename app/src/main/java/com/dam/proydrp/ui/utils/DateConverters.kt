package com.dam.proydrp.ui.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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