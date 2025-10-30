package com.example.sharkflow.presentation.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import kotlinx.coroutines.*

const val OTP_VIEW_TYPE_UNDERLINE = 1
const val OTP_VIEW_TYPE_BORDER = 2

@Composable
fun OtpView(
    modifier: Modifier = Modifier,
    otpText: String = "",
    charColor: Color = Color.Black,
    charBackground: Color = Color.Transparent,
    charSize: TextUnit = 16.sp,
    containerSize: Dp = charSize.value.dp * 2,
    otpCount: Int = 4,
    type: Int = OTP_VIEW_TYPE_UNDERLINE,
    enabled: Boolean = true,
    password: Boolean = false,
    passwordChar: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    onOtpTextChange: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isFocused by remember { mutableStateOf(false) }

    val cursorVisible = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    var cursorJob by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(isFocused) {
        if (isFocused) {
            cursorJob?.cancel()
            cursorJob = scope.launch {
                while (true) {
                    cursorVisible.value = !cursorVisible.value
                    delay(500L)
                }
            }
        } else {
            cursorJob?.cancel()
            cursorVisible.value = false
        }
    }


    BasicTextField(
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged { state ->
                isFocused = state.isFocused
                if (state.isFocused) keyboardController?.show()
            }
            .clickable {
                focusRequester.requestFocus()
                keyboardController?.show()
            },
        value = otpText,
        onValueChange = {
            val filtered = it.filter(Char::isDigit).take(otpCount)
            onOtpTextChange(filtered)
            if (filtered.length == otpCount) keyboardController?.hide()
        },
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        decorationBox = { _ ->
            Row(
                modifier = Modifier.wrapContentWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(otpCount) { index ->
                    CharView(
                        index = index,
                        text = otpText,
                        charColor = charColor,
                        charSize = charSize,
                        containerSize = containerSize,
                        type = type,
                        charBackground = charBackground,
                        password = password,
                        passwordChar = passwordChar,
                        isActive = index == otpText.length,
                        isFocused = isFocused,
                        cursorVisible = cursorVisible.value,
                        onCellClick = {
                            focusRequester.requestFocus()
                            keyboardController?.show()
                        }
                    )
                }
            }
        }
    )
}


@Composable
private fun CharView(
    index: Int,
    text: String,
    charColor: Color,
    charSize: TextUnit,
    containerSize: Dp,
    type: Int = OTP_VIEW_TYPE_UNDERLINE,
    charBackground: Color = Color.Transparent,
    password: Boolean = false,
    passwordChar: String = "",
    isActive: Boolean,
    isFocused: Boolean,
    cursorVisible: Boolean,
    onCellClick: () -> Unit
) {
    val adjustedHeight = containerSize + 12.dp

    val char = when {
        index >= text.length -> ""
        password -> passwordChar
        else -> text[index].toString()
    }

    val cellBase = Modifier
        .size(containerSize, adjustedHeight)
        .background(charBackground)
        .clickable { onCellClick() }

    val bordered = if (type == OTP_VIEW_TYPE_BORDER) {
        cellBase
            .border(1.dp, charColor, MaterialTheme.shapes.medium)
            .padding(4.dp)
    } else cellBase

    Box(
        modifier = bordered,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char,
            fontSize = charSize,
            color = charColor,
            textAlign = TextAlign.Center
        )
        if (isActive && char.isEmpty() && isFocused && cursorVisible) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(containerSize * 0.5f)
                    .background(charColor)
            )
        }

        if (type == OTP_VIEW_TYPE_UNDERLINE) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(1.dp)
                    .width(containerSize)
                    .background(charColor)
            )
        }
    }
}