package com.adorastudios.movieappkotlinmvvm.movie_repository

import com.adorastudios.movieappkotlinmvvm.data.Actor
import com.adorastudios.movieappkotlinmvvm.data.MovieDetails
import com.adorastudios.movieappkotlinmvvm.data.MoviePreview

class MovieRepositoryImpl : MovieRepository {
    override fun loadMovies(): List<MoviePreview> {

        //TODO: change to a server thing
        val moviePreview = MoviePreview(1, "Title",
            "https://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
            13, listOf(), 132, 100, 8.8, true)
        return listOf(moviePreview)
    }

    override fun loadMovie(movieId: Int): MovieDetails? {
        return when (movieId) {
            1 -> MovieDetails(1, "Title", "Very long long long storyline",
                "https://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
            13, listOf(), 8.8, true, listOf(Actor(1, "Name",
                    "https://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg")),10003 )
            2 -> MovieDetails(2, "2Title2", "2Very long long long storyline2",
                "https://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
                13, listOf(), 2.8, true, listOf(Actor(2, "2Name2",
                    "https://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg")),10003 )
            else -> null
        }
    }
}