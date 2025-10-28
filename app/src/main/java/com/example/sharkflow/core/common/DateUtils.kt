package com.example.sharkflow.core.common

import java.time.*
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    private val uiFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    fun parseToInstant(raw: String?): Instant? {
        if (raw.isNullOrBlank()) return null

        runCatching {
            return Instant.parse(raw)
        }.getOrNull()

        runCatching {
            val ld = LocalDate.parse(raw, DateTimeFormatter.ISO_LOCAL_DATE)
            return ld.atStartOfDay(ZoneId.systemDefault()).toInstant()
        }.getOrNull()

        return null
    }

    fun formatDateTimeReadable(raw: String?): String? {
        val inst = parseToInstant(raw) ?: return "-"
        val zoned = inst.atZone(ZoneId.systemDefault())
        val localDate = zoned.toLocalDate()
        val localTime = zoned.toLocalTime()

        val currentLocale = Locale.getDefault()

        return if (localTime != LocalTime.MIDNIGHT) {
            val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy, HH:mm", currentLocale)
            zoned.format(formatter)
        } else {
            val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", currentLocale)
            localDate.format(formatter)
        }
    }


    fun toServerInstantString(raw: String?): String? {
        val inst = parseToInstant(raw) ?: return null
        return inst.toString()
    }

    fun millisToServerInstantString(millis: Long): String =
        Instant.ofEpochMilli(millis).toString()
}
