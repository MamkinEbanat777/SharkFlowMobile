package com.example.sharkflow

import SplashScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.sharkflow.data.local.ThemePreference
import com.example.sharkflow.ui.navigation.AppNavHost
import com.example.sharkflow.ui.screens.auth.viewmodel.AuthStateViewModel
import com.example.sharkflow.ui.screens.profile.viewmodel.UserProfileViewModel
import com.example.sharkflow.ui.theme.SharkFlowTheme
import com.example.sharkflow.viewmodel.AppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current.applicationContext ?: return@setContent
            var isDarkTheme by remember { mutableStateOf(ThemePreference.get(context)) }

            val appViewModel: AppViewModel = hiltViewModel()
            val authStateViewModel: AuthStateViewModel = hiltViewModel()
            val userProfileViewModel: UserProfileViewModel = hiltViewModel()

            val isLoading by remember {
                derivedStateOf {
                    userProfileViewModel.isLoading ||
                            !appViewModel.isLanguageInitialized
                }
            }

            LaunchedEffect(Unit) {
                appViewModel.initializeApp(context)
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
                                isDarkTheme = newTheme
                                ThemePreference.set(context, newTheme)
                            }
                        )
                    }
                }
            }

        }
    }
}