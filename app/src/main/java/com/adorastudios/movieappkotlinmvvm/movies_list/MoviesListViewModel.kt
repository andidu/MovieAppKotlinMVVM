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

    private val loadedFrom: MutableLiveData<LoadedFrom> = MutableLiveData()
    val loaded: MutableLiveData<LoadedFrom> get() = loadedFrom

    private val random = Random(System.currentTimeMillis())

    fun init() {
        loadedFrom.postValue(LoadedFrom.Nowhere)
        loadMovies()
        notifications.initialize()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            handleMoviePreviewRemoteResult(repository.loadMoviesRemote())
        }
    }

    private fun handleMoviePreviewRemoteResult(result: Result<List<MoviePreview>>) {
        when (result) {
            is Success -> {
                moviesList.postValue(MoviesListViewState.MoviesLoaded(result.data))
                loadedFrom.postValue(LoadedFrom.FromRemote)
            }
            is Failure -> loadMoviesFromDB()
        }
    }

    private fun loadMoviesFromDB() {
        viewModelScope.launch {
            handleMoviePreviewLocaleResult(repository.loadMoviesLocale())
        }
    }

    private fun handleMoviePreviewLocaleResult(result: Result<List<MoviePreview>>) {
        when (result) {
            is Success -> {
                moviesList.postValue(MoviesListViewState.MoviesLoaded(result.data))
                loadedFrom.postValue(LoadedFrom.FromLocale)
            }
            is Failure -> moviesList.postValue(MoviesListViewState.FailedToLoad(result.exception))
        }
    }

    fun finishLoading() {
        loadedFrom.postValue(LoadedFrom.Nowhere)
    }

    fun showNotification(list: List<MoviePreview>) :Boolean {
        if (list.isEmpty()) return false
        val randomId = list[random.nextInt(list.size)].id
        viewModelScope.launch {
            handleMovieDetailsResult(repository.loadMovieRemote(randomId))
        }
        return true
    }

    private fun handleMovieDetailsResult(result: Result<MovieDetails>) {
        if (result is Success) {
            notifications.showNotification(result.data)
        }
    }
}