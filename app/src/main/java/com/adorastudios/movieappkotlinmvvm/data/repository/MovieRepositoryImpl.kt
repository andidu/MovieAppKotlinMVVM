package com.adorastudios.movieappkotlinmvvm.data.repository

import com.adorastudios.movieappkotlinmvvm.data.remote.RemoteDataSource
import com.adorastudios.movieappkotlinmvvm.model.Actor
import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepositoryImpl (private val remoteDataSource: RemoteDataSource) : MovieRepository {

    override suspend fun loadMovies(): List<MoviePreview> {
        return remoteDataSource.loadMovies()
    }

    override suspend fun loadMovie(movieId: Int): MovieDetails? {
        return remoteDataSource.loadMovie(movieId)
    }
}