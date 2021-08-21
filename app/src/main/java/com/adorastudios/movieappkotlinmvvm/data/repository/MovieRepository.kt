package com.adorastudios.movieappkotlinmvvm.data.repository

import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview
import com.adorastudios.movieappkotlinmvvm.data.Result

interface MovieRepository {
    suspend fun loadMovies(): Result<List<MoviePreview>>
    suspend fun loadMovie(movieId: Long): Result<MovieDetails>
}