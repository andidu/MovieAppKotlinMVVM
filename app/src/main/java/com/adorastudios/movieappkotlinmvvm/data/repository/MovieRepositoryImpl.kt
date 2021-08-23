package com.adorastudios.movieappkotlinmvvm.data.repository

import com.adorastudios.movieappkotlinmvvm.data.Result
import com.adorastudios.movieappkotlinmvvm.data.locale.LocaleDataSource
import com.adorastudios.movieappkotlinmvvm.data.remote.RemoteDataSource
import com.adorastudios.movieappkotlinmvvm.data.runCatchingResult
import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localeDataSource: LocaleDataSource
) : MovieRepository {

    override suspend fun loadMoviesRemote(): Result<List<MoviePreview>> {
        return runCatchingResult {
            withContext(Dispatchers.IO) {
                val movies = remoteDataSource.loadMovies()
                launch { localeDataSource.insertMovies(movies) }
                movies
            }
        }
    }

    override suspend fun loadMovieRemote(movieId: Long): Result<MovieDetails> {
        return runCatchingResult {
            withContext(Dispatchers.IO) {
                val movie = remoteDataSource.loadMovie(movieId)
                launch { localeDataSource.insertMovieDetails(movie) }
                movie
            }
        }
    }

    override suspend fun loadMoviesLocale(): Result<List<MoviePreview>> {
        return runCatchingResult {
            withContext(Dispatchers.IO) {
                localeDataSource.loadMovies()
            }
        }
    }

    override suspend fun loadMovieLocale(movieId: Long): Result<MovieDetails> {
        return runCatchingResult {
            withContext(Dispatchers.IO) {
                localeDataSource.loadMovie(movieId).first()
            }
        }
    }
}