package com.example.sharkflow.presentation.common


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.*

enum class AppButtonVariant {
    Filled, Outlined, Text
}

enum class AppButtonTone {
    Default, Danger
}

enum class AppButtonSize {
    ExtraSmall, Small, Medium, Large
}

private fun getButtonPadding(size: AppButtonSize): Dp {
    return when (size) {
        AppButtonSize.ExtraSmall -> 4.dp
        AppButtonSize.Small -> 8.dp
        AppButtonSize.Medium -> 12.dp
        AppButtonSize.Large -> 16.dp
    }
}

private fun getButtonHeight(size: AppButtonSize): Dp {
    return when (size) {
        AppButtonSize.ExtraSmall -> 24.dp
        AppButtonSize.Small -> 32.dp
        AppButtonSize.Medium -> 40.dp
        AppButtonSize.Large -> 48.dp
    }
}

@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    variant: AppButtonVariant = AppButtonVariant.Filled,
    size: AppButtonSize = AppButtonSize.Medium,
    tone: AppButtonTone = AppButtonTone.Default,
    icon: ImageVector? = null,
    enabled: Boolean = true,
) {
    val colorScheme = MaterialTheme.colorScheme

    val containerColor = when (tone) {
        AppButtonTone.Default -> colorScheme.primary
        AppButtonTone.Danger -> colorScheme.error
    }

    val contentColor = when (tone) {
        AppButtonTone.Default -> colorScheme.onPrimary
        AppButtonTone.Danger -> colorScheme.onPrimary
    }

    val outlineColor = when (tone) {
        AppButtonTone.Default -> colorScheme.primary
        AppButtonTone.Danger -> colorScheme.error
    }

    val buttonColors = when (variant) {
        AppButtonVariant.Filled -> ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )

        AppButtonVariant.Outlined -> ButtonDefaults.outlinedButtonColors(
            contentColor = outlineColor
        )

        AppButtonVariant.Text -> ButtonDefaults.textButtonColors(
            contentColor = outlineColor
        )
    }

    val buttonModifier = modifier
        .height(getButtonHeight(size))
        .padding(horizontal = getButtonPadding(size))

    val content: @Composable RowScope.() -> Unit = {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(end = if (text.isNotEmpty()) 8.dp else 0.dp)
            )
        }
        Text(text)
    }

    when (variant) {
        AppButtonVariant.Filled -> Button(
            onClick = onClick,
            modifier = buttonModifier,
            colors = buttonColors,
            enabled = enabled,
            content = content
        )

        AppButtonVariant.Outlined -> OutlinedButton(
            onClick = onClick,
            modifier = buttonModifier,
            colors = buttonColors,
            enabled = enabled,
            content = content,
            border = BorderStroke(
                width = ButtonDefaults.outlinedButtonBorder(enabled = enabled).width,
                brush = SolidColor(outlineColor)
            ),
        )

        AppButtonVariant.Text -> TextButton(
            onClick = onClick,
            modifier = buttonModifier,
            colors = buttonColors,
            enabled = enabled,
            content = content
        )
    }
}