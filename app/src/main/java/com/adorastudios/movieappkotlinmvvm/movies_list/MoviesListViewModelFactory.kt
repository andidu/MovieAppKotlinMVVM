package com.adorastudios.movieappkotlinmvvm.movies_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adorastudios.movieappkotlinmvvm.data.repository.MovieRepository
import com.adorastudios.movieappkotlinmvvm.notifications.Notifications
import com.adorastudios.movieappkotlinmvvm.scheduler.SchedulerProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Suppress("UNCHECKED_CAST")
class MoviesListViewModelFactory(
    private val schedulerProvider: SchedulerProvider,
    private val repository: MovieRepository,
    private val notifications: Notifications
) : ViewModelProvider.Factory {
    @ExperimentalCoroutinesApi
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MoviesListViewModelImpl(schedulerProvider, repository, notifications) as T
}