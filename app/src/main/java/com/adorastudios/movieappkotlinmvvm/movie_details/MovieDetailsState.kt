package com.adorastudios.movieappkotlinmvvm.movie_details

import com.adorastudios.movieappkotlinmvvm.model.MovieDetails

sealed class MovieDetailsState {
    class MovieLoaded(val movie: MovieDetails) : MovieDetailsState()
    object MovieError : MovieDetailsState()
}