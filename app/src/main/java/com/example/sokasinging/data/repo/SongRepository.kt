package com.example.sokasinging.data.repo

import com.example.sokasinging.data.model.Song
import com.example.sokasinging.data.model.SongDetails
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    suspend fun getSongs(): Flow<List<Song>>
    suspend fun getSongDetails(songName: String): Flow<SongDetails>
}