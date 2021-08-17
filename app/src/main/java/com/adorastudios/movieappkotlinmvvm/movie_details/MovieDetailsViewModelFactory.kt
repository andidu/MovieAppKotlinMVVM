package com.adorastudios.movieappkotlinmvvm.movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adorastudios.movieappkotlinmvvm.data.repository.MovieRepository

class MovieDetailsViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MovieDetailsViewModel(repository) as T
}