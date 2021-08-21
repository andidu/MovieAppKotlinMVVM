package com.adorastudios.movieappkotlinmvvm.movies_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adorastudios.movieappkotlinmvvm.data.repository.MovieRepository
import com.adorastudios.movieappkotlinmvvm.notifications.Notifications

class MoviesListViewModelFactory(
    private val repository: MovieRepository,
    private val notifications: Notifications
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MoviesListViewModel(repository, notifications) as T
}