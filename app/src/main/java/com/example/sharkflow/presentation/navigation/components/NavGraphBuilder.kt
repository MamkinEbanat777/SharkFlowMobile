package com.example.sharkflow.presentation.navigation.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.*
import com.example.sharkflow.core.presentation.*
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.animatedComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    navOrder: List<String>,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = {
            val fromRoute = initialState.destination.route?.substringBefore('/') ?: ""
            val toRoute = targetState.destination.route?.substringBefore('/') ?: ""
            if (navOrder.indexOf(toRoute) > navOrder.indexOf(fromRoute)) slideInFromRight()
            else slideInFromLeft()
        },
        exitTransition = {
            val fromRoute = initialState.destination.route?.substringBefore('/') ?: ""
            val toRoute = targetState.destination.route?.substringBefore('/') ?: ""
            if (navOrder.indexOf(toRoute) > navOrder.indexOf(fromRoute)) slideOutToLeft()
            else slideOutToRight()
        },
        popEnterTransition = {
            val fromRoute = initialState.destination.route?.substringBefore('/') ?: ""
            val toRoute = targetState.destination.route?.substringBefore('/') ?: ""
            if (navOrder.indexOf(toRoute) > navOrder.indexOf(fromRoute)) slideInFromRight()
            else slideInFromLeft()
        },
        popExitTransition = {
            val fromRoute = initialState.destination.route?.substringBefore('/') ?: ""
            val toRoute = targetState.destination.route?.substringBefore('/') ?: ""
            if (navOrder.indexOf(toRoute) > navOrder.indexOf(fromRoute)) slideOutToLeft()
            else slideOutToRight()
        }
    ) { backStackEntry ->
        content(backStackEntry)
    }
}
