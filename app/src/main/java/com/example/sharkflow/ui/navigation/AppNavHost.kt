package com.example.sharkflow.ui.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.hilt.lifecycle.viewmodel.compose.*
import androidx.navigation.compose.*
import com.example.sharkflow.ui.components.*
import com.example.sharkflow.ui.screens.*
import com.example.sharkflow.ui.screens.auth.*
import com.example.sharkflow.ui.screens.marketing.*
import com.example.sharkflow.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val authStateViewModel: AuthStateViewModel = hiltViewModel()

    val navController = rememberNavController()
    val isLoggedIn = authStateViewModel.isLoggedIn
    val currentUser = authStateViewModel.currentUser

    val bottomNavItems = if (isLoggedIn) userBottomNavItems else publicBottomNavItems
    val startDestination = if (isLoggedIn) "dashboard" else "hero"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "SharkFlow",
                            color = colorScheme.onPrimary,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            onThemeChange(!isDarkTheme)
                        }) {
                            Crossfade(targetState = isDarkTheme) { darkMode ->
                                Icon(
                                    if (darkMode) {
                                        Icons.Filled.DarkMode
                                    } else {
                                        Icons.Filled.LightMode
                                    },
                                    contentDescription = if (darkMode) {
                                        "Темная тема"
                                    } else {
                                        "Светлая тема"
                                    },
                                    modifier = Modifier.animateContentSize()
                                )
                            }
                        }
                    }
                },
                modifier = Modifier.background(colorScheme.primary),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primary,
                    titleContentColor = colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            BottomNavBar(
                navController,
                bottomNavItems,
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            composable("hero") { HeroScreen(navController) }
            composable("how_it_works") { HowItWorksScreen() }
            composable("features") { FeaturesScreen() }
            composable("advantages") { AdvantagesScreen() }
            composable("about") { AboutScreen() }
            composable("security") { SecurityScreen() }
            composable("faq") { FAQScreen() }
            composable("support") { SupportScreen() }
            composable("login") { LoginScreen(navController) }
            composable("register") { RegisterScreen(navController = navController) }
            composable("dashboard") { DashboardScreen() }
            composable("profile") { ProfileScreen(currentUser) }
        }
    }
}
