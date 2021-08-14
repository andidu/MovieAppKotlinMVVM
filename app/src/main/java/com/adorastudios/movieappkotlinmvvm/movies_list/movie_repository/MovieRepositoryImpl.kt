package com.adorastudios.movieappkotlinmvvm.movies_list.movie_repository

import com.adorastudios.movieappkotlinmvvm.data.MovieDetails
import com.adorastudios.movieappkotlinmvvm.data.MoviePreview

class MovieRepositoryImpl : MovieRepository {
    override fun loadMovies(): List<MoviePreview> {

        //TODO: change to a server thing
        val moviePreview = MoviePreview(1, "Title",
            "https://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg",
            13, listOf(), 132, 100, 4, true)
        return listOf(moviePreview)
    }

    override fun loadMovie(movieId: Int): MovieDetails? {
        TODO("Not yet implemented")
    }
}