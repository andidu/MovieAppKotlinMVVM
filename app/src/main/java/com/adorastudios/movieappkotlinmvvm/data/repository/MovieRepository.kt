package com.adorastudios.movieappkotlinmvvm.data.repository

import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview

interface MovieRepository {
    suspend fun loadMovies(): List<MoviePreview>
    suspend fun loadMovie(movieId: Int): MovieDetails?
}