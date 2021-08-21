package com.adorastudios.movieappkotlinmvvm.data.locale.room

import androidx.room.Embedded
import androidx.room.Relation
import com.adorastudios.movieappkotlinmvvm.data.locale.MovieDbContract

class MovieDetailsWithGenresAndActors(
    @Embedded
    val details: MovieDetailsDB,
    @Relation(
        parentColumn = MovieDbContract.MovieDetailsTable.COLUMN_NAME_ID,
        entityColumn = MovieDbContract.MovieDetailsTable.COLUMN_NAME_ID
    )
    val genres: List<GenreDB>,
    @Relation(
        parentColumn = MovieDbContract.MovieDetailsTable.COLUMN_NAME_ID,
        entityColumn = MovieDbContract.MovieDetailsTable.COLUMN_NAME_ID
    )
    val actors: List<ActorDB>
)