package com.example.sharkflow.ui.navigation

import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.*

sealed class NavScreen(val route: String, val label: String, val icon: ImageVector) {
    object Hero : NavScreen("hero", "Главная", Icons.Filled.Home)
    object Login : NavScreen("login", "Вход", Icons.AutoMirrored.Filled.Login)
    object Register : NavScreen("register", "Регистрация", Icons.Filled.Key)
    object HowItWorks :
        NavScreen("how_it_works", "Как это работает", Icons.AutoMirrored.Filled.Help)

    object Features : NavScreen("features", "Особенности", Icons.AutoMirrored.Filled.List)
    object Advantages : NavScreen("advantages", "Преимущества", Icons.Filled.ThumbUp)
    object About : NavScreen("about", "О нас", Icons.Filled.Info)
    object Security : NavScreen("security", "Безопасность", Icons.Filled.Lock)
    object FAQ : NavScreen("faq", "ЧАВО", Icons.Filled.QuestionMark)
    object Support : NavScreen("support", "Поддержка", Icons.Filled.SupportAgent)
    object Dashboard : NavScreen("dashboard", "Мои доски", Icons.Filled.CheckBox)
    object Profile : NavScreen("profile", "Профиль", Icons.Filled.AccountCircle)
}

val publicBottomNavItems = listOf(
    NavScreen.Hero,
    NavScreen.Login,
    NavScreen.Register,
    NavScreen.HowItWorks,
    NavScreen.Features,
    NavScreen.Advantages,
    NavScreen.About,
    NavScreen.Security,
    NavScreen.FAQ,
    NavScreen.Support,
)

val userBottomNavItems = listOf(
    //NavScreen.Hero,
    NavScreen.Profile,
    NavScreen.Dashboard,
    //NavScreen.FAQ,
    NavScreen.Support,
)
