package com.example.sharkflow.data.mapper

import com.example.sharkflow.data.api.dto.user.UserResponseDto
import com.example.sharkflow.domain.model.User

object UserMapper {
    fun fromDto(dto: UserResponseDto): User {
        return User(
            login = dto.login.orEmpty(),
            email = dto.email.orEmpty(),
            role = dto.role.orEmpty(),
            avatarUrl = dto.avatarUrl
        )
    }
}
