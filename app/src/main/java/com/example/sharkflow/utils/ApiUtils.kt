package com.example.sharkflow.utils

import retrofit2.Response
import java.io.IOException

suspend fun <T> safeApiCall(
    call: suspend () -> Response<T>
): Result<T> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.success(body)
            } else {
                Result.failure(Exception("Пустой ответ от сервера"))
            }
        } else {
            val errBody = try {
                response.errorBody()?.string()
            } catch (e: Exception) {
                AppLog.e("Error reading errorBody", e)
                null
            }
            val message = ErrorMapper.map(response.code(), errBody)
            Result.failure(Exception(message))
        }
    } catch (e: IOException) {
        AppLog.e("Network IO exception", e)
        Result.failure(Exception("Сервер недоступен, проверьте подключение"))
    } catch (e: Exception) {
        AppLog.e("Unexpected exception in safeApiCall", e)
        Result.failure(Exception("Произошла ошибка. Повторите попытку позже"))
    }
}
