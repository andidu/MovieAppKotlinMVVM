package com.adorastudios.movieappkotlinmvvm.data.locale.room

import com.adorastudios.movieappkotlinmvvm.data.locale.LocaleDataSource
import com.adorastudios.movieappkotlinmvvm.data.locale.MovieDatabase
import com.adorastudios.movieappkotlinmvvm.model.Actor
import com.adorastudios.movieappkotlinmvvm.model.Genre
import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview

class RoomDataSource(private val db: MovieDatabase) : LocaleDataSource {
    override suspend fun loadMovies(): List<MoviePreview> {
        return db.getMoviePreviewsDao().getMovies().map { movie ->
            MoviePreview(
                id = movie.movie.id,
                title = movie.movie.title,
                imageUrl = movie.movie.image,
                age = movie.movie.age,
                genres = movie.genres.map {
                    Genre(it.id, it.name)
                },
                runningTime = movie.movie.time,
                reviewCount = movie.movie.reviewCount,
                rating = movie.movie.rating,
                isLiked = movie.movie.like
            )
        }
    }

    override fun insertMovies(moviePreviewFromNetwork: List<MoviePreview>) {
        val movies = moviePreviewFromNetwork.map { movie ->
            MoviePreviewsDB(
                id = movie.id,
                title = movie.title,
                image = movie.imageUrl.orEmpty(),
                age = movie.age,
                time = movie.runningTime,
                reviewCount = movie.reviewCount,
                rating = movie.rating,
                like = movie.isLiked
            )
        }
        db.getMoviePreviewsDao().insertMovies(movies)
    }

    override suspend fun loadMovie(movieId: Long): List<MovieDetails> {
        return db.getMovieDetailsDao().getMovieDetails(movieId).map { movie ->
            MovieDetails(
                id = movie.details.id,
                title = movie.details.title,
                storyLine = movie.details.story,
                detailImageUrl = movie.details.image,
                age = movie.details.age,
                genres = movie.genres.map {
                    Genre(it.id, it.name)
                },
                rating = movie.details.rating,
                isLiked = movie.details.like,
                actors = movie.actors.map {
                    Actor(it.id, it.name, it.image)
                },
                reviewCount = movie.details.review
            )
        }
    }

    override fun insertMovieDetails(movieDetailsFromNetwork: MovieDetails) {
        val movie = movieDetailsFromNetwork.let { movie ->
            MovieDetailsDB(
                id = movie.id,
                title = movie.title,
                story = movie.storyLine,
                image = movie.detailImageUrl.orEmpty(),
                age = movie.age,
                rating = movie.rating,
                like = movie.isLiked,
                review = movie.reviewCount
            )
        }
        db.getMovieDetailsDao().insertMovieDetails(movie)
    }
}