package com.danielyan.coreCompose.componentsDemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danielyan.coreCompose.components.switchC.Switch

@Composable
fun SwitchDemo() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp
        )
    ) {
        Text(
            text = "Switch",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        var isChecked by remember {
            mutableStateOf(false)
        }
        Switch(
            isChecked = isChecked,
            onCheckedStateChange = {
                isChecked = it
            }
        )
    }
}