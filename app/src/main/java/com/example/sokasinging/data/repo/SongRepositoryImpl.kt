package com.example.sokasinging.data.repo

import android.util.Log
import com.example.sokasinging.data.model.Song
import com.example.sokasinging.data.model.SongDetails
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase): SongRepository {
        val songsList = mutableListOf<Song>()


    override suspend fun getSongs(): Flow<List<Song>> = flow {
        val songRef = firebaseDatabase.getReference("songs").get().await()
        for (songSnapshot in songRef.children) {
            val song = songSnapshot.getValue(SongDetails::class.java)
            val songsFinal = Song(
                songTitle = song?.songName.toString(),
                year = song?.songYear!!.toInt()
            )
            songsList.add(songsFinal)
            emit(songsList)

        }
    }

    override suspend fun getSongDetails(songName: String): Flow<SongDetails> = flow {
        val songRef = firebaseDatabase.getReference("songs/$songName").get().await()

        val songPlaying = songRef.child("songName").value.toString()
        val lyrics = songRef.child("songLyrics").value.toString()
        val songUrl = songRef.child("songUrl").value.toString()

        emit(SongDetails(songPlaying, lyrics, songUrl))
    }
}