package com.example.sharkflow.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*

enum class AppButtonVariant {
    Primary, Secondary, Outline, Ghost
}

enum class AppButtonSize {
    Small, Medium, Large
}

private fun getButtonPadding(size: AppButtonSize): Dp {
    return when (size) {
        AppButtonSize.Small -> 8.dp
        AppButtonSize.Medium -> 12.dp
        AppButtonSize.Large -> 16.dp
    }
}

private fun getButtonHeight(size: AppButtonSize): Dp {
    return when (size) {
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
    variant: AppButtonVariant = AppButtonVariant.Primary,
    size: AppButtonSize = AppButtonSize.Medium,
    enabled: Boolean = true,
) {
    val colorScheme = MaterialTheme.colorScheme
    val buttonColors = when (variant) {
        AppButtonVariant.Primary -> ButtonDefaults.buttonColors(
            containerColor = colorScheme.primary,
            contentColor = colorScheme.onPrimary
        )

        AppButtonVariant.Secondary -> ButtonDefaults.buttonColors(
            containerColor = colorScheme.secondary,
            contentColor = colorScheme.onSecondary
        )

        AppButtonVariant.Outline -> ButtonDefaults.outlinedButtonColors(
            contentColor = colorScheme.primary
        )

        AppButtonVariant.Ghost -> ButtonDefaults.textButtonColors(
            contentColor = colorScheme.primary
        )
    }

    val buttonContent: @Composable () -> Unit = {
        Text(text)
    }

    val buttonModifier = modifier
        .height(getButtonHeight(size))
        .padding(horizontal = getButtonPadding(size))

    when (variant) {
        AppButtonVariant.Primary, AppButtonVariant.Secondary -> {
            Button(
                onClick = onClick,
                modifier = buttonModifier,
                colors = buttonColors,
                enabled = enabled
            ) {
                buttonContent()
            }
        }

        AppButtonVariant.Outline -> {
            OutlinedButton(
                onClick = onClick,
                modifier = buttonModifier,
                colors = buttonColors,
                enabled = enabled
            ) {
                buttonContent()
            }
        }

        AppButtonVariant.Ghost -> {
            TextButton(
                onClick = onClick,
                modifier = buttonModifier,
                colors = buttonColors,
                enabled = enabled
            ) {
                buttonContent()
            }
        }
    }
}
