package com.adorastudios.movieappkotlinmvvm.movie_details

import com.adorastudios.movieappkotlinmvvm.model.MovieDetails

sealed class MovieDetailsState {
    class MovieLoaded(val movie: MovieDetails) : MovieDetailsState()
    object MovieNotLoaded : MovieDetailsState()
    class MovieError(val error: Throwable) : MovieDetailsState()
}