package com.example.sharkflow.data.mapper

import com.example.sharkflow.data.api.dto.board.*
import com.example.sharkflow.data.local.db.entities.BoardEntity
import com.example.sharkflow.domain.model.Board
import java.time.Instant
import java.util.UUID

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

    fun toLocalEntityForCreate(title: String, color: String, userUuid: String): BoardEntity {
        return BoardEntity(
            uuid = UUID.randomUUID().toString(),
            serverUuid = null,
            title = title,
            color = color,
            userUuid = userUuid,
            isPinned = false,
            isFavorite = false,
            taskCount = "0",
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            isSynced = false,
            isDeleted = false
        )
    }

    fun mergeLocalWithRemoteAfterCreate(
        local: BoardEntity,
        remoteDto: BoardResponseDto?
    ): BoardEntity {
        if (remoteDto == null) return local
        if (local.serverUuid != null) return local
        return local.copy(
            serverUuid = remoteDto.uuid,
            title = remoteDto.title,
            color = remoteDto.color,
            isPinned = remoteDto.isPinned,
            isFavorite = remoteDto.isFavorite,
            taskCount = remoteDto.taskCount,
            isSynced = true,
            updatedAt = remoteDto.updatedAt ?: Instant.now()
        )
    }

    fun mergeEntityWithUpdate(
        local: BoardEntity,
        update: UpdateBoardRequestDto,
        remoteResponse: UpdateBoardRequestDto? = null
    ): BoardEntity {
        val merged = local.copy(
            title = update.title ?: local.title,
            color = update.color ?: local.color,
            isPinned = update.isPinned ?: local.isPinned,
            isFavorite = update.isFavorite ?: local.isFavorite,
            updatedAt = Instant.now(),
            isSynced = remoteResponse != null
        )
        return if (remoteResponse != null) {
            merged.copy(updatedAt = Instant.now() ?: merged.updatedAt)
        } else merged
    }

    fun mergeRemoteWithLocalList(
        userUuid: String,
        remoteBoards: List<Board>,
        localBoards: List<BoardEntity>
    ): List<BoardEntity> {
        val localByServer = localBoards
            .filter { it.serverUuid != null }
            .associateBy { it.serverUuid }

        val toInsertOrUpdate = mutableListOf<BoardEntity>()

        remoteBoards.forEach { remote ->
            val serverUuid = remote.serverUuid ?: remote.uuid
            val existing = localByServer[serverUuid]

            if (existing != null) {
                toInsertOrUpdate.add(
                    existing.copy(
                        serverUuid = serverUuid,
                        title = remote.title,
                        color = remote.color,
                        isPinned = remote.isPinned,
                        isFavorite = remote.isFavorite,
                        taskCount = remote.taskCount,
                        isSynced = true,
                        isDeleted = false,
                        updatedAt = remote.updatedAt ?: existing.updatedAt
                    )
                )
            } else {
                val unsyncedLocal = localBoards.find {
                    it.serverUuid == null &&
                            !it.isDeleted &&
                            it.userUuid == userUuid &&
                            it.title == remote.title
                }

                val uuidToUse = unsyncedLocal?.uuid ?: UUID.randomUUID().toString()

                toInsertOrUpdate.add(
                    BoardEntity(
                        uuid = uuidToUse,
                        serverUuid = serverUuid,
                        title = remote.title,
                        color = remote.color,
                        userUuid = userUuid,
                        isPinned = remote.isPinned,
                        isFavorite = remote.isFavorite,
                        taskCount = remote.taskCount,
                        createdAt = remote.createdAt ?: Instant.now(),
                        updatedAt = remote.updatedAt ?: Instant.now(),
                        isSynced = true,
                        isDeleted = false
                    )
                )
            }
        }

        return toInsertOrUpdate
    }


    fun mergeLocalWithRemoteAfterCreate(local: BoardEntity, serverUuid: String?): BoardEntity {
        if (serverUuid == null) return local
        if (local.serverUuid != null) return local
        return local.copy(
            serverUuid = serverUuid,
            isSynced = true,
            updatedAt = Instant.now()
        )
    }
}
