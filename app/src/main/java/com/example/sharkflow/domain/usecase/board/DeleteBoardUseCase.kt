package com.example.sharkflow.domain.usecase.board

import com.example.sharkflow.data.api.dto.board.DeletedBoardInfoDto
import com.example.sharkflow.domain.repository.BoardRepositoryCombined
import javax.inject.Inject

class DeleteBoardUseCase @Inject constructor(
    private val repository: BoardRepositoryCombined
) {
    suspend operator fun invoke(boardUuid: String): Result<DeletedBoardInfoDto> =
        repository.deleteBoard(boardUuid)
}