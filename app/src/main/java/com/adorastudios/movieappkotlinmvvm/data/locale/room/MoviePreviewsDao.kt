package com.adorastudios.movieappkotlinmvvm.data.locale.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview

@Dao
interface MoviePreviewsDao {
    @Query("SELECT * FROM movie_previews")
    fun getMovies(): List<MoviePreviewWithGenres>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(items: Iterable<MoviePreviewsDB>)
}