package com.example.sharkflow.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.*

@Composable
fun AppSwipeRefreshOGO(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val swipeState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    SwipeRefresh(
        state = swipeState,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize(),
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = 80.dp,
                contentColor = MaterialTheme.colorScheme.primary,
                backgroundColor = MaterialTheme.colorScheme.background
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
        ) {
            content()
        }
    }
}


@Composable
fun AppSwipeRefresh(
    state: SwipeRefreshState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    SwipeRefresh(
        state = state,
        onRefresh = onRefresh,
        modifier = modifier.fillMaxSize(),
        indicator = { _, _ -> }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}
