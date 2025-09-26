package com.example.sharkflow.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavScreen(val route: String, val label: String, val icon: ImageVector) {
    object Hero : NavScreen("hero", "Главная", Icons.Filled.Home)
    object Features : NavScreen("features", "Особенности", Icons.AutoMirrored.Filled.List)
    object Advantages : NavScreen("advantages", "Преимущества", Icons.Filled.ThumbUp)
    object FAQ : NavScreen("faq", "ЧАВО", Icons.Filled.QuestionMark)
}

val bottomNavItems = listOf(
    NavScreen.Hero,
    NavScreen.Features,
    NavScreen.Advantages,
    NavScreen.FAQ,
)
