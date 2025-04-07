package com.example.sokasinging.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sokasinging.presentation.viewmodel.SongDetailViewModel


@Composable
fun SongPlaying(modifier: Modifier,
                songName: String,
                viewModel: SongDetailViewModel = hiltViewModel(),
                navController: NavController
                ){

    val uiState = viewModel.uiState.collectAsState()
    val currentState = uiState.value
    val url = currentState.songDetails?.songURL
    val songLyrics = currentState.songDetails?.songLyrics ?: ""

    LaunchedEffect(songName) {
        viewModel.loadSongDetails(songName)
    }

    Scaffold(
        topBar = { TopBar2(modifier = Modifier, navController = navController) },
        bottomBar = { BottomBar2(modifier = Modifier) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(
                modifier.fillMaxSize().padding(5.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier.padding(5.dp).height(430.dp).fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .background(MaterialTheme.colorScheme.surfaceVariant).border(
                        5.dp,
                        MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(10.dp)
                    ),
                )
                {
                    when {
                        currentState.isLoading -> {
                            CircularProgressIndicator(
                                modifier.align(Alignment.Center),
                            )
                        }

                        currentState.error != null -> {
                            Text(
                                text = currentState.error,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(15.dp)
                            )
                        }

                        else -> {
                            Text(
                                text = songLyrics,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(15.dp)
                            )
                        }
                    }

                }
                if (!url.isNullOrEmpty() && !currentState.isLoading) {
                    ExoMediaPlayer(songName, url)
                }

            }
        }

    }
}



@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar2(modifier: Modifier, navController: NavController)
{
    TopAppBar(
        modifier = modifier.shadow(elevation = 8.dp),
        colors = TopAppBarDefaults.
        topAppBarColors(containerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        ),
        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text( text = "Now Playing",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 15.dp).align(Alignment.CenterVertically)
                ) }
        },

        windowInsets = TopAppBarDefaults.windowInsets,
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier.padding(horizontal = 4.dp).size(30.dp)
                    )
                }
            }
    )

}

@Composable
fun BottomBar2(modifier: Modifier
){
    var selectedItem by rememberSaveable { mutableStateOf(0) }
    var navItems = listOf("Home", "Profile", "Settings")
    var navIcons = listOf(Icons.Default.Home, Icons.Default.AccountBox, Icons.Default.Settings)
    NavigationBar {
        navItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(navIcons[index], contentDescription = item) },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = { selectedItem = index }
            )
        }
    }
}