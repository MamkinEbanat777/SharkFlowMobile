package com.example.sharkflow.core.validators

import android.content.Context
import android.util.Patterns
import com.example.sharkflow.core.presentation.ToastManager

object RegisterValidator {
    private val loginRegex = Regex("^[a-zA-Z0-9_]{3,30}$")
    private val passwordUpperCase = Regex("[A-Z]")
    private val passwordLowerCase = Regex("[a-z]")
    private val passwordDigit = Regex("[0-9]")
    private val passwordSpecialChar = Regex("[@$!%*?&#]")
    private val noCyrillic = Regex("^[^\\u0400-\\u04FF]*$")

    private val notValidCode = Regex("^\\d{6}\$")

    fun validateLogin(login: String, context: Context): Boolean {
        val trimmed = login.trim()
        return when {
            trimmed.isEmpty() -> {
                ToastManager.warning(context, "Логин не может быть пустым")
                false
            }

            trimmed.length < 3 -> {
                ToastManager.warning(context, "Логин должен быть не меньше 3 символов")
                false
            }

            trimmed.length > 30 -> {
                ToastManager.warning(context, "Логин не должен быть длиннее 30 символов")
                false
            }

            !loginRegex.matches(trimmed) -> {
                ToastManager.warning(
                    context,
                    "Логин может содержать только латинские буквы, цифры и подчёркивания"
                )
                false
            }

            !noCyrillic.matches(trimmed) -> {
                ToastManager.warning(context, "Кириллица в логине запрещена")
                false
            }

            else -> true
        }
    }

    fun validateEmail(email: String, context: Context): Boolean {
        val trimmed = email.trim()
        return when {
            trimmed.isEmpty() -> {
                ToastManager.warning(context, "Email не может быть пустым")
                false
            }

            !Patterns.EMAIL_ADDRESS.matcher(trimmed).matches() -> {
                ToastManager.warning(context, "Неверный формат email")
                false
            }

            !noCyrillic.matches(trimmed) -> {
                ToastManager.warning(context, "Кириллица в email запрещена")
                false
            }

            else -> true
        }
    }

    fun validatePassword(password: String, context: Context): Boolean {
        return when {
            password.isEmpty() -> {
                ToastManager.warning(context, "Пароль не может быть пустым")
                false
            }

            password.length < 8 -> {
                ToastManager.warning(context, "Пароль должен быть не меньше 8 символов")
                false
            }

            password.length > 100 -> {
                ToastManager.warning(context, "Пароль слишком длинный")
                false
            }

            !passwordUpperCase.containsMatchIn(password) -> {
                ToastManager.warning(
                    context,
                    "Пароль должен содержать хотя бы одну заглавную букву"
                )
                false
            }

            !passwordLowerCase.containsMatchIn(password) -> {
                ToastManager.warning(context, "Пароль должен содержать хотя бы одну строчную букву")
                false
            }

            !passwordDigit.containsMatchIn(password) -> {
                ToastManager.warning(context, "Пароль должен содержать хотя бы одну цифру")
                false
            }

            !passwordSpecialChar.containsMatchIn(password) -> {
                ToastManager.warning(
                    context,
                    "Пароль должен содержать хотя бы один специальный символ (@, $, !, %, *, ?, &, #)"
                )
                false
            }

            !noCyrillic.matches(password) -> {
                ToastManager.warning(context, "Кириллица в пароле запрещена")
                false
            }

            else -> true
        }
    }

    fun validatePasswordConfirmation(password: String, confirm: String, context: Context): Boolean {
        return when {
            confirm.isEmpty() -> {
                ToastManager.warning(context, "Подтверждение пароля не может быть пустым")
                false
            }

            password != confirm -> {
                ToastManager.warning(context, "Пароли не совпадают")
                false
            }

            else -> true
        }
    }

    fun validateConfirmationCode(code: String, context: Context): Boolean {
        return when {
            code.isEmpty() -> {
                ToastManager.warning(context, "Пожалуйста, введите код подтверждения")
                false
            }

            code.length < 6 -> {
                ToastManager.warning(context, "Код должен содержать 6 цифр")
                false
            }

            !notValidCode.matches(code) -> {
                ToastManager.warning(context, "Код должен содержать только цифры")
                false
            }

            else -> {
                true
            }
        }
    }
}
