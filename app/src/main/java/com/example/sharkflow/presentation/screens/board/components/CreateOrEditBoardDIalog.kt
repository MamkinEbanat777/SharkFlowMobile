package com.example.sharkflow.presentation.screens.board.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.sharkflow.presentation.common.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrEditBoardDialog(
    initialTitle: String = "",
    initialColor: String = "ffffff",
    onDismiss: () -> Unit,
    onConfirm: (title: String, color: String) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var selectedColor by remember {
        mutableStateOf(
            initialColor.trim().removePrefix("#").lowercase()
        )
    }

    val availableColors = listOf(
        "ffffff",
        "ff0000",
        "00ff00",
        "0000ff",
        "ff00ff",
        "00ffff",
        "000000",
        "808080",
        "ffa500",
        "800080",
        "ffff00",
        "008000"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colorScheme.background,
        confirmButton = {
            AppButton(onClick = {
                onConfirm(title.trim(), selectedColor)
            }, text = "Сохранить", variant = AppButtonVariant.Text)
        },
        dismissButton = {
            AppButton(onClick = onDismiss, text = "Отмена", variant = AppButtonVariant.Text)
        },
        title = {
            Text(if (initialTitle.isEmpty()) "Создание доски" else "Редактирование доски")
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                AppField(title, { title = it }, "Название доски")
                Spacer(Modifier.height(12.dp))

                Text("Выберите цвет", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                Color("#${selectedColor}".toColorInt()),
                                shape = CircleShape
                            )
                            .border(
                                width = 1.dp,
                                shape = CircleShape,
                                color = colorScheme.onSurface
                            )
                    )

                    Spacer(Modifier.width(12.dp))

                    AppColorPickerField(
                        value = selectedColor,
                        availableColors = availableColors,
                        label = "HEX",
                        onColorSelected = { selectedColor = it },
                        modifier = Modifier
                            .width(180.dp)
                    )

                    Spacer(Modifier.weight(1f))
                }

                Spacer(Modifier.height(12.dp))

            }
        }
    )
}
