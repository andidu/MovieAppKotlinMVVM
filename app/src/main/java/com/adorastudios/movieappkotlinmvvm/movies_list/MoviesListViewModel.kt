package com.adorastudios.movieappkotlinmvvm.movies_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adorastudios.movieappkotlinmvvm.data.Failure
import com.adorastudios.movieappkotlinmvvm.data.Result
import com.adorastudios.movieappkotlinmvvm.data.Success
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview
import com.adorastudios.movieappkotlinmvvm.data.repository.MovieRepository
import kotlinx.coroutines.launch

class MoviesListViewModel(private val repository: MovieRepository) : ViewModel() {

    private val moviesList: MutableLiveData<MoviesListViewState> = MutableLiveData()
    val movies: MutableLiveData<MoviesListViewState> get() = moviesList

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            handleResult(repository.loadMovies())
        }
    }

    private fun handleResult(result: Result<List<MoviePreview>>) {
        when (result) {
            is Success -> moviesList.postValue(MoviesListViewState.MoviesLoaded(result.data))
            is Failure -> moviesList.postValue(MoviesListViewState.FailedToLoad(result.exception))
        }
    }
}