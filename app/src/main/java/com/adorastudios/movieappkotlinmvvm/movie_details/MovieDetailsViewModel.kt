package com.adorastudios.movieappkotlinmvvm.movie_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adorastudios.movieappkotlinmvvm.movie_repository.MovieRepository
import kotlinx.coroutines.launch

class MovieDetailsViewModel(private val repository: MovieRepository) : ViewModel() {
    private val movieDetails: MutableLiveData<MovieDetailsState> = MutableLiveData()
    val movie: MutableLiveData<MovieDetailsState> get() = movieDetails

    fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            val m = repository.loadMovie(movieId)
            if (m == null) {
                movieDetails.value = MovieDetailsState.MovieError
            } else {
                movieDetails.value = MovieDetailsState.MovieLoaded(m)
            }
        }
    }
}