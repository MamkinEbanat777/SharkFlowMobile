package com.example.sharkflow.utils

import android.content.*
import android.view.*
import android.widget.*
import es.dmoral.toasty.*

object ToastManager {

    private fun showToast(toast: Toast) {
        toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 200)
        toast.show()
    }

    fun success(context: Context, message: String) {
        val toast = Toasty.success(context, message, 3000, true)
        showToast(toast)
    }

    fun error(context: Context, message: String) {
        val toast = Toasty.error(context, message, 3000, true)
        showToast(toast)
    }

    fun info(context: Context, message: String) {
        val toast = Toasty.info(context, message, 3000, true)
        showToast(toast)
    }

    fun warning(context: Context, message: String) {
        val toast = Toasty.warning(context, message, 3000, true)
        showToast(toast)
    }
}
