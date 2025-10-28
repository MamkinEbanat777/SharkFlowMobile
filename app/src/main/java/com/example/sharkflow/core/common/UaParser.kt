package com.example.sharkflow.core.common

import ua_parser.Parser

object UaParser {
    private val parser = Parser()

    data class ParsedAgent(
        val os: String,
        val osVersion: String?,
        val browser: String,
        val browserVersion: String?,
        val device: String,
        val deviceBrand: String?,
        val deviceModel: String?
    )

    fun parse(userAgent: String?): ParsedAgent {
        if (userAgent.isNullOrBlank()) {
            return ParsedAgent("Неизвестно", null, "Неизвестно", null, "Неизвестно", null, null)
        }

        val client = parser.parse(userAgent)
        val os = client.os.family?.takeIf { it != "Other" } ?: "Неизвестно"
        val osVersion = listOfNotNull(client.os.major, client.os.minor).joinToString(".")
            .takeIf { it.isNotBlank() }
        val browser = client.userAgent.family?.takeIf { it != "Other" } ?: "Неизвестно"
        val browserVersion = client.userAgent.major?.let { v ->
            listOfNotNull(
                client.userAgent.major,
                client.userAgent.minor,
                client.userAgent.patch
            ).joinToString(".")
        }
        val device = client.device.family?.takeIf { it != "Other" } ?: "Неизвестно"

        val brandModel = device.split(" ", limit = 2).map { it.trim() }
        val deviceBrand = brandModel.getOrNull(0)
        val deviceModel = brandModel.getOrNull(1)

        return ParsedAgent(os, osVersion, browser, browserVersion, device, deviceBrand, deviceModel)
    }
}