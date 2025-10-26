package com.example.sharkflow.data.network

import com.example.sharkflow.data.local.preference.DeviceIdPreference
import okhttp3.*

class DeviceIdInterceptor(private val deviceIdPreference: DeviceIdPreference) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val deviceId = deviceIdPreference.getOrCreateDeviceId()
        val request = chain.request().newBuilder()
            .header("x-device-id", deviceId)
            .build()
        return chain.proceed(request)
    }
}
