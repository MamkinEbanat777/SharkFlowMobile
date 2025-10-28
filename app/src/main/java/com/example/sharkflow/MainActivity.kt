package com.example.sharkflow

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.sharkflow.core.presentation.requestNotificationPermissionIfNeeded
import com.example.sharkflow.core.system.*
import com.example.sharkflow.presentation.navigation.AppNavHost
import com.example.sharkflow.presentation.screens.auth.viewmodel.AuthStateViewModel
import com.example.sharkflow.presentation.screens.common.SplashScreen
import com.example.sharkflow.presentation.screens.profile.viewmodel.UserProfileViewModel
import com.example.sharkflow.presentation.theme.SharkFlowTheme
import com.example.sharkflow.viewmodel.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.let {
            IntentBus.flow.tryEmit(it)
            AppLog.d("MainActivity", "onCreate: emitted initial intent extras=${it.extras}")
        }

        requestNotificationPermissionIfNeeded { granted ->
            if (granted) {
                AppLog.d("NotificationPermission", "granted")
            } else {
                AppLog.d("NotificationPermission", "denied")
            }
        }
        setContent {
            val authStateViewModel: AuthStateViewModel = hiltViewModel()
            val userProfileViewModel: UserProfileViewModel = hiltViewModel()
            val themeViewModel: ThemeViewModel = hiltViewModel()

            val userIsLoading by userProfileViewModel.isUserLoading.collectAsState(initial = false)
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

            val appViewModel: AppViewModel = hiltViewModel()

            val isLoading by remember {
                derivedStateOf {
                    !appViewModel.isLanguageInitialized || userIsLoading
                }
            }

            LaunchedEffect(Unit) {
                appViewModel.initializeApp()
            }

            SharkFlowTheme(darkTheme = isDarkTheme) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (isLoading) {
                        SplashScreen(
                            text = "Пожалуйста, подождите...",
                            onFinish = { /* ничего */ },
                            showSpinner = true
                        )
                    } else {
                        AppNavHost(
                            authStateViewModel = authStateViewModel,
                            userProfileViewModel = userProfileViewModel,
                            isDarkTheme = isDarkTheme,
                            onThemeChange = { newTheme ->
                                themeViewModel.setTheme(newTheme)
                            }
                        )
                    }
                }
            }
        }

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.let {
            setIntent(it)
            IntentBus.flow.tryEmit(it)
            AppLog.d("MainActivity", "onNewIntent: emitted intent extras=${it.extras}")
        }
    }
}

