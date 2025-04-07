package com.example.sokasinging.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.sokasinging.presentation.screens.SongListScreen
import com.example.sokasinging.presentation.screens.SongPlaying
import kotlinx.serialization.Serializable

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ScreenA){
        composable<ScreenA>{
            SongListScreen(modifier = Modifier, navController = navController)
        }
        composable<ScreenB> {
            val args = it.toRoute<ScreenB>()
            SongPlaying(modifier = Modifier, navController = navController, songName = args.name?: "")
        }
    }
}

@Serializable
object ScreenA

@Serializable
data class ScreenB(
    val name: String?
)