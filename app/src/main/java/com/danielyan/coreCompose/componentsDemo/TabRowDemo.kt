package com.danielyan.coreCompose.componentsDemo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.danielyan.coreCompose.components.tabRow.TabRow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabRowDemo() {
    Column {
        val sectionsBig = remember {
            listOf("Matthew", "Luke", "1 Corinthians", "1 Thessalonians")
        }
        val pagerStateBig = rememberPagerState(
            pageCount = {
                sectionsBig.size
            }
        )
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            pagerState = pagerStateBig,
            items = sectionsBig,
        ) { modifier, title, _ ->
            Text(
                text = title,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                modifier = modifier
                    .weight(1f, false)
                    .padding(16.dp),
                overflow = TextOverflow.Ellipsis,
            )
        }
        HorizontalPager(
            state = pagerStateBig,
            key = { it },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = sectionsBig[it],
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        val sectionsSmall = remember {
            listOf("Matthew", "Mark", "Luke")
        }
        val pagerStateSmall = rememberPagerState(
            pageCount = {
                sectionsSmall.size
            }
        )
        TabRow(
            modifier = Modifier.fillMaxWidth(),
            pagerState = pagerStateSmall,
            indicatorColor = MaterialTheme.colorScheme.primary,
            indicatorThickness = 4.dp,
            items = sectionsSmall,
        ) { modifier, title, isSelected ->
            Text(
                text = title,
                textAlign = TextAlign.Center,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onBackground
                },
                maxLines = 1,
                modifier = modifier
                    .weight(1f, false)
                    .padding(16.dp),
                overflow = TextOverflow.Ellipsis,
            )
        }
        HorizontalPager(
            state = pagerStateSmall,
            key = { it },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = sectionsSmall[it],
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}