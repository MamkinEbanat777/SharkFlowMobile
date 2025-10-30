package com.example.sharkflow.data.repository.combined

import com.example.sharkflow.data.api.dto.board.*
import com.example.sharkflow.data.mapper.BoardMapper
import com.example.sharkflow.data.repository.local.BoardLocalRepositoryImpl
import com.example.sharkflow.data.repository.remote.BoardRepositoryImpl
import com.example.sharkflow.domain.manager.UserManager
import com.example.sharkflow.domain.model.Board
import com.example.sharkflow.domain.repository.BoardRepositoryCombined
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.Instant

class BoardRepositoryCombinedImpl @Inject constructor(
    private val local: BoardLocalRepositoryImpl,
    private val remote: BoardRepositoryImpl,
    private val userManager: UserManager
) : BoardRepositoryCombined {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getBoardsFlow(): Flow<List<Board>> =
        userManager.currentUser.flatMapLatest { user ->
            if (user == null) emptyFlow()
            else local.getBoardsFlow(user.uuid).map { it.map(BoardMapper::fromEntity) }
        }

    override suspend fun refreshBoards() {
        val user = userManager.currentUser.value ?: return
        val remoteBoards = remote.getBoards().getOrNull() ?: return

        val boardsWithUser = remoteBoards.map { it.copy(userUuid = user.uuid) }
        local.insertOrUpdateBoards(boardsWithUser.map(BoardMapper::toEntity))
    }

    override suspend fun createBoard(title: String, color: String): Result<BoardResponseDto> =
        runCatching {
            val board = remote.createBoard(title, color).getOrNull()
                ?: throw Exception("Failed to create board remotely")
            val user = userManager.currentUser.value ?: throw Exception("User not logged in")

            val boardWithUser = board.copy(userUuid = user.uuid)
            local.insertOrUpdateBoards(listOf(BoardMapper.toEntity(boardWithUser)))

            BoardResponseDto(
                uuid = boardWithUser.uuid,
                title = boardWithUser.title,
                color = boardWithUser.color ?: "#FFFFFF",
                isPinned = boardWithUser.isPinned,
                isFavorite = boardWithUser.isFavorite,
                taskCount = boardWithUser.taskCount ?: "0",
                createdAt = boardWithUser.createdAt,
                updatedAt = boardWithUser.updatedAt
            )
        }

    override suspend fun updateBoard(
        boardUuid: String,
        update: UpdateBoardRequestDto
    ): Result<UpdateBoardRequestDto> = runCatching {
        val user = userManager.currentUser.value ?: throw Exception("User not logged in")
        val updateDto = remote.updateBoard(boardUuid, update).getOrNull()
            ?: throw Exception("Failed to update board remotely")

        val currentBoardEntity =
            local.getBoardsFlow(userUuid = user.uuid).firstOrNull()?.find { it.uuid == boardUuid }
                ?: throw Exception("Board not found locally")

        val updatedBoard = Board(
            uuid = currentBoardEntity.uuid,
            title = updateDto.title ?: currentBoardEntity.title,
            color = updateDto.color ?: currentBoardEntity.color,
            userUuid = user.uuid,
            isPinned = updateDto.isPinned ?: currentBoardEntity.isPinned,
            isFavorite = updateDto.isFavorite ?: currentBoardEntity.isFavorite,
            isDeleted = currentBoardEntity.isDeleted,
            updatedAt = Instant.now(),
            createdAt = currentBoardEntity.createdAt,
            taskCount = currentBoardEntity.taskCount
        )

        local.updateBoard(BoardMapper.toEntity(updatedBoard))
        updateDto
    }


    override suspend fun deleteBoard(boardUuid: String): Result<DeletedBoardInfoDto> = runCatching {
        val user = userManager.currentUser.value ?: throw Exception("User not logged in")
        val deleted = remote.deleteBoard(boardUuid).getOrNull()
            ?: throw Exception("Failed to delete board remotely")

        val tombstone = Board(
            uuid = boardUuid,
            title = deleted.title,
            color = "#FFFFFF",
            userUuid = user.uuid,
            isPinned = false,
            isFavorite = false,
            isDeleted = true,
            updatedAt = Instant.now(),
            createdAt = Instant.now(),
            taskCount = deleted.tasksRemoved
        )
        local.deleteBoard(BoardMapper.toEntity(tombstone))
        deleted
    }
}
