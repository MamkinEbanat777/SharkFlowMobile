package com.example.sharkflow

import android.os.*
import androidx.activity.*
import androidx.activity.compose.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import com.example.sharkflow.data.local.*
import com.example.sharkflow.ui.navigation.*
import com.example.sharkflow.ui.theme.*
import com.example.sharkflow.viewmodel.*
import dagger.hilt.android.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authStateViewModel: AuthStateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current.applicationContext

            var isDarkTheme by remember {
                mutableStateOf(getThemePreference(context))
            }

            LaunchedEffect(isDarkTheme) {
                setThemePreference(context, isDarkTheme)
            }

            LaunchedEffect(Unit) {
                authStateViewModel.initialLoad(context)
            }

            SharkFlowTheme(darkTheme = isDarkTheme) {
                AppNavHost(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { newTheme ->
                        isDarkTheme = newTheme
                    }
                )
            }
        }
    }
}

