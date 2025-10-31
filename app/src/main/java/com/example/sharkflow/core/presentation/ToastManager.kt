package com.example.sharkflow.core.presentation

import android.content.Context
import android.media.*
import android.view.Gravity
import android.widget.Toast
import androidx.core.content.edit
import com.example.sharkflow.R
import es.dmoral.toasty.Toasty

object ToastManager {
    private var currentToast: Toast? = null
    private const val POS_TOP_CENTER = Gravity.TOP or Gravity.CENTER_HORIZONTAL
    private const val OFFSET_X = 0
    private const val OFFSET_Y = 200

    private var soundPool: SoundPool? = null
    private var successSoundId = 0
    private var errorSoundId = 0
    private var warningSoundId = 0
    private var infoSoundId = 0
    private var initialized = false

    private const val PREFS_NAME = "app_settings"
    private const val KEY_SOUND_ENABLED = "sound_enabled"

    private var isSoundEnabled = true

    private fun initSounds(context: Context) {
        if (initialized) return

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(audioAttributes)
            .build()

        successSoundId = soundPool?.load(context, R.raw.success, 1) ?: 0
        errorSoundId = soundPool?.load(context, R.raw.error, 1) ?: 0
        warningSoundId = soundPool?.load(context, R.raw.warning, 1) ?: 0
        infoSoundId = soundPool?.load(context, R.raw.info, 1) ?: 0

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        isSoundEnabled = prefs.getBoolean(KEY_SOUND_ENABLED, true)

        initialized = true
    }

    private fun playSound(soundId: Int) {
        if (!isSoundEnabled) return
        soundPool?.play(soundId, 1f, 1f, 0, 0, 1f)
    }

    private fun show(toast: Toast) {
        currentToast?.cancel()
        currentToast = toast
        toast.setGravity(POS_TOP_CENTER, OFFSET_X, OFFSET_Y)
        toast.show()

        toast.view?.postDelayed({ currentToast = null }, 1000)
    }

    fun success(context: Context, message: String, long: Boolean = false) {
        initSounds(context)
        playSound(successSoundId)

        val toast = Toasty.success(
            context.applicationContext,
            message,
            if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT,
            true
        )
        show(toast)
    }

    fun warning(context: Context, message: String, long: Boolean = false) {
        initSounds(context)
        playSound(warningSoundId)

        val toast = Toasty.warning(
            context.applicationContext,
            message,
            if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT,
            true
        )
        show(toast)
    }

    fun error(context: Context, message: String, long: Boolean = false) {
        initSounds(context)
        playSound(errorSoundId)

        val toast = Toasty.error(
            context.applicationContext,
            message,
            if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT,
            true
        )
        show(toast)
    }

    fun info(context: Context, message: String, long: Boolean = false) {
        initSounds(context)
        playSound(infoSoundId)

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

    fun toggleSound(context: Context) {
        isSoundEnabled = !isSoundEnabled
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            putBoolean(KEY_SOUND_ENABLED, isSoundEnabled)
        }
    }

    fun isSoundOn(context: Context): Boolean {
        initSounds(context)
        return isSoundEnabled
    }
}