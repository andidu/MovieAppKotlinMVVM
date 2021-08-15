package com.adorastudios.movieappkotlinmvvm.movie_details

import com.adorastudios.movieappkotlinmvvm.data.MovieDetails

sealed class MovieDetailsState {
    class MovieLoaded(val movie: MovieDetails) : MovieDetailsState()
    object MovieError : MovieDetailsState()
}