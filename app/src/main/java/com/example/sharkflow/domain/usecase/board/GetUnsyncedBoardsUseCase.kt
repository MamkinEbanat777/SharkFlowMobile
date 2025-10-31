package com.example.sharkflow.domain.usecase.board

import com.example.sharkflow.domain.model.Board
import com.example.sharkflow.domain.repository.BoardRepositoryCombined
import javax.inject.Inject

class GetUnsyncedBoardsUseCase @Inject constructor(
    private val repository: BoardRepositoryCombined
) {
    suspend operator fun invoke(): List<Board> =
        repository.getUnsyncedBoards()
}
