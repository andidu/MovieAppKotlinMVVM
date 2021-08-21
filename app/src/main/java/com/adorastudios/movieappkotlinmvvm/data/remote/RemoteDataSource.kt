package com.adorastudios.movieappkotlinmvvm.data.remote

import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview

interface RemoteDataSource {
    suspend fun loadMovies() : List<MoviePreview>
    suspend fun loadMovie(id: Long) : MovieDetails
}