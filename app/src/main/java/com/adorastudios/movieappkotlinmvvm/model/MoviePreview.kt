package com.adorastudios.movieappkotlinmvvm.model

data class MoviePreview (
    val id: Long,
    val title: String,
    val imageUrl: String?,
    val age: Int,
    val genres: List<Genre>,
    val runningTime: Int,
    val reviewCount: Int,
    val rating: Double,
    val isLiked: Boolean
)