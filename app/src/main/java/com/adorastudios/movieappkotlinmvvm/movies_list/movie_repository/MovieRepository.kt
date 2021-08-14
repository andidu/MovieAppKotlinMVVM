package com.adorastudios.movieappkotlinmvvm.movies_list.movie_repository

import com.adorastudios.movieappkotlinmvvm.data.MovieDetails
import com.adorastudios.movieappkotlinmvvm.data.MoviePreview

interface MovieRepository {
    fun loadMovies(): List<MoviePreview>
    fun loadMovie(movieId: Int): MovieDetails?
}