package com.adorastudios.movieappkotlinmvvm.movie_repository

interface MovieRepositoryProvider {
    fun provideMovieRepository(): MovieRepository
}