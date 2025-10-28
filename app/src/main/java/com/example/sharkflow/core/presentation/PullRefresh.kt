package com.example.sharkflow.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.*

@Composable
fun <T> PullRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    items: List<T>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit
) {
    val swipeState = rememberSwipeRefreshState(isRefreshing)
    SwipeRefresh(
        state = swipeState,
        onRefresh = onRefresh,
        indicator = { s, trigger ->
            SwipeRefreshIndicator(
                state = s,
                refreshTriggerDistance = trigger,
                contentColor = MaterialTheme.colorScheme.primary,
                backgroundColor = MaterialTheme.colorScheme.background
            )
        },
        modifier = modifier
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items) { item -> itemContent(item) }
        }
    }
}
