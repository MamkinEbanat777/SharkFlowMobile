package com.example.sharkflow.ui.components

import android.content.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.core.net.*


@Composable
fun Link(
    fullText: String,
    linkText: String,
    url: String
) {
    val context = LocalContext.current

    val annotatedText = remember(fullText, linkText, url) {
        buildAnnotatedString {
            val startIndex = fullText.indexOf(linkText).coerceAtLeast(0)
            val endIndex = (startIndex + linkText.length).coerceAtLeast(startIndex)

            append(fullText)

            if (startIndex < endIndex) {
                addStyle(
                    style = SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Medium
                    ),
                    start = startIndex,
                    end = endIndex
                )

                addStringAnnotation(
                    tag = "URL",
                    annotation = url,
                    start = startIndex,
                    end = endIndex
                )
            }
        }
    }

    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    Text(
        text = annotatedText,
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(annotatedText) {
                detectTapGestures { offset ->
                    val layoutResult = textLayoutResult ?: return@detectTapGestures
                    val position = layoutResult.getOffsetForPosition(offset)
                    annotatedText.getStringAnnotations(
                        tag = "URL",
                        start = position,
                        end = position
                    ).firstOrNull()?.let { stringAnnotation ->
                        val intent = Intent(Intent.ACTION_VIEW, stringAnnotation.item.toUri())
                        context.startActivity(intent)
                    }
                }
            },
        onTextLayout = { textLayoutResult = it },
        maxLines = Int.MAX_VALUE,
        overflow = TextOverflow.Ellipsis
    )
}
