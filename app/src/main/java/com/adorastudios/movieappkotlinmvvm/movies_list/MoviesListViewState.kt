package com.adorastudios.movieappkotlinmvvm.movies_list

import com.adorastudios.movieappkotlinmvvm.model.MoviePreview

sealed class MoviesListViewState {

    data class MoviesLoaded(val movies: List<MoviePreview>) : MoviesListViewState()

    data class FailedToLoad(val exception: Throwable) : MoviesListViewState()
}
