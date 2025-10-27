package com.example.sharkflow.utils

import android.content.Intent
import kotlinx.coroutines.flow.MutableSharedFlow

object IntentBus {
    val flow = MutableSharedFlow<Intent>(extraBufferCapacity = 4)
}
