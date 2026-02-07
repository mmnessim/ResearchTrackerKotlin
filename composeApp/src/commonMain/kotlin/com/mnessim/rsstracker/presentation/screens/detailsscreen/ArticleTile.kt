package com.mnessim.rsstracker.presentation.screens.detailsscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mnessim.rsstracker.domain.models.Article
import com.mnessim.rsstracker.domain.repositories.SavedArticlesRepo
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


@OptIn(ExperimentalTime::class)
@Composable
fun ArticleTile(modifier: Modifier = Modifier, article: Article) {
    val savedArticlesRepo = koinInject<SavedArticlesRepo>()
    var isSaved by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }
    val baseFontSize = 16
    val actualTimeString = epochMsToLocalStringKmp(article.pubDateMs)
    val urlHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        val article = savedArticlesRepo.getOneArticle(article.guid)
        isSaved = article != null
    }

    Card(
        modifier = modifier
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { urlHandler.openUri(article.link) },
                    text = article.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = (baseFontSize + 4).sp
//                        fontWeight = FontWeight.Bold
                    )
                )
                IconButton(
                    onClick = {
                        if (!isSaved) {
                            saveArticle(savedArticlesRepo, article)
                            isSaved = true
                        } else {
                            showAlert = true
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isSaved) Icons.Filled.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Save Article",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = actualTimeString,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = (baseFontSize).sp

                    )
                )
                Text(
                    text = article.rssSource,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = (baseFontSize).sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = truncateText(article.description, 500),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = (baseFontSize).sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                article.categories.forEachIndexed { i, c ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = c,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = (baseFontSize - 4).sp
                            )
                        )
                    }
                    if (i < article.categories.size - 1) {
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
    }

    if (showAlert) {
        UnsaveAlert(
            onDismiss = { showAlert = false },
            onConfirm = {
                showAlert = false
                isSaved = false
                unsaveArticle(savedArticlesRepo, article)
            }
        )
    }
}

fun saveArticle(savedArticlesRepo: SavedArticlesRepo, article: Article) {
    savedArticlesRepo.insertArticle(article)
}

fun unsaveArticle(savedArticlesRepo: SavedArticlesRepo, article: Article) {
    savedArticlesRepo.deleteArticle(article.title)
}

fun truncateText(text: String?, maxChars: Int): String {
    if (text.isNullOrEmpty() || maxChars <= 0) return ""
    if (text.length <= maxChars) return text

    val contentLimit = (maxChars - 3).coerceAtLeast(0)
    var cut = text.take(contentLimit).trimEnd()

    val lastWs = cut.indexOfLast { it.isWhitespace() }
    if (lastWs > 0) {
        cut = cut.substring(0, lastWs).trimEnd()
    }

    return cut + "..."
}

@OptIn(ExperimentalTime::class)
fun parseRfc822ToInstant(dateString: String): Instant? {
    // Example input: "Thu, 25 Dec 2025 16:15:13 +0000"
    // Convert to: "2025-12-25T16:15:13Z"
    return try {
        val parts = dateString.substringAfter(", ").split(" ")
        val date = "${parts[2]}-${monthToNumber(parts[1])}-${parts[0]}"
        val time = parts[3]
        val isoString = "${date}T${time}Z"
        Instant.parseOrNull(isoString)
    } catch (e: Exception) {
        null
    }
}

fun monthToNumber(month: String): String = when (month) {
    "Jan" -> "01"
    "Feb" -> "02"
    "Mar" -> "03"
    "Apr" -> "04"
    "May" -> "05"
    "Jun" -> "06"
    "Jul" -> "07"
    "Aug" -> "08"
    "Sep" -> "09"
    "Oct" -> "10"
    "Nov" -> "11"
    "Dec" -> "12"
    else -> "01"
}

@OptIn(ExperimentalTime::class)
fun epochMsToLocalStringKmp(epochMs: Long?, zoneId: String = "America/New_York"): String {
    if (epochMs == null) return ""
    val instant = Instant.fromEpochMilliseconds(epochMs)
    val tz = TimeZone.of(zoneId)
    val ldt = instant.toLocalDateTime(tz)
    val minuteStr = ldt.minute.toString().padStart(2, '0')
    val monthStr = ldt.month.name.lowercase().replaceFirstChar { it.uppercase() }
    return "$monthStr ${ldt.day} ${ldt.hour}:${minuteStr}"
}

@Composable
fun UnsaveAlert(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        text = {
            Text("Are you sure you want to unsave this article?")
        },
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                content = { Text("Confirm") })
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                content = { Text("Dismiss") }
            )
        }
    )
}