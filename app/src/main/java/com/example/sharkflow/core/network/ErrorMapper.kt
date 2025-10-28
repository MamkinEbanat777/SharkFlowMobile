package com.example.sharkflow.core.network

import com.example.sharkflow.core.system.AppLog
import com.google.gson.Gson

object ErrorMapper {
    private val defaultMessages = mapOf(
        400 to "Ошибка в данных",
        401 to "Неверный email или пароль",
        403 to "Доступ запрещён",
        404 to "Ресурс не найден",
        429 to "Слишком много попыток. Попробуйте позже",
        500 to "Ошибка сервера",
        502 to "Проблема на стороне шлюза",
        503 to "Сервер временно недоступен",
        504 to "Таймаут сервера"
    )

    fun map(code: Int?, errorBody: String?): String {
        if (code == null) return "Неизвестная ошибка"

        val serverMsg = parseServerError(errorBody)
        if (serverMsg != null) return serverMsg

        return defaultMessages[code] ?: "Ошибка (${code})"
    }

    private fun parseServerError(errorBody: String?): String? {
        if (errorBody.isNullOrBlank()) return null

        return try {
            val json = Gson().fromJson(errorBody, Map::class.java)
            when {
                json["error"] != null -> json["error"].toString()
                json["message"] != null -> json["message"].toString()
                json["detail"] != null -> json["detail"].toString()
                else -> null
            }
        } catch (e: Exception) {
            AppLog.e("Failed to parse error body: $errorBody", e)
            null
        }
    }

}
