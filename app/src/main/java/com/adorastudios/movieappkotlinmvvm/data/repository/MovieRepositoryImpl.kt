package com.adorastudios.movieappkotlinmvvm.data.repository

import com.adorastudios.movieappkotlinmvvm.data.Result
import com.adorastudios.movieappkotlinmvvm.data.locale.LocaleDataSource
import com.adorastudios.movieappkotlinmvvm.data.remote.RemoteDataSource
import com.adorastudios.movieappkotlinmvvm.data.runCatchingResult
import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localeDataSource: LocaleDataSource
) : MovieRepository {

    override suspend fun loadMovies(): Result<List<MoviePreview>> {
        return runCatchingResult {
            withContext(Dispatchers.IO) {
                val moviePreviewDB = localeDataSource.loadMovies()
                if (moviePreviewDB.isEmpty()) {
                    val moviePreviews = remoteDataSource.loadMovies()
                    localeDataSource.insertMovies(moviePreviews)
                    moviePreviews
                } else {
                    moviePreviewDB
                }
            }
        }
    }

    override suspend fun loadMovie(movieId: Long): Result<MovieDetails> {
        return runCatchingResult {
            withContext(Dispatchers.IO) {
                val movieDetailsDB = localeDataSource.loadMovie(movieId)
                if (movieDetailsDB.isEmpty()) {
                    val movieDetails = remoteDataSource.loadMovie(movieId)
                    localeDataSource.insertMovieDetails(movieDetails)
                    movieDetails
                } else {
                    movieDetailsDB.first()
                }
            }
        }
    }
}