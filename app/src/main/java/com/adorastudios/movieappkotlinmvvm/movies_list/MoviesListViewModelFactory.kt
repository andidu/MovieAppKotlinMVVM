package com.adorastudios.movieappkotlinmvvm.movies_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adorastudios.movieappkotlinmvvm.movies_list.movie_repository.MovieRepository

class MoviesListViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MoviesListViewModel(repository) as T
}