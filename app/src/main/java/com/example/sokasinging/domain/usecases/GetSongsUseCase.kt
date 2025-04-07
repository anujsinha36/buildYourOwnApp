package com.example.sokasinging.domain.usecases

import com.example.sokasinging.data.repo.SongRepository
import javax.inject.Inject

class GetSongsUseCase @Inject constructor(private val repository: SongRepository) {

    suspend operator fun invoke() = repository.getSongs()

}