package com.adorastudios.movieappkotlinmvvm.notifications

import com.adorastudios.movieappkotlinmvvm.model.MovieDetails

interface Notifications {
    fun initialize()
    fun showNotification(movieDetails: MovieDetails)
    fun dismissNotification(movieId: Long)
}