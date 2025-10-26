package com.example.sharkflow.data.repository.remote

import com.example.sharkflow.data.api.BoardApi
import com.example.sharkflow.data.api.dto.board.*
import com.example.sharkflow.data.mapper.BoardMapper
import com.example.sharkflow.domain.model.Board
import com.example.sharkflow.domain.repository.BoardRepository
import jakarta.inject.Inject

class BoardRepositoryImpl @Inject constructor(
    private val api: BoardApi
) : BoardRepository {

    override suspend fun getBoards(): Result<List<Board>> = runCatching {
        val response = api.getBoards()
        if (response.isSuccessful) {
            response.body()?.boards?.map { BoardMapper.fromResponseDto(it, userUuid = "") }
                ?: emptyList()
        } else throw Exception("Failed to fetch boards")
    }

    override suspend fun createBoard(title: String, color: String): Result<Board> = runCatching {
        val response = api.createBoard(CreateBoardRequestDto(title, color))
        if (response.isSuccessful) {
            BoardMapper.fromResponseDto(response.body()!!.board, userUuid = "")
        } else throw Exception("Failed to create board")
    }

    override suspend fun updateBoard(
        boardUuid: String,
        update: UpdateBoardRequestDto
    ): Result<UpdateBoardRequestDto> = runCatching {
        val response = api.updateBoard(boardUuid, update)
        if (!response.isSuccessful) throw Exception("Failed to update board")
        response.body()?.updatedBoard ?: throw Exception("Update failed")
    }

    override suspend fun deleteBoard(boardUuid: String): Result<DeletedBoardInfoDto> = runCatching {
        val response = api.deleteBoard(boardUuid)
        if (response.isSuccessful) {
            response.body()?.deletedBoard ?: throw Exception("Delete failed")
        } else throw Exception("Failed to delete board")
    }
}
