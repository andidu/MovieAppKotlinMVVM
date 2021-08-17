package com.adorastudios.movieappkotlinmvvm.model

data class MovieDetails (
    val id: Int,
    val title: String,
    val storyLine: String,
    val detailImageUrl: String?,
    val age: Int,
    val genres: List<Genre>,
    val rating: Double,
    val isLiked: Boolean,
    val actors: List<Actor>,
    val reviewCount: Int
    )