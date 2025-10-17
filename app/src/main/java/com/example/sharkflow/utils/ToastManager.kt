package com.example.sharkflow.utils

import android.content.Context
import android.view.Gravity
import es.dmoral.toasty.Toasty

object ToastManager {
    private var currentToast: android.widget.Toast? = null
    private const val POS_TOP_CENTER = Gravity.TOP or Gravity.CENTER_HORIZONTAL
    private const val OFFSET_X = 0
    private const val OFFSET_Y = 200
    private const val DURATION = 3000

    private fun show(toast: android.widget.Toast) {
        currentToast?.cancel()
        currentToast = toast
        toast.setGravity(POS_TOP_CENTER, OFFSET_X, OFFSET_Y)
        toast.show()

        toast.view?.postDelayed({ currentToast = null }, 1000)
    }

    fun success(context: Context, message: String) {
        val toast = Toasty.success(context.applicationContext, message, DURATION, true)
        show(toast)
    }

    fun warning(context: Context, message: String) {
        val toast = Toasty.warning(context.applicationContext, message, DURATION, true)
        show(toast)
    }

    fun error(context: Context, message: String) {
        val toast = Toasty.error(context.applicationContext, message, DURATION, true)
        show(toast)
    }

    fun info(context: Context, message: String) {
        val toast = Toasty.info(context.applicationContext, message, DURATION, true)
        show(toast)
    }

    fun common(context: Context, message: String) {
        val toast = Toasty.normal(context.applicationContext, message)
        show(toast)
    }
}
