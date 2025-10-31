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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.example.sharkflow.core.common.DateUtils.formatDateTimeReadable
import com.example.sharkflow.data.api.dto.board.UpdateBoardRequestDto
import com.example.sharkflow.domain.model.Board
import com.example.sharkflow.presentation.screens.board.viewmodel.BoardsViewModel
import com.example.sharkflow.presentation.theme.SuccessColor

@Composable
fun BoardRow(
    board: Board,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    boardsViewModel: BoardsViewModel
) {
    val boardColor = Color("#${board.color ?: "FFFFFF"}".toColorInt())

    val syncColor =
        if (board.isSynced) SuccessColor else colorScheme.error

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.primary),
        border = BorderStroke(1.5.dp, boardColor.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(boardColor, RoundedCornerShape(50))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = board.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = colorScheme.onPrimary
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    board.taskCount?.let {
                        Text(
                            text = "$it задач",
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(26.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Редактировать",
                            tint = colorScheme.onPrimary
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(26.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Удалить",
                            tint = colorScheme.error
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = "Создано",
                        tint = colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = board.createdAt?.let { formatDateTimeReadable(it.toString()) }
                            ?: "—",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Update,
                        contentDescription = "Обновлено",
                        tint = colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = board.updatedAt?.let { formatDateTimeReadable(it.toString()) }
                            ?: "—",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (board.isSynced) "Синхронизировано" else "Не синхронизировано",
                    style = MaterialTheme.typography.labelSmall,
                    color = syncColor,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = {
                            boardsViewModel.updateBoard(
                                board.uuid,
                                UpdateBoardRequestDto(
                                    isPinned = !board.isPinned,
                                    isFavorite = board.isFavorite
                                )
                            )
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        val pinTint =
                            if (board.isPinned) SuccessColor else colorScheme.onPrimary
                        Icon(
                            Icons.Default.PushPin,
                            contentDescription = if (board.isPinned) "Открепить" else "Закрепить",
                            tint = pinTint,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            boardsViewModel.updateBoard(
                                board.uuid,
                                UpdateBoardRequestDto(
                                    isPinned = board.isPinned,
                                    isFavorite = !board.isFavorite
                                )
                            )
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        val starTint =
                            if (board.isFavorite) Color(0xFFFFD700) else colorScheme.onPrimary
                        val starIcon =
                            if (board.isFavorite) Icons.Default.Star else Icons.Default.StarBorder
                        Icon(
                            starIcon,
                            contentDescription = if (board.isFavorite) "Убрать из избранного" else "В избранное",
                            tint = starTint,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}