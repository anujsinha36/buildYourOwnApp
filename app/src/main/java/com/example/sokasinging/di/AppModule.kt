package com.example.sokasinging.di

import com.example.sokasinging.data.repo.SongRepository
import com.example.sokasinging.data.repo.SongRepositoryImpl
import com.example.sokasinging.domain.usecases.GetSongDetailsUseCase
import com.example.sokasinging.domain.usecases.GetSongsUseCase
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun providesFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Singleton
    @Provides
    fun provideSongRepository(firebaseDatabase: FirebaseDatabase): SongRepository =
        SongRepositoryImpl(firebaseDatabase)

    @Provides
    fun provideGetSongsUseCase(repository: SongRepository): GetSongsUseCase =
        GetSongsUseCase(repository)

    @Provides
    fun provideGetSongDetailsUseCase(repository: SongRepository): GetSongDetailsUseCase =
        GetSongDetailsUseCase(repository)

}