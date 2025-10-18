package com.example.sharkflow.ui.screens.profile.components

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil3.compose.AsyncImage
import com.example.sharkflow.ui.screens.profile.viewmodel.UserProfileViewModel
import com.example.sharkflow.utils.ToastManager
import com.theartofdev.edmodo.cropper.*

@Composable
fun ProfileAvatar(
    modifier: Modifier = Modifier,
    size: Dp = 240.dp,
    iconSize: Dp = 80.dp,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    userProfileViewModel: UserProfileViewModel
) {
    val currentUser by userProfileViewModel.currentUser.collectAsState()

    var isUploading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val avatarUrl = currentUser?.avatarUrl?.trim()

    val cropImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val resultUri = CropImage.getActivityResult(result.data)?.uri
            resultUri?.let { uri ->
                val contentResolver = context.contentResolver
                val inputStream = contentResolver.openInputStream(uri)
                val imageBytes = inputStream?.readBytes()
                inputStream?.close()

                imageBytes?.let { bytes ->
                    isUploading = true
                    userProfileViewModel.uploadUserAvatar(bytes) { success, url, publicId ->
                        isUploading = false
                        if (success && url != null && publicId != null) {
                            userProfileViewModel.setAvatarPublicId(publicId)
                            userProfileViewModel.updateUserAvatar(
                                url,
                                publicId
                            ) { updateSuccess, message ->
                                if (!updateSuccess) {
                                    Toast.makeText(
                                        context,
                                        message ?: "Ошибка обновления аватара",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Ошибка загрузки аватара", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        } else {
            Toast.makeText(context, "Image crop failed", Toast.LENGTH_SHORT).show()
        }
    }


    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                try {
                    val cropIntent = CropImage.activity(uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .getIntent(context)
                    cropImageLauncher.launch(cropIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        context,
                        "Error starting crop: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    )
    var isImageExpanded by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(BorderStroke(3.dp, borderColor), CircleShape)
                .clickable { isImageExpanded = true }
        ) {
            if (isUploading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = borderColor
                )
            } else if (!avatarUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "User Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Default Avatar",
                    tint = borderColor.copy(alpha = 0.8f),
                    modifier = Modifier.size(iconSize)
                )
            }
        }

        if (isImageExpanded) {
            AlertDialog(
                onDismissRequest = { isImageExpanded = false },
                title = { Text("Ваш аватар", style = MaterialTheme.typography.titleMedium) },
                text = {
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = "Expanded Avatar",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(onClick = { isImageExpanded = false }) {
                        Text("Close")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedButton(
                onClick = { imagePickerLauncher.launch("image/*") },
                shape = CircleShape,
                border = BorderStroke(1.dp, borderColor),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Change photo",
                    modifier = Modifier.size(18.dp),
                    tint = borderColor
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "Изменить",
                    color = borderColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            if (!avatarUrl.isNullOrBlank()) {
                OutlinedButton(
                    onClick = { showDeleteConfirm = true },
                    shape = CircleShape,
                    border = BorderStroke(1.dp, Color.Red),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete photo",
                        modifier = Modifier.size(18.dp),
                        tint = Color.Red
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Удалить",
                        color = Color.Red,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        if (showDeleteConfirm) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirm = false },
                title = { Text("Подтверждение") },
                text = { Text("Вы точно хотите удалить аватар?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDeleteConfirm = false
                        isUploading = true
                        userProfileViewModel.deleteUserAvatar { deleteSuccess, deleteMessage ->
                            isUploading = false
                            if (!deleteSuccess) {
                                ToastManager.error(
                                    context,
                                    deleteMessage ?: "Ошибка при удалении аватара"
                                )
                            }
                        }
                    }) { Text("Да") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirm = false }) { Text("Отмена") }
                }
            )
        }

    }
}
