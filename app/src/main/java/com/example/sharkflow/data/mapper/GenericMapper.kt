package com.example.sharkflow.data.mapper

import com.example.sharkflow.data.api.dto.common.GenericMessageResponseDto
import com.example.sharkflow.domain.model.GenericResult

object GenericMapper {
    fun fromDto(dto: GenericMessageResponseDto): GenericResult {
        return GenericResult(message = dto.message, error = dto.error)
    }
}
