package com.mnessim.researchtrackerkmp.presentation.core

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.mnessim.researchtrackerkmp.domain.services.ApiService
import io.ktor.client.HttpClient
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    colorScheme: ColorScheme,
    canPop: Boolean,
    onNavigate: () -> Unit,
    onChangeColorScheme: () -> Unit,
    onNotificationButton: () -> Unit
) {
    val client = koinInject<HttpClient>()
    val apiService = ApiService(client)
    var status by remember { mutableStateOf(HttpStatusCode.InternalServerError) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Intervals (ms)
        val healthyInterval = 5 * 60_000L       // 5 minutes
        val unhealthyBase = 500L                // half second when unhealthy
        val maxBackoff = 5 * 60_000L            // cap backoff at 5 minutes
        var currentInterval = unhealthyBase
        var consecutiveFailures = 0

        while (isActive) {
            try {
                val result = apiService.checkHealth()
                status = result

                if (result == HttpStatusCode.OK) {
                    currentInterval = healthyInterval
                    consecutiveFailures = 0
                } else {
                    consecutiveFailures++
                    currentInterval = (unhealthyBase * (1L shl (consecutiveFailures - 1)))
                        .coerceAtMost(maxBackoff)
                }
            } catch (t: Throwable) {
                consecutiveFailures++
                currentInterval = (unhealthyBase * (1L shl (consecutiveFailures - 1)))
                    .coerceAtMost(maxBackoff)
                status = HttpStatusCode.InternalServerError
            }

            val jitter = (currentInterval * 0.1).toLong().coerceAtLeast(0L)
            val delayMs = currentInterval + Random.nextLong(-jitter, jitter + 1)
            delay(delayMs)
        }
    }

    TopAppBar(
        colors = TopAppBarColors(
            containerColor = colorScheme.surfaceContainer,
            scrolledContainerColor = colorScheme.surfaceContainer,
            navigationIconContentColor = colorScheme.onSurface,
            titleContentColor = colorScheme.onSurface,
            actionIconContentColor = colorScheme.onSurface
        ), // colors =
        title = { Text("Research Tracker") },
        navigationIcon = if (canPop) {
            {
                IconButton(onClick = onNavigate) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        } else {
            {}
        }, // navigationIcon =
        actions = {
            Surface(
                color = if (status == HttpStatusCode.OK) Color.Green else Color.Red,
                shape = CircleShape,
                onClick = {
                    status = HttpStatusCode.Processing
                    scope.launch {
                        status = apiService.checkHealth()
                    }
                }
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .size(10.dp)
                )
            }
            IconButton(onClick = onNotificationButton) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Test Notifications"
                ) // Icon
            } // IconButton
            Surface(
                modifier = Modifier
                    .padding(horizontal = 18.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                onChangeColorScheme()
                            },
                        )
                    },
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.InvertColors,
                    contentDescription = "Toggle Dark Mode"
                ) // Icon
            } // Surface
        }
    )
}

