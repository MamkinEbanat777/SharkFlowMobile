package com.example.sharkflow.model

import com.google.gson.annotations.SerializedName

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

data class LogoutResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("error")
    val error: String?
)

data class DeleteUserRequest(
    @SerializedName("confirmationCode")
    val confirmationCode: String?,
)

data class DeleteUserResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("error")
    val error: String?
)

data class ConfirmationDeleteUserResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("error")
    val error: String?
)

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

data class UpdateUserResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("error")
    val error: String?
)

data class ConfirmationUpdateUserResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("error")
    val error: String?
)

data class UpdateAvatarRequest(
    @SerializedName("imgUrl")
    val imgUrl: String,
    @SerializedName("publicId")
    val publicId: String? = null,
)

data class UpdateAvatarResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("error")
    val error: String?
)

data class DeleteAvatarResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("error")
    val error: String?
)
