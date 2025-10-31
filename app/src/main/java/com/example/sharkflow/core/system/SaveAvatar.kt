package com.example.sharkflow.core.system

import android.content.Context
import kotlinx.coroutines.*
import okhttp3.*
import java.io.*
import java.util.concurrent.TimeUnit

suspend fun downloadAndSaveToCache(
    context: Context,
    url: String,
    publicId: String? = null
): String? {
    return withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build()

            val req = Request.Builder().url(url).get().build()
            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return@withContext null
                val body = resp.body

                val contentType = body.contentType()?.toString() ?: ""
                val ext = when {
                    contentType.contains("png", ignoreCase = true) -> "png"
                    contentType.contains("webp", ignoreCase = true) -> "webp"
                    contentType.contains("gif", ignoreCase = true) -> "gif"
                    else -> "jpg"
                }

                val dir = File(context.filesDir, "user_avatars")
                if (!dir.exists()) dir.mkdirs()

                val name = publicId?.takeIf { it.isNotBlank() } ?: urlHashName(url)
                val finalFile = File(dir, "$name.$ext")

                if (finalFile.exists() && finalFile.length() > 0L) {
                    return@withContext finalFile.absolutePath
                }

                val tmp = File(dir, "$name.tmp")
                if (tmp.exists()) {
                    try {
                        tmp.delete()
                    } catch (ignored: Exception) { /* ignore */
                    }
                }

                try {
                    body.byteStream().use { input ->
                        tmp.outputStream().buffered().use { output ->
                            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                            var bytesRead: Int
                            while (input.read(buffer).also { bytesRead = it } >= 0) {
                                output.write(buffer, 0, bytesRead)
                            }
                            output.flush()
                        }
                    }
                } catch (e: IOException) {
                    try {
                        tmp.delete()
                    } catch (ignored: Exception) {
                    }
                    throw e
                }

                val renamed = tmp.renameTo(finalFile)
                if (!renamed) {
                    try {
                        tmp.inputStream().use { input ->
                            finalFile.outputStream().buffered().use { output ->
                                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                                var bytesRead: Int
                                while (input.read(buffer).also { bytesRead = it } >= 0) {
                                    output.write(buffer, 0, bytesRead)
                                }
                                output.flush()
                            }
                        }
                        tmp.delete()
                    } catch (e: Exception) {
                        try {
                            tmp.delete()
                        } catch (ignored: Exception) {
                        }
                        try {
                            finalFile.delete()
                        } catch (ignored: Exception) {
                        }
                        return@withContext null
                    }
                }

                if (finalFile.exists() && finalFile.length() > 0L) {
                    return@withContext finalFile.absolutePath
                }
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

private fun urlHashName(url: String): String {
    val md = java.security.MessageDigest.getInstance("SHA-256")
    val bytes = md.digest(url.toByteArray(Charsets.UTF_8))
    return bytes.joinToString("") { "%02x".format(it) }
}
