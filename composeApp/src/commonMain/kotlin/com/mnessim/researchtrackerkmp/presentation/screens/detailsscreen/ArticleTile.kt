package com.mnessim.researchtrackerkmp.presentation.screens.detailsscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mnessim.researchtrackerkmp.domain.models.Article
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


@OptIn(ExperimentalTime::class)
@Composable
fun ArticleTile(modifier: Modifier = Modifier, article: Article) {
    val baseFontSize = 16

    val time = parseRfc822ToInstant(article.pubDate ?: "")
    val estZone = TimeZone.of("America/New_York")
    val dateTime = time?.toLocalDateTime(estZone)
    val minuteStr = dateTime?.minute.toString().padStart(2, '0')
    val monthStr = dateTime?.month?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: ""
    val timeStr = "$monthStr ${dateTime?.day} ${dateTime?.hour}:${minuteStr}"
    val urlHandler = LocalUriHandler.current

    val actualTimeString = if (dateTime != null) {
        timeStr
    } else article.pubDate ?: ""

    SelectionContainer {
        Column(
            modifier = modifier.padding(8.dp)
                .clickable(
                    onClick = { urlHandler.openUri(article.link) }
                )
        ) {
            Text(
                text = "${article.title} - ${article.rssSource}",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = (baseFontSize + 8).sp
                )
            )
            Text(
                text = actualTimeString,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = (baseFontSize).sp
                )
            )
            // 2 Minute Medicine puts raw HTML in article.description
            if (article.rssSource != "2 Minute Medicine") {
                Text(
                    text = truncateText(article.description, 500),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = (baseFontSize).sp
                    )
                )
            }

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                for ((i, c) in article.categories.withIndex()) {
                    Text(
                        text = c,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = (baseFontSize - 4).sp
                        )
                    )
                    if (i < article.categories.size) {
                        Text(
                            text = " | ",
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = (baseFontSize - 4).sp
                            )
                        )
                    }
                }
            }

            // TODO: This is almost always null, so maybe just remove it?
            if (article.mediaContentUrl != null) {
                AsyncImage(
                    model = article.mediaContentUrl,
                    contentDescription = article.mediaDescription,
                    modifier = Modifier
                        .fillMaxWidth(.9f)
                        .height(150.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
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
