package com.adorastudios.movieappkotlinmvvm.data.locale

import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview

interface LocaleDataSource {
    suspend fun loadMovies(): List<MoviePreview>
    fun insertMovies(moviePreviewFromNetwork: List<MoviePreview>)
    suspend fun loadMovie(movieId: Long): List<MovieDetails>
    fun insertMovieDetails(movieDetailsFromNetwork: MovieDetails)
}