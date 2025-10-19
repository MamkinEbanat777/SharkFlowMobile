package com.example.sharkflow.presentation.navigation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sharkflow.presentation.navigation.NavScreen
import com.example.sharkflow.presentation.screens.auth.viewmodel.AuthStateViewModel
import com.example.sharkflow.utils.Lang

@Composable
fun BottomNavBar(
    navController: NavHostController,
    items: List<NavScreen>,
    authStateViewModel: AuthStateViewModel
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val selectedIndex = items.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)
    val isLoggedIn by authStateViewModel.isLoggedIn.collectAsState()

    Surface(color = colorScheme.primary, tonalElevation = 8.dp) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            PrimaryScrollableTabRow(
                selectedTabIndex = selectedIndex,
                containerColor = colorScheme.primary,
                edgePadding = 0.dp,
                indicator = {
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(selectedIndex, matchContentSize = true)
                            .height(3.dp)
                            .padding(horizontal = 12.dp)
                            .background(colorScheme.primary, RoundedCornerShape(2.dp))
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                items.forEachIndexed { index, screen ->
                    val selected = selectedIndex == index

                    val bgColor by animateColorAsState(
                        targetValue = if (selected) colorScheme.background else colorScheme.primary,
                        animationSpec = tween(durationMillis = 250),
                        label = "TabBgAnim"
                    )

                    val iconColor by animateColorAsState(
                        targetValue = if (selected) colorScheme.primary else colorScheme.onPrimary,
                        animationSpec = tween(durationMillis = 250),
                        label = "IconColorAnim"
                    )

                    val textColor by animateColorAsState(
                        targetValue = if (selected) colorScheme.primary else colorScheme.onPrimary,
                        animationSpec = tween(durationMillis = 250),
                        label = "TextColorAnim"
                    )

                    Tab(
                        selected = selected,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        selectedContentColor = colorScheme.primary,
                        unselectedContentColor = colorScheme.background
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .background(bgColor)
                                .padding(top = 6.dp)
                                .width(110.dp)
                        ) {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = Lang.string(screen.label),
                                tint = iconColor,
                                modifier = Modifier.size(26.dp)
                            )
                            Text(
                                text = Lang.string(screen.label),
                                color = textColor,
                                fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }
                if (isLoggedIn) {
                    LogoutButton(navController)
                }
            }
        }
    }
}