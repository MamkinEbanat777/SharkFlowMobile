package com.example.sharkflow.presentation.screens.profile.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.sharkflow.core.common.DateUtils.formatDateTimeReadable
import com.example.sharkflow.core.common.UaParser
import com.example.sharkflow.domain.model.UserSession
import com.example.sharkflow.presentation.common.*
import com.example.sharkflow.presentation.screens.auth.viewmodel.AuthStateViewModel
import com.example.sharkflow.presentation.screens.profile.viewmodel.UserProfileViewModel
import com.example.sharkflow.presentation.theme.SuccessColor

@Composable
fun UserSessionsModal(
    onDismiss: () -> Unit,
    userProfileViewModel: UserProfileViewModel,
    authStateViewModel: AuthStateViewModel,
) {
    val sessions by userProfileViewModel.sessions.collectAsState()
    val isLoading by userProfileViewModel.isLoadingSessions.collectAsState()
    val errorMessage by userProfileViewModel.sessionsError.collectAsState()
    val currentDeviceId by userProfileViewModel.currentDeviceId.collectAsState()

    var showAllDevicesLogoutModalOpen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        userProfileViewModel.loadSessions()
    }

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = {
            Text("Ваши сессии", style = typography.titleMedium)
        },
        text = {

            Column(modifier = Modifier.fillMaxWidth()) {
                if (isLoading) {
                    Text(
                        text = "Сессии загружаются...",
                        style = typography.labelMedium
                    )
                }
                if (sessions.isEmpty() && (errorMessage.isNullOrEmpty()) && !isLoading) {
                    Text(
                        text = "У вас нет активных сессий",
                        style = typography.bodyMedium
                    )
                }

                if (!errorMessage.isNullOrEmpty()) {
                    Text(
                        text = errorMessage!!,
                        color = colorScheme.error,
                        style = typography.bodySmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    if (sessions.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            AppButton(
                                onClick = { userProfileViewModel.loadSessions() },
                                modifier = Modifier.fillMaxWidth(),
                                text = "Обновить",
                                icon = Icons.Default.Refresh,
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            AppButton(
                                onClick = { showAllDevicesLogoutModalOpen = true },
                                text = "Выйти из всех",
                                icon = Icons.AutoMirrored.Filled.Logout,
                                modifier = Modifier.fillMaxWidth(),
                                tone = AppButtonTone.Danger,
                                variant = AppButtonVariant.Outlined
                            )

                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .fillMaxWidth()
                ) {
                    sessions.forEach { session ->
                        val isCurrent = session.deviceId == currentDeviceId
                        val parsedAgent =
                            remember(session.userAgent) { UaParser.parse(session.userAgent) }

                        val browserOsText =
                            if (parsedAgent.browser.equals(parsedAgent.os, ignoreCase = true)) {
                                ""
                            } else {
                                "${parsedAgent.browser} ${parsedAgent.browserVersion}"
                            }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = colorScheme.primary,
                                contentColor = colorScheme.onPrimary
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = deviceTypeIcon(session, parsedAgent),
                                        contentDescription = null,
                                        modifier = Modifier.size(40.dp),
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
//                                        Text(
//                                            text = session.deviceBrand ?: session.clientName
//                                            ?: "Устройство",
//                                            style = MaterialTheme.typography.titleSmall
//                                        )
                                        Text(
                                            text = "${session.osName ?: "OS"} ${session.osVersion ?: ""}",
                                            style = MaterialTheme.typography.bodySmall,
                                        )
                                    }

                                    if (isCurrent) {
                                        Surface(
                                            shape = MaterialTheme.shapes.small,
                                            color = colorScheme.primary,
                                            tonalElevation = 2.dp,
                                            modifier = Modifier
                                                .padding(start = 8.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(
                                                    horizontal = 8.dp,
                                                    vertical = 4.dp
                                                ),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Person,
                                                    contentDescription = "Вы",
                                                    tint = colorScheme.onPrimary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    "Вы",
                                                    color = colorScheme.onPrimary,
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .height(2.dp)
                                        .background(colorScheme.onPrimary)
                                )

                                if (browserOsText.isNotEmpty()) {
                                    Text(
                                        text = browserOsText,
                                        style = typography.bodySmall,
                                    )
                                }

                                Text(
                                    text = buildString {
                                        append("Устройство: ${parsedAgent.device}")
//                                            parsedAgent.deviceBrand?.let { append(" • Бренд: $it") }
//                                            parsedAgent.deviceModel?.let { append(" • Модель: $it") }
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    "IP: ${session.ipAddress ?: "Не указано"}",
                                    style = typography.bodySmall,
                                )
                                Text(
                                    "Первый вход: ${formatDateTimeReadable(session.createdAt)}",
                                    style = typography.bodySmall,
                                )
                                Text(
                                    "Последний вход: ${formatDateTimeReadable(session.lastLoginAt)}",
                                    style = typography.bodySmall,
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val statusBg =
                                        if (session.isActive) SuccessColor else colorScheme.error
                                    val statusTextColor =
                                        if (session.isActive) colorScheme.onPrimary else colorScheme.onPrimary

                                    Surface(
                                        shape = MaterialTheme.shapes.small,
                                        color = statusBg,
                                    ) {
                                        Text(
                                            if (session.isActive) "Активна" else "Не активна",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = statusTextColor,
                                            modifier = Modifier.padding(
                                                horizontal = 8.dp,
                                                vertical = 4.dp
                                            )
                                        )
                                    }

                                    if (sessions.isNotEmpty()) {
                                        AppButton(
                                            onClick = {
                                                authStateViewModel.logoutFromDevice(session.deviceId) { success ->
                                                    if (success) {
                                                        userProfileViewModel.markSessionInactive(
                                                            session.deviceId
                                                        )
                                                    }
                                                }
                                            },
                                            enabled = session.isActive && (session.deviceId != currentDeviceId),
                                            text = "Выйти",
                                            tone = AppButtonTone.Danger,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            AppButton(
                variant = AppButtonVariant.Text,
                onClick = { onDismiss() },
                text = "Закрыть"
            )
        }
    )

    if (showAllDevicesLogoutModalOpen) {
        LogoutAllDevicesModal(
            onDismiss = { showAllDevicesLogoutModalOpen = false },
            authStateViewModel = authStateViewModel
        )
    }
}

@Composable
private fun deviceTypeIcon(session: UserSession, parsed: UaParser.ParsedAgent): ImageVector {
    val typeHint = (session.deviceType ?: parsed.device).lowercase()
    return when {
        "tablet" in typeHint -> Icons.Default.TabletAndroid
        "android" in typeHint || "mobile" in typeHint || "iphone" in typeHint || "phone" in typeHint -> Icons.Default.PhoneAndroid
        "windows" in typeHint || "mac" in typeHint || "linux" in typeHint || "desktop" in typeHint -> Icons.Default.Computer
        else -> Icons.Default.Computer
    }
}
