package com.example.sharkflow.presentation.screens.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sharkflow.R
import com.example.sharkflow.presentation.common.*
import com.example.sharkflow.presentation.screens.auth.viewmodel.AuthStateViewModel
import com.example.sharkflow.utils.Lang

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun HeroScreen(
    navController: NavController
) {
    val authStateViewModel: AuthStateViewModel = hiltViewModel()
    val isLoggedIn by remember { derivedStateOf { authStateViewModel.isLoggedIn.value } }
    val steps = Lang.stringArray(R.array.hero_array_step)

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
                text = Lang.string(R.string.hero_title),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                color = colorScheme.primary,
            )

            Text(
                text = Lang.string(R.string.hero_subtitle),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                steps.forEach { step ->
                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            AppButton(
                onClick = {
                    val target = if (isLoggedIn) "dashboard" else "login"
                    navController.navigate(target) {
                        popUpTo("hero") { inclusive = true }
                    }
                },
                variant = AppButtonVariant.Primary,
                text = Lang.string(R.string.hero_button)
            )
        }
    }
}
