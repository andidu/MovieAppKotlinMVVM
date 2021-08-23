package com.adorastudios.movieappkotlinmvvm.data.locale.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.adorastudios.movieappkotlinmvvm.data.locale.MovieDbContract

@Entity(
    tableName = MovieDbContract.ActorTable.TABLE_NAME,
    primaryKeys = [MovieDbContract.ActorTable.COLUMN_NAME_ID]
)
class ActorDB(
    @ColumnInfo(name = MovieDbContract.ActorTable.COLUMN_NAME_ID)
    val id: Int,

    @ColumnInfo(name = MovieDbContract.ActorTable.COLUMN_NAME_NAME)
    val name: String,

    @ColumnInfo(name = MovieDbContract.ActorTable.COLUMN_NAME_IMAGE)
    val image: String?
)