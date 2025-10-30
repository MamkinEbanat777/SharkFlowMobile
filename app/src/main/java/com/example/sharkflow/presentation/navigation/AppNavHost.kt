package com.example.sharkflow.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import androidx.navigation.compose.rememberNavController
import com.example.sharkflow.R
import com.example.sharkflow.core.common.Lang
import com.example.sharkflow.core.system.*
import com.example.sharkflow.presentation.navigation.components.*
import com.example.sharkflow.presentation.screens.auth.*
import com.example.sharkflow.presentation.screens.auth.viewmodel.AuthStateViewModel
import com.example.sharkflow.presentation.screens.board.*
import com.example.sharkflow.presentation.screens.board.viewmodel.BoardsViewModel
import com.example.sharkflow.presentation.screens.hero.HeroScreen
import com.example.sharkflow.presentation.screens.marketing.*
import com.example.sharkflow.presentation.screens.profile.ProfileScreen
import com.example.sharkflow.presentation.screens.profile.viewmodel.UserProfileViewModel
import com.example.sharkflow.presentation.screens.support.SupportScreen
import com.example.sharkflow.presentation.screens.task.TaskDetailScreen
import com.example.sharkflow.presentation.screens.task.viewmodel.*
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(
    authStateViewModel: AuthStateViewModel,
    userProfileViewModel: UserProfileViewModel,
    tasksViewModel: TasksViewModel,
    taskDetailViewModel: TaskDetailViewModel,
    boardsViewModel: BoardsViewModel,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()

    val isLoggedIn by authStateViewModel.isLoggedIn.collectAsState()
    val bottomNavItems = if (isLoggedIn) userBottomNavItems else publicBottomNavItems
    val startDestination = if (isLoggedIn) "boards" else "hero"

    val publicOrder = listOf(
        "hero", "login", "register", "how-it-works", "features", "advantages",
        "about", "security", "faq", "support"
    )

    val userOrder = listOf(
        "profile", "boards", "task", "support"
    )

    val navOrder = if (isLoggedIn) userOrder else publicOrder

    LaunchedEffect(Unit) {
        IntentBus.flow.collect { intent ->
            try {
                val navigate = intent.getBooleanExtra("navigate_to_task", false)
                if (navigate) {
                    val boardUuid = intent.getStringExtra("board_uuid")
                    val taskUuid = intent.getStringExtra("task_uuid")
                    AppLog.d(
                        "AppNavHost",
                        "IntentBus received navigate_to_task board=$boardUuid task=$taskUuid"
                    )
                    if (!boardUuid.isNullOrBlank() && !taskUuid.isNullOrBlank()) {
                        navController.navigate("task/$boardUuid/$taskUuid") {
                            launchSingleTop = true
                        }
                    }
                }
            } catch (t: Throwable) {
                AppLog.e("AppNavHost", "Failed to handle intent", t)
            }
        }
    }

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
        AnimatedNavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            animatedComposable("hero", navOrder = navOrder) {
                HeroScreen(
                    navController,
                    authStateViewModel
                )
            }
            animatedComposable("how-it-works", navOrder = navOrder) { HowItWorksScreen() }
            animatedComposable("features", navOrder = navOrder) { FeaturesScreen() }
            animatedComposable("advantages", navOrder = navOrder) { AdvantagesScreen() }
            animatedComposable("about", navOrder = navOrder) { AboutScreen() }
            animatedComposable("security", navOrder = navOrder) { SecurityScreen() }
            animatedComposable("faq", navOrder = navOrder) { FAQScreen() }
            animatedComposable("support", navOrder = navOrder) { SupportScreen() }
            animatedComposable("login", navOrder = navOrder) { LoginScreen(navController) }
            animatedComposable(
                "register",
                navOrder = navOrder
            ) { RegisterScreen(navController = navController) }
            animatedComposable("boards", navOrder = navOrder) {
                BoardsScreen(
                    navController,
                    boardsViewModel
                )
            }
            animatedComposable(
                "board/{boardUuid}",
                navOrder = navOrder,
                arguments = listOf(navArgument("boardUuid") { type = NavType.StringType })
            ) { backStackEntry ->
                val boardUuid = backStackEntry.arguments?.getString("boardUuid") ?: ""
                BoardDetailScreen(navController, boardUuid, boardsViewModel, tasksViewModel)
            }
            animatedComposable(
                route = "task/{boardUuid}/{taskUuid}",
                navOrder = navOrder,
                arguments = listOf(
                    navArgument("boardUuid") { type = NavType.StringType },
                    navArgument("taskUuid") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val boardUuid = backStackEntry.arguments?.getString("boardUuid") ?: ""
                val taskUuid = backStackEntry.arguments?.getString("taskUuid") ?: ""
                TaskDetailScreen(
                    navController = navController,
                    boardUuid = boardUuid,
                    taskUuid = taskUuid,
                    tasksViewModel = tasksViewModel,
                    taskDetailViewModel = taskDetailViewModel
                )
            }
            animatedComposable("profile", navOrder = navOrder) {
                ProfileScreen(
                    userProfileViewModel = userProfileViewModel,
                    navController = navController,
                    authStateViewModel = authStateViewModel
                )
            }
        }
    }
}
