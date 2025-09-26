package com.example.sharkflow.navigation

import NavigationBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sharkflow.R
import com.example.sharkflow.ui.screens.AboutScreen
import com.example.sharkflow.ui.screens.AdvantagesScreen
import com.example.sharkflow.ui.screens.FAQScreen
import com.example.sharkflow.ui.screens.FeaturesScreen
import com.example.sharkflow.ui.screens.HeroScreen
import com.example.sharkflow.ui.screens.HowItWorksScreen
import com.example.sharkflow.ui.screens.SecurityScreen
import com.example.sharkflow.ui.screens.SupportScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    // TODO: попробовать сделать нижнюю панель скроллируемой
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .background(Color.Blue)
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight()
                    .drawBehind {
                        val strokeWidth = 2.dp.toPx()
                        drawLine(
                            color = Color.White,
                            start = androidx.compose.ui.geometry.Offset(
                                size.width - strokeWidth / 2,
                                0f
                            ),
                            end = androidx.compose.ui.geometry.Offset(
                                size.width - strokeWidth / 2,
                                size.height
                            ),
                            strokeWidth = strokeWidth
                        )
                    }
            ) {

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Меню",
                        fontSize = 46.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 30.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(top = 60.dp))

                val drawerItems = listOf(
                    "login" to "Логин",
                    "register" to "Регистрация",
                    "how_it_works" to "Как это работает",
                    "security" to "Безопасность",
                    "about" to "О нас",
                    "faq" to "ЧАВО"
                )

                drawerItems.forEach { (route, label) ->
                    NavigationDrawerItem(
                        label = { Text(label, fontSize = 18.sp) },
                        selected = navController.currentBackStackEntryAsState().value?.destination?.route == route,
                        onClick = {
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            scope.launch { drawerState.close() }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = Color.Blue,
                            selectedTextColor = Color.White,
                            unselectedContainerColor = Color.White,
                            unselectedTextColor = Color.Blue
                        ),
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 10.dp)
                            .fillMaxWidth(0.8f)


                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                "SharkFlow",
                                color = Color.White,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Logo",
                            )
                        }
                    },
                    modifier = Modifier.background(
                        Color.Blue
                    ),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Меню")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Blue,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    navController = navController,
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "hero",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("hero") { HeroScreen(onStartClick = { navController.navigate("login") }) }
                composable("how_it_works") { HowItWorksScreen() }
                composable("features") { FeaturesScreen() }
                composable("advantages") { AdvantagesScreen() }
                composable("about") { AboutScreen() }
                composable("security") { SecurityScreen() }
                composable("faq") { FAQScreen() }
                composable("support") { SupportScreen() }
            }
        }
    }
}
