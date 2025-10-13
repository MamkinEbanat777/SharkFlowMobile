package com.example.sharkflow.data.network

import com.example.sharkflow.data.local.*
import okhttp3.*

class DeviceIdInterceptor(private val deviceIdProvider: DeviceIdProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val deviceId = deviceIdProvider.getOrCreateDeviceId()
        val request = chain.request().newBuilder()
            .header("x-device-id", deviceId)
            .build()
        return chain.proceed(request)
    }
}
