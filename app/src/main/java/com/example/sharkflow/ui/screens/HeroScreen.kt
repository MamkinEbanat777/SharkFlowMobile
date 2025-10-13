package com.example.sharkflow.ui.screens

import android.annotation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.hilt.lifecycle.viewmodel.compose.*
import androidx.navigation.*
import com.example.sharkflow.ui.components.*
import com.example.sharkflow.viewmodel.*

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun HeroScreen(
    navController: NavController
) {
    val authStateViewModel: AuthStateViewModel = hiltViewModel()

    val isLoggedIn by remember { derivedStateOf { authStateViewModel.isLoggedIn } }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "SharkFlow",
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                color = colorScheme.primary,
            )
            Text(
                text = "управляй задачами как акула",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                listOf("Нацелься", "Планируй", "Действуй").forEach { step ->
                    Text(step, style = MaterialTheme.typography.bodyMedium, fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            AppButton(
                onClick = {
                    if (isLoggedIn) {
                        navController.navigate("dashboard") {
                            popUpTo("hero") { inclusive = true }
                        }
                    } else {
                        navController.navigate("login") {
                            popUpTo("hero") { inclusive = true }
                        }
                    }
                },
                variant = AppButtonVariant.Primary,
                text = "Начать охоту"
            )
        }
    }
}
