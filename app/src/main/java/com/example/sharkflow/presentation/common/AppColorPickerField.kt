package com.example.sharkflow.presentation.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt

@Composable
fun AppColorPickerField(
    value: String,
    availableColors: List<String>,
    label: String,
    onColorSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        AppField(
            value = "#${value.uppercase()}",
            onValueChange = {},
            readOnly = true,
            label = label,
            trailing = {
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
            FlowRow(
                modifier = Modifier.padding(12.dp)
            ) {
                availableColors.forEach { hex ->
                    val isSelected = hex.equals(value, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(2.dp)
                            .background(Color("#$hex".toColorInt()), CircleShape)
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                onColorSelected(hex)
                                expanded = false
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Выбран",
                                tint = contentColorFor(Color("#$hex".toColorInt()))
                            )
                        }
                    }
                }
            }
        }
    }
}
