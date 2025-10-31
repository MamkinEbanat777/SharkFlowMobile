package com.example.sharkflow.domain.usecase.board

import com.example.sharkflow.data.api.dto.board.UpdateBoardRequestDto
import com.example.sharkflow.domain.model.Board
import com.example.sharkflow.domain.repository.BoardRepositoryCombined
import javax.inject.Inject

class UpdateBoardUseCase @Inject constructor(
    private val repository: BoardRepositoryCombined
) {
    suspend operator fun invoke(
        boardUuid: String,
        update: UpdateBoardRequestDto
    ): Result<Board> =
        repository.updateBoard(boardUuid, update)
}