package com.example.sharkflow.data.mapper

import com.example.sharkflow.data.api.dto.board.*
import com.example.sharkflow.data.local.db.entities.BoardEntity
import com.example.sharkflow.domain.model.Board

object BoardMapper {
    fun fromDto(dto: BoardDto): Board = Board(
        uuid = dto.uuid,
        serverUuid = dto.serverUuid,
        title = dto.title,
        color = dto.color,
        userUuid = dto.userUuid,
        createdAt = dto.createdAt,
        updatedAt = dto.updatedAt,
        isPinned = dto.isPinned ?: false,
        isFavorite = dto.isFavorite ?: false,
        isDeleted = dto.isDeleted ?: false,
        taskCount = dto.taskCount,
    )

    fun fromResponseDto(dto: BoardResponseDto, userUuid: String): Board = Board(
        uuid = dto.uuid,
        title = dto.title,
        color = dto.color,
        userUuid = userUuid,
        createdAt = dto.createdAt,
        updatedAt = dto.updatedAt,
        isPinned = dto.isPinned,
        isFavorite = dto.isFavorite,
        isDeleted = false,
        isSynced = true,
        taskCount = dto.taskCount
    )

    fun fromUpdateDto(update: UpdateBoardRequestDto, currentBoard: Board): Board =
        currentBoard.copy(
            title = update.title ?: currentBoard.title,
            color = update.color ?: currentBoard.color,
            isPinned = update.isPinned ?: currentBoard.isPinned,
            isFavorite = update.isFavorite ?: currentBoard.isFavorite
        )

    fun toDto(board: Board): BoardDto = BoardDto(
        uuid = board.uuid,
        serverUuid = board.serverUuid,
        title = board.title,
        color = board.color,
        userUuid = board.userUuid,
        createdAt = board.createdAt,
        updatedAt = board.updatedAt,
        isPinned = board.isPinned,
        isFavorite = board.isFavorite,
        isDeleted = board.isDeleted,
        taskCount = board.taskCount
    )

    fun fromEntity(entity: BoardEntity): Board = Board(
        uuid = entity.uuid,
        serverUuid = entity.serverUuid,
        title = entity.title,
        color = entity.color,
        userUuid = entity.userUuid,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt,
        isPinned = entity.isPinned,
        isFavorite = entity.isFavorite,
        isDeleted = entity.isDeleted,
        isSynced = entity.isSynced,
        taskCount = entity.taskCount
    )

    fun toEntity(board: Board): BoardEntity = BoardEntity(
        uuid = board.uuid,
        title = board.title,
        color = board.color,
        userUuid = board.userUuid,
        createdAt = board.createdAt,
        updatedAt = board.updatedAt,
        isPinned = board.isPinned,
        isFavorite = board.isFavorite,
        isDeleted = board.isDeleted,
        isSynced = board.isSynced,
        taskCount = board.taskCount
    )
}
