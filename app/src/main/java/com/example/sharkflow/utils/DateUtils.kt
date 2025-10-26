package com.example.sharkflow.utils

import java.time.*
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    private val uiFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    /**
     * Попытка распарсить входную строку как Instant, иначе как LocalDate (ISO_LOCAL_DATE).
     * Возвращает Instant (временную точку) или null.
     */
    fun parseToInstant(raw: String?): Instant? {
        if (raw.isNullOrBlank()) return null

        // 1) ISO instant (например 2025-10-29T00:00:00.000Z)
        runCatching {
            return Instant.parse(raw)
        }.getOrNull()

        // 2) ISO local date (yyyy-MM-dd) -> start of day local -> instant
        runCatching {
            val ld = java.time.LocalDate.parse(raw, DateTimeFormatter.ISO_LOCAL_DATE)
            return ld.atStartOfDay(ZoneId.systemDefault()).toInstant()
        }.getOrNull()

        return null
    }

    /**
     * Формат для отображения в UI: dd.MM.yyyy (локальная дата).
     * Если строка не парсится — возвращает null.
     */

    fun formatDateTimeReadable(raw: String?): String? {
        val inst = DateUtils.parseToInstant(raw) ?: return "-"
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


    /**
     * Нормализация для отправки на сервер: принимает либо:
     * - Instant-строку -> возвращает её (если валидна), или
     * - строку yyyy-MM-dd -> конвертирует в Instant (startOfDay локальной зоны) и возвращает toString().
     * Если не парсится -> null.
     */
    fun toServerInstantString(raw: String?): String? {
        val inst = parseToInstant(raw) ?: return null
        return inst.toString()
    }

    /**
     * Утилита: из millis -> server instant string (используем локальную зону startOfDay если нужно).
     */
    fun millisToServerInstantString(millis: Long): String =
        Instant.ofEpochMilli(millis).toString()
}
