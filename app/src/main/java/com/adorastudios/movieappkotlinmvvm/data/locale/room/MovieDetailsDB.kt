package com.adorastudios.movieappkotlinmvvm.data.locale.room

import androidx.room.*
import com.adorastudios.movieappkotlinmvvm.data.locale.MovieDbContract

@Entity(
    tableName = MovieDbContract.MovieDetailsTable.TABLE_NAME,
    primaryKeys = [MovieDbContract.MovieDetailsTable.COLUMN_NAME_ID],
    indices = [Index(MovieDbContract.MovieDetailsTable.COLUMN_NAME_ID)]
)
class MovieDetailsDB(
    @ColumnInfo(name = MovieDbContract.MovieDetailsTable.COLUMN_NAME_ID)
    val id: Long = 0,

    @ColumnInfo(name = MovieDbContract.MovieDetailsTable.COLUMN_NAME_TITLE)
    val title: String,

    @ColumnInfo(name = MovieDbContract.MovieDetailsTable.COLUMN_NAME_STORY)
    val story: String,

    @ColumnInfo(name = MovieDbContract.MovieDetailsTable.COLUMN_NAME_IMAGE)
    val image: String,

    @ColumnInfo(name = MovieDbContract.MovieDetailsTable.COLUMN_NAME_AGE)
    val age: Int,

    @ColumnInfo(name = MovieDbContract.MovieDetailsTable.COLUMN_NAME_LIKE)
    val like: Boolean,

    @ColumnInfo(name = MovieDbContract.MovieDetailsTable.COLUMN_NAME_RATING)
    val rating: Double,

    @ColumnInfo(name = MovieDbContract.MovieDetailsTable.COLUMN_NAME_REVIEWS)
    val review: Int
)