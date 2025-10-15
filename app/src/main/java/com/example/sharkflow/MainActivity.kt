package com.example.sharkflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.sharkflow.data.local.ThemePreference
import com.example.sharkflow.data.local.language.LanguageState
import com.example.sharkflow.ui.navigation.AppNavHost
import com.example.sharkflow.ui.theme.SharkFlowTheme
import com.example.sharkflow.viewmodel.AuthStateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current.applicationContext ?: return@setContent

            var isDarkTheme by remember {
                mutableStateOf(ThemePreference.get(context))
            }

            val authStateViewModel: AuthStateViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                LanguageState.init(context)
            }

            LaunchedEffect(Unit) {
                authStateViewModel.initialLoad(context)
            }

            SharkFlowTheme(darkTheme = isDarkTheme) {
                AppNavHost(
                    authStateViewModel = authStateViewModel,
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

