package com.danielyan.coreCompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.danielyan.coreCompose.componentsDemo.BottomSheetDemo
import com.danielyan.coreCompose.componentsDemo.CircularDotsLoaderDemo
import com.danielyan.coreCompose.componentsDemo.CircularLoadingDemo
import com.danielyan.coreCompose.componentsDemo.MediaPlaybackProgressIndicatorDemo
import com.danielyan.coreCompose.componentsDemo.RunningTextDemo
import com.danielyan.coreCompose.componentsDemo.SeparatedFlowRowDemo
import com.danielyan.coreCompose.componentsDemo.SwitchDemo
import com.danielyan.coreCompose.componentsDemo.TabRowDemo
import com.danielyan.coreCompose.ui.theme.CoreComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoreComposeTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .statusBarsPadding()
                        .verticalScroll(rememberScrollState())
                        .navigationBarsPadding(),
                    verticalArrangement = Arrangement
                        .spacedBy(
                            space = 32.dp,
                            alignment = Alignment.Top
                        )
                ) {
                    SeparatedFlowRowDemo()
                    MediaPlaybackProgressIndicatorDemo()
                    BottomSheetDemo()
                    SwitchDemo()
                    CircularLoadingDemo()
                    CircularDotsLoaderDemo()
                    RunningTextDemo()
                    TabRowDemo()
                }
            }
        }
    }
}
