package com.example.sharkflow.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.sharkflow.R

sealed class NavScreen(val route: String, val label: Int, val icon: ImageVector) {
    object Hero : NavScreen("hero", R.string.nav_hero, Icons.Filled.Home)
    object Login : NavScreen("login", R.string.nav_login, Icons.AutoMirrored.Filled.Login)
    object Register : NavScreen("register", R.string.nav_register, Icons.Filled.Key)
    object HowItWorks :
        NavScreen("how_it_works", R.string.nav_how_it_works, Icons.AutoMirrored.Filled.Help)

    object Features : NavScreen("features", R.string.nav_features, Icons.AutoMirrored.Filled.List)
    object Advantages : NavScreen("advantages", R.string.nav_advantages, Icons.Filled.ThumbUp)
    object About : NavScreen("about", R.string.nav_about, Icons.Filled.Info)
    object Security : NavScreen("security", R.string.nav_security, Icons.Filled.Lock)
    object FAQ : NavScreen("faq", R.string.nav_faq, Icons.Filled.QuestionMark)
    object Support : NavScreen("support", R.string.nav_support, Icons.Filled.SupportAgent)
    object Dashboard : NavScreen("dashboard", R.string.nav_dashboard, Icons.Filled.CheckBox)
    object Profile : NavScreen("profile", R.string.nav_profile, Icons.Filled.AccountCircle)
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
