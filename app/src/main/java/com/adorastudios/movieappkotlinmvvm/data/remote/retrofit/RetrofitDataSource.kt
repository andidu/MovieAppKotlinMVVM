package com.adorastudios.movieappkotlinmvvm.data.remote.retrofit

import com.adorastudios.movieappkotlinmvvm.data.remote.RemoteDataSource
import com.adorastudios.movieappkotlinmvvm.data.remote.retrofit.response.ImageResponse
import com.adorastudios.movieappkotlinmvvm.model.Actor
import com.adorastudios.movieappkotlinmvvm.model.Genre
import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview

class RetrofitDataSource (private val api: MovieApiService) : RemoteDataSource {

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
        val genres = api.loadGenres().genres
        return api.loadUpcoming(page = 1).results.map { movie ->
            MoviePreview(
                id = movie.id,
                title = movie.title,
                imageUrl = formingUrl(baseUrl, posterSize, movie.posterPath),
                age = if (movie.adult) 16 else 13,
                genres = genres.filter { genreResponse ->
                    movie.genreIds.contains(genreResponse.id)
                }.map { genreResponse ->
                    Genre(genreResponse.id, genreResponse.name)
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
            actors = api.loadMovieCast(id).cast.map {
                Actor(it.id, it.name, formingUrl(baseUrl, posterSize, it.profilePath))
            },
            reviewCount = details.voteCount
        )
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

    private fun formingUrl(url: String?, size: String?, path: String?) : String? {
        return if (url == null || path == null) {
            null
        } else {
            url.plus(size.takeUnless { it.isNullOrEmpty() }?: DEFAULT_SIZE)
                .plus(path)
        }
    }
}