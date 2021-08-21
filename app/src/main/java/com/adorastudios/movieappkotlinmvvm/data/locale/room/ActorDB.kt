package com.adorastudios.movieappkotlinmvvm.data.locale.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.adorastudios.movieappkotlinmvvm.data.locale.MovieDbContract

@Entity(
    tableName = MovieDbContract.ActorTable.TABLE_NAME,
    primaryKeys = [MovieDbContract.ActorTable.COLUMN_NAME_ID],
    foreignKeys = [
        ForeignKey(
            entity = MoviePreviewsDB::class,
            parentColumns = [MovieDbContract.MoviePreviewsTable.COLUMN_NAME_ID],
            childColumns = [MovieDbContract.ActorTable.COLUMN_NAME_ID],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
class ActorDB(
    @ColumnInfo(name = MovieDbContract.ActorTable.COLUMN_NAME_ID)
    val id: Int,

    @ColumnInfo(name = MovieDbContract.ActorTable.COLUMN_NAME_NAME)
    val name: String,

    @ColumnInfo(name = MovieDbContract.ActorTable.COLUMN_NAME_IMAGE)
    val image: String?
)