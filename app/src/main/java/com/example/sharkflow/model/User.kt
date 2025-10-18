package com.example.sharkflow.model

import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    override val message: String? = null,
    override val error: String? = null
) : BaseResponse(message, error)

data class DeleteUserResponse(
    override val message: String? = null,
    override val error: String? = null
) : BaseResponse(message, error)

data class UpdateUserResponse(
    override val message: String? = null,
    override val error: String? = null
) : BaseResponse(message, error)

data class ConfirmationUpdateUserResponse(
    override val message: String? = null,
    override val error: String? = null
) : BaseResponse(message, error)

data class UpdateAvatarResponse(
    override val message: String? = null,
    override val error: String? = null
) : BaseResponse(message, error)


data class DeleteAvatarResponse(
    override val message: String? = null,
    override val error: String? = null
) : BaseResponse(message, error)


data class UpdateAvatarRequest(
    @SerializedName("imgUrl")
    val imgUrl: String,
    @SerializedName("publicId")
    val publicId: String? = null,
)

data class DeleteUserRequest(
    @SerializedName("confirmationCode")
    val confirmationCode: String?,
)

data class ConfirmationDeleteUserResponse(
    override val message: String? = null,
    override val error: String? = null
) : BaseResponse(message, error)


data class UpdatedFields(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("login")
    val login: String? = null
)


data class UpdateUserRequest(
    @SerializedName("confirmationCode")
    val confirmationCode: String,
    @SerializedName("updatedFields")
    val updatedFields: UpdatedFields? = null
)


data class UserResponse(
    @SerializedName("login")
    val login: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("role")
    val role: String?,
    @SerializedName("avatarUrl")
    val avatarUrl: String?
)