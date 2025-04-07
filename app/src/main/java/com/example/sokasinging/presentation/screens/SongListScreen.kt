package com.example.sokasinging.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sokasinging.R
import com.example.sokasinging.presentation.navigation.ScreenB
import com.example.sokasinging.presentation.viewmodel.SongsListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongListScreen(
    modifier: Modifier,
    viewModel: SongsListViewModel = hiltViewModel(),
    navController: NavController
){
    val uiState = viewModel.uiState.collectAsState()
    val currentState = uiState.value

    var searchMode by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val filteredSongs = if (searchText.isBlank()) {
        currentState.songs
    } else {
        currentState.songs.filter {
            it.songTitle.contains(searchText, ignoreCase = true)
        }
    }
    Scaffold(
        topBar = { TopBar(modifier = Modifier,
            searchMode = searchMode,
            searchText = searchText,
            onSearchTextChange = { searchText = it },
            onToggleSearch = { searchMode = !searchMode
            if (!searchMode) searchText = ""
            }
            ) },
        bottomBar = { BottomBar(modifier = Modifier) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                currentState.isLoading -> ProgressBar()
                currentState.error != null -> Text(
                    text = "Error: ${currentState.error}",
                    modifier.align(Alignment.Center)
                )
                else -> LazyColumn {
                    itemsIndexed(filteredSongs) { index, song ->
                        SongsList(songName = song.songTitle,
                            year = song.year, modifier,
                            onClick = {
                            navController.navigate(ScreenB(
                                name = song.songTitle
                            ))
                        })
                    }
                }
            }
        }
    }
}


@Composable
fun ProgressBar(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator()
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar(modifier: Modifier,
           searchMode: Boolean,
           searchText: String,
           onSearchTextChange: (String) -> Unit,
           onToggleSearch: () -> Unit
           )
{
    TopAppBar(
        modifier = modifier.shadow(elevation = 8.dp),
        colors = TopAppBarDefaults.
        topAppBarColors(containerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        ),
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        title = {
            if (searchMode) {
                TextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    placeholder = { Text("Search songs...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
            else{
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) { Image(painterResource(R.drawable.bsglogo), contentDescription = "",
                    modifier
                        .size(35.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
                    Text( text = "Soka Singing",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 15.dp)
                    ) }
            }
        },
        windowInsets = TopAppBarDefaults.windowInsets,
        actions = {
//            IconButton(onClick = {}) {
//                Icon(
//                    imageVector = Icons.Filled.FavoriteBorder,
//                    contentDescription = "favourites",
//                    modifier
//                        .size(42.dp)
//                        .padding(start = 5.dp, end = 10.dp)
//                )
//            }

            IconButton(onClick = onToggleSearch) {
                Icon(
                    imageVector = if (searchMode) Icons.Default.Close else Icons.Default.Search,
                    contentDescription = if (searchMode) "Close Search" else "Search",
                    modifier
                        .size(42.dp)
                        .padding(start = 5.dp, end = 10.dp)
                )
            }

        }

    )

}

@Composable
fun BottomBar(modifier: Modifier
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