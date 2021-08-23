package com.adorastudios.movieappkotlinmvvm.data.locale.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.adorastudios.movieappkotlinmvvm.data.locale.MovieDbContract

@Entity(
    tableName = MovieDbContract.ActorMovieTable.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = MovieDetailsDB::class,
            parentColumns = [MovieDbContract.MovieDetailsTable.COLUMN_NAME_ID],
            childColumns = [MovieDbContract.ActorMovieTable.COLUMN_NAME_MOVIE],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ActorDB::class,
            parentColumns = [MovieDbContract.ActorTable.COLUMN_NAME_ID],
            childColumns = [MovieDbContract.ActorMovieTable.COLUMN_NAME_ACTOR],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class ActorMovieDB (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = MovieDbContract.ActorMovieTable.COLUMN_NAME_ID_INTERNAL)
    val id: Int = 0,

    @ColumnInfo(name = MovieDbContract.ActorMovieTable.COLUMN_NAME_ACTOR)
    val actor: Int,

    @ColumnInfo(name = MovieDbContract.ActorMovieTable.COLUMN_NAME_MOVIE)
    val movie: Int
)