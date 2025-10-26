package com.example.sharkflow.presentation.screens.board.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt


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
    var expanded by remember { mutableStateOf(false) }

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
        confirmButton = {
            TextButton(onClick = {
                onConfirm(title.trim(), selectedColor)
            }) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        },
        title = {
            Text(if (initialTitle.isEmpty()) "Создание доски" else "Редактирование доски")
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название доски") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                Text("Выберите цвет", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color("#${selectedColor}".toColorInt()))
                    )

                    Spacer(Modifier.width(12.dp))

                    Box {
                        OutlinedTextField(
                            value = "#${selectedColor.uppercase()}",
                            onValueChange = {},
                            modifier = Modifier
                                .width(160.dp)
                                .clickable { expanded = true },
                            label = { Text("HEX") },
                            trailingIcon = {
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(
                                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                        contentDescription = if (expanded) "Свернуть" else "Открыть"
                                    )
                                }
                            }
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.width(260.dp)
                        ) {
                            androidx.compose.foundation.layout.FlowRow(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                availableColors.forEach { hex ->
                                    val isSelected = hex == selectedColor
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .padding(2.dp)
                                            .background(Color("#$hex".toColorInt()))
                                            .clickable {
                                                selectedColor = hex
                                                expanded = false
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Выбран",
                                                tint = contentColorFor(
                                                    backgroundColor = Color(
                                                        "#$hex".toColorInt()
                                                    )
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.weight(1f))
                }

                Spacer(Modifier.height(12.dp))

            }
        }
    )
}
