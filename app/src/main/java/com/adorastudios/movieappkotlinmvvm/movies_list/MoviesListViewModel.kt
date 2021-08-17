package com.adorastudios.movieappkotlinmvvm.movies_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview
import com.adorastudios.movieappkotlinmvvm.data.repository.MovieRepository
import kotlinx.coroutines.launch

class MoviesListViewModel(private val repository: MovieRepository) : ViewModel() {

    private val moviesList: MutableLiveData<List<MoviePreview>> = MutableLiveData(emptyList())
    val movies: MutableLiveData<List<MoviePreview>> get() = moviesList

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            moviesList.postValue(repository.loadMovies())
        }
    }



}