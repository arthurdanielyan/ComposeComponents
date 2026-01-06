package com.danielyan.coreCompose.componentsDemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.danielyan.coreCompose.components.separatedFlowRow.SeparatedFlowRow
import kotlin.random.Random

@Composable
internal fun SeparatedFlowRowDemo() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp
        )
    ) {
        Text(
            text = "SeparatedFlowRow",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        SeparatedFlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                alignment = Alignment.CenterHorizontally,
                space = 8.dp
            ),
            verticalArrangement = Arrangement
                .spacedBy(
                    alignment = Alignment.CenterVertically,
                    space = 8.dp,
                ),
            itemVerticalAlignment = Alignment.Bottom,
            itemSeparator = {
                VerticalDivider(
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = 3.dp
                )
            }
        ) {
            repeat(20) {
                Box(
                    modifier = Modifier
                        .width(Random.nextInt(30, 60).dp)
                        .height(Random.nextInt(50, 100).dp)
                        .background(Color(Random.nextLong(0xFFFFFFFF))),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = it.toString())
                }
            }
        }

        SeparatedFlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                alignment = Alignment.Start,
                space = 8.dp
            ),
            verticalArrangement = Arrangement
                .spacedBy(
                    alignment = Alignment.CenterVertically,
                    space = 8.dp,
                ),
            itemVerticalAlignment = Alignment.Bottom,
            itemSeparator = {
                Box(
                    Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onBackground)
                )
            }
        ) {
            BookTitles.forEach {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

private val BookTitles = listOf(
    "Genesis",
    "Exodus",
    "Leviticus",
    "Numbers",
    "Deuteronomy",
    "Joshua",
    "Judges",
    "Ruth",
    "1 Samuel",
    "2 Samuel",
    "1 Kings",
    "2 Kings",
    "1 Chronicles",
    "2 Chronicles",
    "Ezra",
    "Nehemiah",
    "Esther",
    "Job",
    "Psalms",
    "Proverbs",
    "Ecclesiastes",
    "Song of Songs",
    "Isaiah",
    "Jeremiah",
    "Lamentations",
    "Ezekiel",
    "Daniel",
    "Hosea",
    "Joel",
    "Amos",
    "Obadiah",
    "Jonah",
    "Micah",
    "Nahum",
    "Habakkuk",
    "Zephaniah",
    "Haggai",
    "Zachariah",
    "Malachi",
    "Matthew",
    "Mark",
    "Luke",
    "John",
    "Acts",
    "Romans",
    "1 Corinthians",
    "2 Corinthians",
    "Galatians",
    "Ephesians",
    "Philippians",
    "Colossians",
    "1 Thessalonians",
    "2 Thessalonians",
    "1 Timothy",
    "2 Timothy",
    "Titus",
    "Philemon",
    "Hebrews",
    "James",
    "1 Peter",
    "2 Peter",
    "1 John",
    "2 John",
    "3 John",
    "Jude",
    "Revelation"
)
