package com.example.sharkflow.data.mapper

import com.example.sharkflow.data.api.dto.user.UserDto
import com.example.sharkflow.domain.model.User

object UserMapper {
    fun fromDto(dto: UserDto): User {
        return User(
            login = dto.login.orEmpty(),
            email = dto.email.orEmpty(),
            role = dto.role.orEmpty(),
            avatarUrl = dto.avatarUrl.orEmpty(),
            publicId = dto.publicId.orEmpty()
        )
    }
}
