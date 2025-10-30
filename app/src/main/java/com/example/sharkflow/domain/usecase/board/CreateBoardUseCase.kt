package com.example.sharkflow.domain.usecase.board

import com.example.sharkflow.data.api.dto.board.BoardResponseDto
import com.example.sharkflow.domain.repository.BoardRepositoryCombined
import javax.inject.Inject

class CreateBoardUseCase @Inject constructor(
    private val repository: BoardRepositoryCombined
) {
    suspend operator fun invoke(title: String, color: String): Result<BoardResponseDto> =
        repository.createBoard(title, color)
}