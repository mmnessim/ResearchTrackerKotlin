package com.mnessim.rsstracker.presentation.screens.optionsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FeedSelector(
    modifier: Modifier = Modifier,
    feeds: List<String>,
    blockedFeeds: List<String> = emptyList(),
    toggleBlock: (String) -> Unit = {},
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            ),
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                text = "Block results from specific sources",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = modifier.height(500.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = feeds,
                    key = { it -> it }
                ) {
                    val blocked = blockedFeeds.contains(it)

                    Surface(
                        modifier = Modifier.fillMaxWidth()
                            .clickable(
                                onClick = { toggleBlock(it) }
                            ),
                        color = if (blocked) Color.Red else MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "$it ${if (blocked) "BLOCKED" else ""}",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}