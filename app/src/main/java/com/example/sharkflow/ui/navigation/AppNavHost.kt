package com.example.sharkflow.ui.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.example.sharkflow.R
import com.example.sharkflow.ui.navigation.components.*
import com.example.sharkflow.ui.screens.auth.*
import com.example.sharkflow.ui.screens.auth.viewmodel.AuthStateViewModel
import com.example.sharkflow.ui.screens.common.*
import com.example.sharkflow.ui.screens.dashboard.DashboardScreen
import com.example.sharkflow.ui.screens.marketing.*
import com.example.sharkflow.ui.screens.profile.ProfileScreen
import com.example.sharkflow.ui.screens.profile.viewmodel.UserProfileViewModel
import com.example.sharkflow.utils.Lang

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    authStateViewModel: AuthStateViewModel,
    userProfileViewModel: UserProfileViewModel,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val isLoggedIn by authStateViewModel.isLoggedIn.collectAsState()
    val bottomNavItems = if (isLoggedIn) userBottomNavItems else publicBottomNavItems
    val startDestination = if (isLoggedIn) "dashboard" else "hero"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = Lang.string(R.string.common_app_name),
                        color = colorScheme.onPrimary,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    IconButton(onClick = { onThemeChange(!isDarkTheme) }) {
                        Crossfade(targetState = isDarkTheme) { darkMode ->
                            Icon(
                                imageVector = if (darkMode) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                                contentDescription = if (darkMode)
                                    Lang.string(R.string.common_dark_mode)
                                else
                                    Lang.string(R.string.common_light_mode),
                                modifier = Modifier.animateContentSize()
                            )
                        }
                    }
                    LanguageButton()
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primary,
                    titleContentColor = colorScheme.onPrimary,
                    actionIconContentColor = colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            BottomNavBar(
                navController,
                bottomNavItems,
                authStateViewModel
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
            composable("profile") {
                ProfileScreen(
                    userProfileViewModel = userProfileViewModel,
                    navController = navController,
                )
            }
        }
    }
}
