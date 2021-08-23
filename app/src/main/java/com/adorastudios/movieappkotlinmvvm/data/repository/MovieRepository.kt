package com.adorastudios.movieappkotlinmvvm.data.repository

import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview
import com.adorastudios.movieappkotlinmvvm.data.Result

interface MovieRepository {
    suspend fun loadMoviesRemote(): Result<List<MoviePreview>>
    suspend fun loadMovieRemote(movieId: Long): Result<MovieDetails>

    suspend fun loadMoviesLocale(): Result<List<MoviePreview>>
    suspend fun loadMovieLocale(movieId: Long): Result<MovieDetails>
}