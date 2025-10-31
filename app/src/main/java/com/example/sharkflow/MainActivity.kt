package com.example.sharkflow

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.sharkflow.core.system.IntentBus
import com.example.sharkflow.presentation.navigation.AppNavHost
import com.example.sharkflow.presentation.screens.auth.viewmodel.AuthStateViewModel
import com.example.sharkflow.presentation.screens.board.viewmodel.BoardsViewModel
import com.example.sharkflow.presentation.screens.splash.SplashScreen
import com.example.sharkflow.presentation.screens.task.viewmodel.*
import com.example.sharkflow.presentation.screens.user.viewmodel.UserProfileViewModel
import com.example.sharkflow.presentation.theme.SharkFlowTheme
import com.example.sharkflow.viewmodel.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.let {
            IntentBus.flow.tryEmit(it)
        }

        setContent {
            val authStateViewModel: AuthStateViewModel = hiltViewModel()
            val userProfileViewModel: UserProfileViewModel = hiltViewModel()
            val tasksViewModel: TasksViewModel = hiltViewModel()
            val boardsViewModel: BoardsViewModel = hiltViewModel()
            val taskDetailViewModel: TaskDetailViewModel = hiltViewModel()
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val appViewModel: AppViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                userProfileViewModel.loadUser()
            }

            val userIsLoading by userProfileViewModel.isUserLoading.collectAsState(initial = false)
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

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
                            tasksViewModel = tasksViewModel,
                            taskDetailViewModel = taskDetailViewModel,
                            boardsViewModel = boardsViewModel,
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
        }
    }
}

