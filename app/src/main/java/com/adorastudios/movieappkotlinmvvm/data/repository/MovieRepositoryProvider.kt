package com.adorastudios.movieappkotlinmvvm.data.repository

interface MovieRepositoryProvider {
    fun provideMovieRepository(): MovieRepository
}