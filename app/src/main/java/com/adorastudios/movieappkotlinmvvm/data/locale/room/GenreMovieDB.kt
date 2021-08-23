package com.adorastudios.movieappkotlinmvvm.data.locale.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.adorastudios.movieappkotlinmvvm.data.locale.MovieDbContract

@Entity(
    tableName = MovieDbContract.GenreMovieTable.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = MoviePreviewsDB::class,
            parentColumns = [MovieDbContract.MoviePreviewsTable.COLUMN_NAME_ID],
            childColumns = [MovieDbContract.GenreMovieTable.COLUMN_NAME_MOVIE],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GenreDB::class,
            parentColumns = [MovieDbContract.GenreTable.COLUMN_NAME_ID],
            childColumns = [MovieDbContract.GenreMovieTable.COLUMN_NAME_GENRE],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class GenreMovieDB (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = MovieDbContract.GenreMovieTable.COLUMN_NAME_ID_INTERNAL)
    val id: Int = 0,

    @ColumnInfo(name = MovieDbContract.GenreMovieTable.COLUMN_NAME_GENRE)
    val genre: Int,

    @ColumnInfo(name = MovieDbContract.GenreMovieTable.COLUMN_NAME_MOVIE)
    val movie: Int
)