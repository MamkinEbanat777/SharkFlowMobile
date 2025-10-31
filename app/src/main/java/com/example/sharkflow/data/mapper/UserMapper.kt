package com.example.sharkflow.data.mapper

import com.example.sharkflow.data.api.dto.user.UserDto
import com.example.sharkflow.data.local.db.entities.UserEntity
import com.example.sharkflow.domain.model.User

object UserMapper {
    fun fromDto(dto: UserDto): User = User(
        uuid = dto.uuid,
        login = dto.login,
        email = dto.email,
        avatarUrl = dto.avatarUrl,
        publicId = dto.publicId,
        role = dto.role
    )

    fun fromDomain(user: User): UserEntity = UserEntity(
        uuid = user.uuid,
        login = user.login,
        email = user.email,
        avatarUrl = user.avatarUrl,
        publicId = user.publicId,
        role = user.role,
        isActive = user.isActive
    )

    fun toDomain(entity: UserEntity): User = User(
        uuid = entity.uuid,
        login = entity.login,
        email = entity.email,
        avatarUrl = entity.avatarUrl,
        publicId = entity.publicId,
        role = entity.role,
        isActive = entity.isActive
    )
}
