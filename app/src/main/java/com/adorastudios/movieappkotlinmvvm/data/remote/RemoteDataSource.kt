package com.adorastudios.movieappkotlinmvvm.data.remote

import com.adorastudios.movieappkotlinmvvm.model.Actor
import com.adorastudios.movieappkotlinmvvm.model.Genre
import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview

interface RemoteDataSource {
    suspend fun loadMovies() : List<MoviePreview>
    suspend fun loadMovie(id: Long) : MovieDetails
    suspend fun loadGenres() : List<Genre>
    suspend fun loadActors(id: Long) : List<Actor>
}