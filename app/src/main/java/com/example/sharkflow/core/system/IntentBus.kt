package com.example.sharkflow.core.system

import android.content.Intent
import kotlinx.coroutines.flow.MutableSharedFlow

object IntentBus {
    val flow = MutableSharedFlow<Intent>(extraBufferCapacity = 4)
}
