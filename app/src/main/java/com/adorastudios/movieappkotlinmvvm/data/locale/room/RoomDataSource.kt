package com.adorastudios.movieappkotlinmvvm.data.locale.room

import com.adorastudios.movieappkotlinmvvm.data.locale.LocaleDataSource
import com.adorastudios.movieappkotlinmvvm.data.locale.MovieDatabase
import com.adorastudios.movieappkotlinmvvm.model.Actor
import com.adorastudios.movieappkotlinmvvm.model.Genre
import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview

class RoomDataSource(private val db: MovieDatabase) : LocaleDataSource {
    override suspend fun loadMovies(): List<MoviePreview> {
        return db.getMovieDao().getMovies().map { movie ->
            val genres = db.getMovieDao().getGenresByMovieId(movie.movie.id.toInt()).map {
                Genre(it.id, it.name)
            }
            MoviePreview(
                id = movie.movie.id,
                title = movie.movie.title,
                imageUrl = movie.movie.image,
                age = movie.movie.age,
                genres = genres,
                runningTime = movie.movie.time,
                reviewCount = movie.movie.reviewCount,
                rating = movie.movie.rating,
                isLiked = movie.movie.like
            )
        }
    }

    override suspend fun insertMovies(moviePreviewFromNetwork: List<MoviePreview>) {
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
        val genres = moviePreviewFromNetwork.map { movie ->
            movie.genres
        }.flatten().distinct().map { genre ->
            GenreDB(
                id = genre.id,
                name = genre.name
            )
        }
        val genreMovies = moviePreviewFromNetwork.map { movie ->
            movie.genres.map { genre ->
                GenreMovieDB(
                    movie = movie.id.toInt(),
                    genre = genre.id
                )
            }
        }.flatten()

        db.getMovieDao().insertAsTransaction(
            movies,
            genres,
            genreMovies
        )
    }

    override suspend fun loadMovie(movieId: Long): List<MovieDetails> {
        val genres = db.getMovieDao().getGenresByMovieId(movieId.toInt()).map {
            Genre(it.id, it.name)
        }
        val actorsDB = db.getMovieDao().getCastByMovieId(movieId.toInt())
        val actors = actorsDB.map {
            Actor(it.id, it.name, it.image)
        }

        return db.getMovieDao().getMovieDetails(movieId).map { movie ->
            MovieDetails(
                id = movie.details.id,
                title = movie.details.title,
                storyLine = movie.details.story,
                detailImageUrl = movie.details.image,
                age = movie.details.age,
                genres = genres,
                rating = movie.details.rating,
                isLiked = movie.details.like,
                actors = actors,
                reviewCount = movie.details.review
            )
        }
    }

    override suspend fun insertMovieDetails(movieDetailsFromNetwork: MovieDetails) {
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

        val actors = movieDetailsFromNetwork.actors.map { actor ->
            ActorDB(
                id = actor.id,
                name = actor.name,
                image = actor.imageUrl
            )
        }

        val actorMovies = movieDetailsFromNetwork.actors.map { actor ->
            ActorMovieDB(
                actor = actor.id,
                movie = movieDetailsFromNetwork.id.toInt()
            )
        }

        db.getMovieDao().insertAsTransaction(
            movie,
            actors,
            actorMovies
        )
    }
}