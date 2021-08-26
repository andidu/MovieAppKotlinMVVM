package com.adorastudios.movieappkotlinmvvm.data.remote.retrofit

import com.adorastudios.movieappkotlinmvvm.data.remote.RemoteDataSource
import com.adorastudios.movieappkotlinmvvm.data.remote.network_module.ApiKeyInterceptor.Companion.API_KEY
import com.adorastudios.movieappkotlinmvvm.data.remote.retrofit.response.ImageResponse
import com.adorastudios.movieappkotlinmvvm.model.Actor
import com.adorastudios.movieappkotlinmvvm.model.Genre
import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlinx.coroutines.AbstractCoroutine

class RetrofitDataSource(private val api: MovieApiService) : RemoteDataSource {

    companion object {
        const val DEFAULT_SIZE = "original"
    }

    private var imageResponse: ImageResponse? = null
    private var baseUrl: String? = null
    private var posterSize: String? = null
    private var backdropSize: String? = null
    private var profileSize: String? = null

    override suspend fun loadMovies(): List<MoviePreview> {
        loadConfiguration()
        val genres = loadGenres()
        return api.loadUpcoming(page = 1).results.map { movie ->
            MoviePreview(
                id = movie.id,
                title = movie.title,
                imageUrl = formingUrl(baseUrl, posterSize, movie.posterPath),
                age = if (movie.adult) 16 else 13,
                genres = genres.filter { genre ->
                    movie.genreIds.contains(genre.id)
                },
                runningTime = -1,
                reviewCount = movie.voteCount,
                rating = movie.voteAverage,
                isLiked = false
            )
        }
    }

    override suspend fun loadMovie(id: Long): MovieDetails {
        loadConfiguration()
        val details = api.loadMovieDetails(id)
        return MovieDetails(
            id = id,
            title = details.title,
            storyLine = details.overview.orEmpty(),
            detailImageUrl = formingUrl(baseUrl, posterSize, details.backdropPath),
            age = if (details.adult) 16 else 13,
            genres = details.genres.map {
                Genre(it.id, it.name)
            },
            rating = details.voteAverage,
            isLiked = false,
            actors = loadActors(id),
            reviewCount = details.voteCount
        )
    }

    override suspend fun loadGenres(): List<Genre> {
        return api.loadGenres().genres.map { genreResponse ->
            Genre(genreResponse.id, genreResponse.name)
        }
    }

    override suspend fun loadActors(id: Long): List<Actor> {
        loadConfiguration()
        return api.loadMovieCast(id).cast.map {
            Actor(it.id, it.name, formingUrl(baseUrl, posterSize, it.profilePath))
        }
    }

    private suspend fun loadConfiguration() {
        if (imageResponse == null) {
            imageResponse = api.loadConfiguration().images
            baseUrl = imageResponse?.secureBaseUrl
            posterSize = imageResponse?.posterSizes?.find { it == "w500" }
            backdropSize = imageResponse?.backdropSizes?.find { it == "w780" }
            profileSize = imageResponse?.profileSizes?.find { it == "w185" }
        }
    }

    private fun formingUrl(url: String?, size: String?, path: String?): String? {
        return if (url == null || path == null) {
            null
        } else {
            url.plus(size.takeUnless { it.isNullOrEmpty() } ?: DEFAULT_SIZE)
                .plus(path)
        }
    }

    @FlowPreview
    override suspend fun searchMovies(query: String, page: Int): List<MoviePreview> {
        loadConfiguration()
        val genres = loadGenres()
        return withContext(Dispatchers.IO) {
            val t = api.searchMovie(API_KEY, query, page)
            flowOf(
                t
            )
        }
            .flowOn(Dispatchers.IO)
            .flatMapMerge { it.results.asFlow() }
            .map { movie ->
                MoviePreview(
                    id = movie.id,
                    title = movie.title,
                    imageUrl = formingUrl(baseUrl, posterSize, movie.posterPath),
                    age = if (movie.adult) 16 else 13,
                    genres = genres.filter { genre ->
                        movie.genreIds.contains(genre.id)
                    },
                    runningTime = -1,
                    reviewCount = movie.voteCount,
                    rating = movie.voteAverage,
                    isLiked = false
                )
            }
            .toList()
    }
}