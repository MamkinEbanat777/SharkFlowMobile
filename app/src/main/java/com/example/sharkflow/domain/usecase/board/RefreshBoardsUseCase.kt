package com.example.sharkflow.domain.usecase.board

import com.example.sharkflow.domain.repository.BoardRepositoryCombined
import javax.inject.Inject

class RefreshBoardsUseCase @Inject constructor(
    private val repository: BoardRepositoryCombined
) {
    suspend operator fun invoke() =
        repository.refreshBoards()
}