package com.example.sharkflow.presentation.screens.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.sharkflow.presentation.screens.auth.components.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegisterScreen(
    navController: NavController,
) {
    var step by remember { mutableIntStateOf(1) }
    val stepsCount = 3

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(28.dp))

        StepDotsIndicator(
            current = step,
            total = stepsCount,
            modifier = Modifier.fillMaxWidth(),
            indicatorWidth = 10.dp,
            indicatorSelectedWidth = 28.dp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    val forward = targetState > initialState
                    if (forward) {
                        slideInHorizontally(
                            animationSpec = tween(360),
                            initialOffsetX = { fullWidth -> fullWidth }
                        ) + fadeIn(animationSpec = tween(260)) togetherWith
                                slideOutHorizontally(
                                    animationSpec = tween(300),
                                    targetOffsetX = { fullWidth -> -fullWidth / 2 }
                                ) + fadeOut(animationSpec = tween(260))
                    } else {
                        slideInHorizontally(
                            animationSpec = tween(360),
                            initialOffsetX = { fullWidth -> -fullWidth }
                        ) + fadeIn(animationSpec = tween(260)) togetherWith
                                slideOutHorizontally(
                                    animationSpec = tween(300),
                                    targetOffsetX = { fullWidth -> fullWidth / 2 }
                                ) + fadeOut(animationSpec = tween(260))
                    }.using(
                        SizeTransform(clip = false)
                    )
                },
                label = "RegisterStepsAnimation"
            ) { currentStep ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    when (currentStep) {
                        1 -> RegisterForm(
                            onNext = { step = 2 },
                            navController = navController
                        )

                        2 -> CodeConfirmation(
                            onNext = { step = 3 },
                            onBack = { step = 1 }
                        )

                        3 -> RegistrationSuccess()
                    }
                }
            }
        }
    }

    if (step == 3) {
        LaunchedEffect(Unit) {
            delay(3000)
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
            delay(1000)
            step = 1
        }
    }
}

@Composable
private fun StepDotsIndicator(
    current: Int,
    total: Int,
    modifier: Modifier = Modifier,
    indicatorWidth: Dp = 8.dp,
    indicatorSelectedWidth: Dp = 24.dp,
    spacing: Dp = 8.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..total) {
            val selected = i == current
            val width by animateDpAsState(targetValue = if (selected) indicatorSelectedWidth else indicatorWidth)
            val alpha by animateFloatAsState(targetValue = if (selected) 1f else 0.45f)

            Box(
                modifier = Modifier
                    .padding(horizontal = spacing / 2)
                    .height(indicatorWidth)
                    .width(width)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = alpha))
            )
        }
    }
}
