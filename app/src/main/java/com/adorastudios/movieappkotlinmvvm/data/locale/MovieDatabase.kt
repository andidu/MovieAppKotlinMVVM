package com.adorastudios.movieappkotlinmvvm.data.locale

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adorastudios.movieappkotlinmvvm.data.locale.room.*

@Database(
    entities = [MoviePreviewsDB::class, MovieDetailsDB::class, ActorDB::class,
        GenreDB::class, ActorMovieDB::class, GenreMovieDB::class],
    version = 1
)
abstract class MovieDatabase : RoomDatabase() {

    companion object {

        fun create(appContext: Context): MovieDatabase =
            Room.databaseBuilder(
                appContext,
                MovieDatabase::class.java,
                MovieDbContract.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }

    abstract fun getMovieDao(): MovieDao
}