package com.example.sharkflow.core.system

import android.content.*
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.sharkflow.domain.model.UpdateInfo
import kotlinx.coroutines.*
import okhttp3.*
import java.io.File
import java.security.MessageDigest

suspend fun fetchUpdateInfo(
    manifestUrl: String,
    client: OkHttpClient = OkHttpClient()
): UpdateInfo? =
    withContext(Dispatchers.IO) {
        val req = Request.Builder().url(manifestUrl).build()
        client.newCall(req).execute().use { resp ->
            if (!resp.isSuccessful) return@withContext null
            val body = resp.body.string()
            com.google.gson.Gson().fromJson(body, UpdateInfo::class.java)
        }
    }

suspend fun downloadApkToCache(
    context: Context,
    apkUrl: String,
    fileName: String,
    client: OkHttpClient = OkHttpClient()
): File? =
    withContext(Dispatchers.IO) {
        val req = Request.Builder().url(apkUrl).build()
        client.newCall(req).execute().use { resp ->
            if (!resp.isSuccessful) return@withContext null
            val dir = File(context.cacheDir, "apk")
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, fileName)
            resp.body.byteStream().use { input ->
                file.outputStream().buffered().use { output ->
                    input.copyTo(output)
                }
            }
            file
        }
    }

fun sha256OfFile(file: File): String {
    val md = MessageDigest.getInstance("SHA-256")
    file.inputStream().use { fis ->
        val buffer = ByteArray(8 * 1024)
        var read: Int
        while (fis.read(buffer).also { read = it } > 0) {
            md.update(buffer, 0, read)
        }
    }
    return md.digest().joinToString("") { "%02x".format(it) }
}


fun promptInstallApk(context: Context, apkFile: File) {
    val uri: Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", apkFile)

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/vnd.android.package-archive")
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    context.startActivity(intent)
}
