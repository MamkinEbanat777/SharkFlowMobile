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
    override fun getBoardsFlow(userId: Int): Flow<List<Board>> =
        userManager.currentUser.flatMapLatest { user ->
            if (user == null) emptyFlow()
            else local.getBoardsFlow(userUuid = user.uuid).map { it.map(BoardMapper::fromEntity) }
        }

    override suspend fun refreshBoards(userId: Int) {
        val user = userManager.currentUser.value ?: return
        val remoteBoards = remote.getBoards().getOrNull() ?: return
        val boardsWithUser = remoteBoards.map { it.copy(userUuid = user.uuid) }
        local.insertOrUpdateBoards(boardsWithUser.map(BoardMapper::toEntity))
    }

    override suspend fun createBoard(title: String, color: String): Result<Board> =
        remote.createBoard(title, color).onSuccess { board ->
            val userUuid = userManager.currentUser.value?.uuid ?: ""
            val entity = BoardMapper.toEntity(board.copy(userUuid = userUuid))
            local.insertOrUpdateBoards(listOf(entity))
        }

    override suspend fun updateBoard(
        boardUuid: String,
        update: UpdateBoardRequestDto
    ): Result<Board> = runCatching {
        val user = userManager.currentUser.value ?: throw Exception("User not logged in")
        val updatedFields = remote.updateBoard(boardUuid, update).getOrNull()
            ?: throw Exception("Failed to update board remotely")

        val currentBoardEntity =
            local.getBoardsFlow(userUuid = user.uuid).first().find { it.uuid == boardUuid }
                ?: throw Exception("Board not found locally")

        val updatedBoard = Board(
            uuid = currentBoardEntity.uuid,
            title = updatedFields.title ?: currentBoardEntity.title,
            color = updatedFields.color ?: currentBoardEntity.color,
            userUuid = user.uuid,
            isPinned = updatedFields.isPinned ?: currentBoardEntity.isPinned,
            isFavorite = updatedFields.isFavorite ?: currentBoardEntity.isFavorite,
            isDeleted = currentBoardEntity.isDeleted,
            updatedAt = Instant.now(),
            createdAt = currentBoardEntity.createdAt,
            taskCount = currentBoardEntity.taskCount
        )

        local.updateBoard(BoardMapper.toEntity(updatedBoard))
        updatedBoard
    }

    override suspend fun deleteBoard(boardUuid: String): Result<DeletedBoardInfoDto> =
        remote.deleteBoard(boardUuid).onSuccess { deleted ->
            val userUuid = userManager.currentUser.value?.uuid ?: ""
            val entity = BoardMapper.toEntity(
                Board(
                    uuid = boardUuid,
                    title = deleted.title,
                    color = "#FFFFFF",
                    userUuid = userUuid,
                    isPinned = false,
                    isFavorite = false,
                    isDeleted = true,
                    updatedAt = Instant.now(),
                    createdAt = Instant.now(),
                    taskCount = deleted.tasksRemoved
                )
            )
            local.deleteBoard(entity)
        }
}
