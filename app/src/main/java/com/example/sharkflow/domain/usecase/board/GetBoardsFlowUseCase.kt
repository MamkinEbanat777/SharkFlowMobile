package com.example.sharkflow.domain.usecase.board

import com.example.sharkflow.domain.model.Board
import com.example.sharkflow.domain.repository.BoardRepositoryCombined
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBoardsFlowUseCase @Inject constructor(
    private val repository: BoardRepositoryCombined
) {
    operator fun invoke(): Flow<List<Board>> =
        repository.getBoardsFlow()
}