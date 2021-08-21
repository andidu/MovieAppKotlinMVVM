package com.adorastudios.movieappkotlinmvvm.data.locale.room

import androidx.room.Embedded
import androidx.room.Relation
import com.adorastudios.movieappkotlinmvvm.data.locale.MovieDbContract

class MoviePreviewWithGenres(
    @Embedded
    val movie: MoviePreviewsDB,
    @Relation(
        parentColumn = MovieDbContract.MoviePreviewsTable.COLUMN_NAME_ID,
        entityColumn = MovieDbContract.MoviePreviewsTable.COLUMN_NAME_ID
    )
    val genres: List<GenreDB>
)