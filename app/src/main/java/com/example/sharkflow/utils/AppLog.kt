package com.example.sharkflow.utils

import com.example.sharkflow.BuildConfig
import timber.log.Timber

object AppLog {
    private const val LOG_TAG = "SharkflowAppLog"

    init {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            // В релизе можно подключить другие логирующие механизмы (например, Crashlytics)
        }
    }

    private fun getCallerInfo(): String {
        if (!BuildConfig.DEBUG) return ""
        val stack = Throwable().stackTrace
        val skip =
            listOf(AppLog::class.java.name, "timber.log.Timber", "java.lang.Thread", "kotlin.")
        val el = stack.firstOrNull { skip.none { p -> it.className.startsWith(p) } }
            ?: stack.getOrNull(0)
        return el?.let {
            val file = (it.fileName ?: it.className.substringAfterLast('.')).removeSuffix(".kt")
            "$file:${it.lineNumber}"
        } ?: ""
    }

    private fun buildMessage(component: String?, message: String): String {
        val callerInfo = getCallerInfo()
        val compPart = component?.let { "[$it]" } ?: ""
        val callerPart = if (callerInfo.isNotBlank()) "[$callerInfo]" else ""
        return "$compPart$callerPart - $message"
    }

    private fun log(
        level: LogLevel,
        component: String?,
        message: String,
        throwable: Throwable? = null,
        args: Array<out Any?> = emptyArray()
    ) {
        val finalMessage = buildMessage(component, message).trim()
        when (level) {
            LogLevel.DEBUG -> Timber.tag(LOG_TAG).d(finalMessage, *args)
            LogLevel.INFO -> Timber.tag(LOG_TAG).i(finalMessage, *args)
            LogLevel.WARN -> Timber.tag(LOG_TAG).w(finalMessage, *args)
            LogLevel.ERROR -> {
                if (throwable != null) {
                    if (finalMessage.isNotBlank()) {
                        Timber.tag(LOG_TAG).e(throwable, finalMessage.trim())
                    } else {
                        Timber.tag(LOG_TAG).e(throwable)
                    }
                } else {
                    if (finalMessage.isNotBlank()) {
                        Timber.tag(LOG_TAG).e(finalMessage.trim())
                    } else LOG_TAG
                }
            }
        }
    }

    fun d(message: String, vararg args: Any?) = log(LogLevel.DEBUG, null, message, null, args)
    fun i(message: String, vararg args: Any?) = log(LogLevel.INFO, null, message, null, args)
    fun w(message: String, vararg args: Any?) = log(LogLevel.WARN, null, message, null, args)
    fun e(message: String, throwable: Throwable? = null, vararg args: Any?) =
        log(LogLevel.ERROR, null, message, throwable, args)

    fun d(component: String, message: String, vararg args: Any?) =
        log(LogLevel.DEBUG, component, message, null, args)

    fun i(component: String, message: String, vararg args: Any?) =
        log(LogLevel.INFO, component, message, null, args)

    fun w(component: String, message: String, vararg args: Any?) =
        log(LogLevel.WARN, component, message, null, args)

    fun e(component: String, message: String, throwable: Throwable? = null, vararg args: Any?) =
        log(LogLevel.ERROR, component, message, throwable, args)

    private enum class LogLevel { DEBUG, INFO, WARN, ERROR }
}
