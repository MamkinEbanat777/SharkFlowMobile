package com.example.sharkflow.core.presentation

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import es.dmoral.toasty.Toasty

object ToastManager {
    private var currentToast: Toast? = null
    private const val POS_TOP_CENTER = Gravity.TOP or Gravity.CENTER_HORIZONTAL
    private const val OFFSET_X = 0
    private const val OFFSET_Y = 200

    private fun show(toast: Toast) {
        currentToast?.cancel()
        currentToast = toast
        toast.setGravity(POS_TOP_CENTER, OFFSET_X, OFFSET_Y)
        toast.show()

        toast.view?.postDelayed({ currentToast = null }, 1000)
    }

    fun success(context: Context, message: String, long: Boolean = false) {
        val toast = Toasty.success(
            context.applicationContext,
            message,
            if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT,
            true
        )
        show(toast)
    }


    fun warning(context: Context, message: String, long: Boolean = false) {
        val toast = Toasty.warning(
            context.applicationContext,
            message,
            if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT,
            true
        )
        show(toast)
    }

    fun error(context: Context, message: String, long: Boolean = false) {
        val toast = Toasty.error(
            context.applicationContext,
            message,
            if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT,
            true
        )
        show(toast)
    }

    fun info(context: Context, message: String, long: Boolean = false) {
        val toast = Toasty.info(
            context.applicationContext,
            message,
            if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT,
            true
        )
        show(toast)
    }

    fun common(context: Context, message: String, long: Boolean = false) {
        val toast = Toasty.normal(
            context.applicationContext,
            message,
            if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        )
        show(toast)
    }
}