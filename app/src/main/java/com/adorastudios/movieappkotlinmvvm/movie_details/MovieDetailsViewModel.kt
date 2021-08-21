package com.adorastudios.movieappkotlinmvvm.movie_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adorastudios.movieappkotlinmvvm.data.Failure
import com.adorastudios.movieappkotlinmvvm.data.Success
import com.adorastudios.movieappkotlinmvvm.data.Result
import com.adorastudios.movieappkotlinmvvm.data.repository.MovieRepository
import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import kotlinx.coroutines.launch

class MovieDetailsViewModel(private val repository: MovieRepository) : ViewModel() {
    private val movieDetails: MutableLiveData<MovieDetailsState> = MutableLiveData()
    val movie: MutableLiveData<MovieDetailsState> get() = movieDetails

    fun loadMovie(movieId: Long) {
        viewModelScope.launch {
            val m = repository.loadMovie(movieId)
            handleResult(m)
        }
    }

    private fun handleResult(result: Result<MovieDetails?>) {
        when (result) {
            is Success -> if (result.data == null) {
                movieDetails.postValue(MovieDetailsState.MovieNotLoaded)
            } else {
                movieDetails.postValue(MovieDetailsState.MovieLoaded(result.data))
            }
            is Failure -> movieDetails.postValue(MovieDetailsState.MovieError(result.exception))
        }
    }
}