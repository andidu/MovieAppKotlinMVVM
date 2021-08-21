package com.adorastudios.movieappkotlinmvvm.movies_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adorastudios.movieappkotlinmvvm.data.Failure
import com.adorastudios.movieappkotlinmvvm.data.Result
import com.adorastudios.movieappkotlinmvvm.data.Success
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview
import com.adorastudios.movieappkotlinmvvm.data.repository.MovieRepository
import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import com.adorastudios.movieappkotlinmvvm.notifications.Notifications
import kotlinx.coroutines.launch
import kotlin.random.Random

class MoviesListViewModel(
    private val repository: MovieRepository,
    private val notifications: Notifications
) : ViewModel() {

    private val moviesList: MutableLiveData<MoviesListViewState> = MutableLiveData()
    val movies: MutableLiveData<MoviesListViewState> get() = moviesList
    private val random = Random(System.currentTimeMillis())

    init {
        loadMovies()
        notifications.initialize()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            handleMoviePreviewResult(repository.loadMovies())
        }
    }

    private fun handleMoviePreviewResult(result: Result<List<MoviePreview>>) {
        when (result) {
            is Success -> moviesList.postValue(MoviesListViewState.MoviesLoaded(result.data))
            is Failure -> moviesList.postValue(MoviesListViewState.FailedToLoad(result.exception))
        }
    }

    fun showNotification(list: List<MoviePreview>) {
        val randomId = list[random.nextInt(list.size)].id
        viewModelScope.launch {
            handleMovieDetailsResult(repository.loadMovie(randomId))
        }
    }

    private fun handleMovieDetailsResult(result: Result<MovieDetails>) {
        if (result is Success) {
            notifications.showNotification(result.data)
        }
    }
}