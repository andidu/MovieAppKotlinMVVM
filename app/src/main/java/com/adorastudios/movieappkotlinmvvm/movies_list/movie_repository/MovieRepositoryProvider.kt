package com.adorastudios.movieappkotlinmvvm.movies_list.movie_repository

interface MovieRepositoryProvider {
    fun provideMovieRepository(): MovieRepository
}