package com.example.sharkflow.data.repository.combined

import com.example.sharkflow.core.system.AppLog
import com.example.sharkflow.data.api.dto.board.*
import com.example.sharkflow.data.local.db.entities.BoardEntity
import com.example.sharkflow.data.mapper.BoardMapper
import com.example.sharkflow.data.repository.local.BoardLocalRepositoryImpl
import com.example.sharkflow.data.repository.remote.BoardRepositoryImpl
import com.example.sharkflow.domain.manager.UserManager
import com.example.sharkflow.domain.model.Board
import com.example.sharkflow.domain.repository.BoardRepositoryCombined
import jakarta.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class BoardRepositoryCombinedImpl @Inject constructor(
    private val local: BoardLocalRepositoryImpl,
    private val remote: BoardRepositoryImpl,
    private val userManager: UserManager
) : BoardRepositoryCombined {
    private val ioScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getBoardsFlow(): Flow<List<Board>> =
        userManager.currentUser.flatMapLatest { user ->
            if (user == null) emptyFlow()
            else local.getBoardsFlow(user.uuid).map { it.map(BoardMapper::fromEntity) }
        }

    override suspend fun refreshBoards() {
        val user = userManager.currentUser.value ?: return
        val remoteBoards = try {
            remote.getBoards().getOrNull()
        } catch (_: Exception) {
            null
        } ?: return

        val localEntities = local.getBoardsOnce(user.uuid)
        val merged = BoardMapper.mergeRemoteWithLocalList(user.uuid, remoteBoards, localEntities)
        local.insertOrUpdateBoards(merged)
    }

    override suspend fun createBoard(
        title: String,
        color: String,
        localUuid: String?
    ): Result<Board> = runCatching {
        val user = userManager.currentUser.value ?: throw Exception("User not logged in")

        val existingLocal: BoardEntity? = localUuid?.let { local.getByLocalUuid(it) }
        val tempEntity = existingLocal?.copy(
            title = title,
            color = color,
            isSynced = false,
            isDeleted = false
        ) ?: BoardMapper.toLocalEntityForCreate(title = title, color = color, userUuid = user.uuid)

        local.insertOrUpdateBoards(listOf(tempEntity))

        val remoteBoard: Board? = try {
            remote.createBoard(title, color).getOrNull()
        } catch (e: Exception) {
            null
        }

        val serverUuidFromRemote: String? = remoteBoard?.serverUuid ?: remoteBoard?.uuid

        val mergedEntity =
            BoardMapper.mergeLocalWithRemoteAfterCreate(tempEntity, serverUuidFromRemote)
        local.updateBoard(mergedEntity)

        BoardMapper.fromEntity(mergedEntity)
    }


    override suspend fun updateBoard(
        boardUuid: String,
        update: UpdateBoardRequestDto
    ): Result<Board> = runCatching {
        val localEntity = local.getBoardsOnce(
            userManager.currentUser.value?.uuid ?: throw Exception("User not logged in")
        ).firstOrNull { it.uuid == boardUuid || it.serverUuid == boardUuid }
            ?: throw IllegalStateException("Board not found locally for uuid: $boardUuid")

        val updatedLocal =
            BoardMapper.mergeEntityWithUpdate(localEntity, update).copy(isSynced = false)
        local.updateBoard(updatedLocal)

        ioScope.launch {
            localEntity.serverUuid?.let { serverUuid ->
                try {
                    val remoteUpdate = remote.updateBoard(serverUuid, update).getOrNull()
                    val merged =
                        BoardMapper.mergeEntityWithUpdate(updatedLocal, update, remoteUpdate)
                    local.updateBoard(merged)
                } catch (_: Exception) {
                }
            }
        }

        BoardMapper.fromEntity(updatedLocal)
    }

    override suspend fun deleteBoard(
        boardUuid: String
    ): Result<DeletedBoardInfoDto> = runCatching {
        val localEntity = local.getAllBoards()
            .firstOrNull { it.uuid == boardUuid || it.serverUuid == boardUuid }
            ?: return@runCatching DeletedBoardInfoDto("Unknown board", tasksRemoved = "0")

        if (localEntity.serverUuid == null) local.deleteBoard(localEntity)
        else local.updateBoard(localEntity.copy(isDeleted = true, isSynced = false))

        ioScope.launch {
            localEntity.serverUuid?.let { serverUuid ->
                try {
                    val remoteResult = remote.deleteBoard(serverUuid).getOrNull()
                    if (remoteResult != null) {
                        val current = local.getByLocalUuid(localEntity.uuid) ?: localEntity
                        val updated = current.copy(isDeleted = true, isSynced = true)
                        local.updateBoard(updated)
                    }
                } catch (e: Exception) {
                    AppLog.e(
                        "BoardRepository",
                        "Failed to delete board remotely for ${localEntity.uuid}",
                        e
                    )
                }
            }
        }

        DeletedBoardInfoDto(
            title = localEntity.title,
            tasksRemoved = localEntity.taskCount ?: "0"
        )
    }

    override suspend fun getAllBoards(): List<Board> =
        local.getAllBoards().map(BoardMapper::fromEntity)

    override suspend fun getUnsyncedBoards(): List<Board> =
        local.getUnsyncedBoards().map(BoardMapper::fromEntity)

    override suspend fun getDeletedBoards(): List<Board> =
        local.getDeletedBoards().map(BoardMapper::fromEntity)
}
