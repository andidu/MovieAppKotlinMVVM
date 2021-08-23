package com.adorastudios.movieappkotlinmvvm.movie_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adorastudios.movieappkotlinmvvm.data.Failure
import com.adorastudios.movieappkotlinmvvm.data.Result
import com.adorastudios.movieappkotlinmvvm.data.Success
import com.adorastudios.movieappkotlinmvvm.data.repository.MovieRepository
import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import kotlinx.coroutines.launch

class MovieDetailsViewModel(private val repository: MovieRepository) : ViewModel() {
    private val movieDetails: MutableLiveData<MovieDetailsState> = MutableLiveData()
    val movie: MutableLiveData<MovieDetailsState> get() = movieDetails

    fun loadMovie(movieId: Long) {
        viewModelScope.launch {
            val m = repository.loadMovieRemote(movieId)
            handleResult(movieId, m)
        }
    }

    private fun handleResult(movieId: Long, result: Result<MovieDetails?>) {
        when (result) {
            is Success -> if (result.data == null) {
                loadMoviesFromDB(movieId)
            } else {
                movieDetails.postValue(MovieDetailsState.MovieLoaded(result.data))
            }
            is Failure -> loadMoviesFromDB(movieId)
        }
    }

    private fun loadMoviesFromDB(movieId: Long) {
        viewModelScope.launch {
            handleMoviePreviewLocaleResult(repository.loadMovieLocale(movieId))
        }
    }

    private fun handleMoviePreviewLocaleResult(result: Result<MovieDetails>) {
        when (result) {
            is Success -> {
                movieDetails.postValue(MovieDetailsState.MovieLoaded(result.data))
            }
            is Failure -> movieDetails.postValue(MovieDetailsState.MovieError(result.exception))
        }
    }
}