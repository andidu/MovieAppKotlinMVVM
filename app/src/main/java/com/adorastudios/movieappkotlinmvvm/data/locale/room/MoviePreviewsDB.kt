package com.adorastudios.movieappkotlinmvvm.data.locale.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.adorastudios.movieappkotlinmvvm.data.locale.MovieDbContract

@Entity(
    tableName = MovieDbContract.MoviePreviewsTable.TABLE_NAME,
    primaryKeys = [MovieDbContract.MoviePreviewsTable.COLUMN_NAME_ID],
    indices = [Index(MovieDbContract.MoviePreviewsTable.COLUMN_NAME_ID)]
)
class MoviePreviewsDB (
    @ColumnInfo(name = MovieDbContract.MoviePreviewsTable.COLUMN_NAME_ID)
    val id: Long = 0,

    @ColumnInfo(name = MovieDbContract.MoviePreviewsTable.COLUMN_NAME_TITLE)
    val title: String,

    @ColumnInfo(name = MovieDbContract.MoviePreviewsTable.COLUMN_NAME_IMAGE)
    val image: String,

    @ColumnInfo(name = MovieDbContract.MoviePreviewsTable.COLUMN_NAME_AGE)
    val age: Int,

    @ColumnInfo(name = MovieDbContract.MoviePreviewsTable.COLUMN_NAME_TIME)
    val time: Int,

    @ColumnInfo(name = MovieDbContract.MoviePreviewsTable.COLUMN_NAME_REVIEWS)
    val reviewCount: Int,

    @ColumnInfo(name = MovieDbContract.MoviePreviewsTable.COLUMN_NAME_RATING)
    val rating: Double,

    @ColumnInfo(name = MovieDbContract.MoviePreviewsTable.COLUMN_NAME_LIKE)
    val like: Boolean
)