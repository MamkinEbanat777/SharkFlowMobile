package com.example.sharkflow.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.PopupProperties
import com.example.sharkflow.presentation.common.AppField

@Composable
fun <T> AppDropdownField(
    value: T,
    options: List<T>,
    label: String,
    modifier: Modifier = Modifier,
    valueText: (T) -> String = { it.toString() },
    onOptionSelected: (T) -> Unit,
    leadingContent: (@Composable (T) -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        AppField(
            value = valueText(value),
            onValueChange = {},
            readOnly = true,
            label = label,
            trailing = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            properties = PopupProperties(focusable = false)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(valueText(option)) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    leadingIcon = {
                        if (leadingContent != null) leadingContent(option)
                    }
                )
            }
        }
    }
}
