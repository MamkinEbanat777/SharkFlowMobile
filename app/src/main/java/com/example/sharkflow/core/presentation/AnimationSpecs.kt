package com.example.sharkflow.core.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween

@OptIn(ExperimentalAnimationApi::class)
fun slideInFromRight(): EnterTransition =
    slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(300)
    ) + fadeIn(animationSpec = tween(300))

@OptIn(ExperimentalAnimationApi::class)
fun slideOutToLeft(): ExitTransition =
    slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300)) + fadeOut(
        animationSpec = tween(300)
    )

@OptIn(ExperimentalAnimationApi::class)
fun slideInFromLeft(): EnterTransition =
    slideInHorizontally(
        initialOffsetX = { -it },
        animationSpec = tween(300)
    ) + fadeIn(animationSpec = tween(300))

@OptIn(ExperimentalAnimationApi::class)
fun slideOutToRight(): ExitTransition =
    slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(300)
    ) + fadeOut(animationSpec = tween(300))
