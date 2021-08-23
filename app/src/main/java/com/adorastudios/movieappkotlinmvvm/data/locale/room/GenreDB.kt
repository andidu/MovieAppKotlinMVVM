package com.adorastudios.movieappkotlinmvvm.data.locale.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.adorastudios.movieappkotlinmvvm.data.locale.MovieDbContract

@Entity(
    tableName = MovieDbContract.GenreTable.TABLE_NAME,
    primaryKeys = [MovieDbContract.GenreTable.COLUMN_NAME_ID]
)
class GenreDB(
    @ColumnInfo(name = MovieDbContract.GenreTable.COLUMN_NAME_ID)
    val id: Int,

    @ColumnInfo(name = MovieDbContract.GenreTable.COLUMN_NAME_NAME)
    val name: String
)