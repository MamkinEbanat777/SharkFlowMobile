package com.example.sharkflow.presentation.screens.board.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.sharkflow.core.common.DateUtils.formatDateTimeReadable
import com.example.sharkflow.domain.model.Board

@Composable
fun BoardRow(
    board: Board,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val boardColor = Color("#${board.color ?: "FFFFFF"}".toColorInt())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(
                color = boardColor,
                width = 2.dp,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = colorScheme.primary),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = board.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.onPrimary
                )
                board.taskCount?.let {
                    Text(
                        text = "Задач: $it",
                        style = MaterialTheme.typography.titleSmall,
                        color = colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Создано",
                        tint = colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = board.createdAt?.let { formatDateTimeReadable(it.toString()) }
                            ?: "—",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onPrimary
                    )

                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Update,
                        contentDescription = "Обновлено",
                        tint = colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = board.updatedAt?.let { formatDateTimeReadable(it.toString()) }
                            ?: "—",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onPrimary
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        tint = colorScheme.onPrimary,
                        contentDescription = "Редактировать"
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        tint = colorScheme.error,
                        contentDescription = "Удалить"
                    )
                }
            }
        }
    }
}
