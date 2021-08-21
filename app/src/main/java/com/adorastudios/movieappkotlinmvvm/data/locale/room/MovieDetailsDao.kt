package com.adorastudios.movieappkotlinmvvm.data.locale.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview

@Dao
interface MovieDetailsDao {
    @Query("SELECT * FROM movie_details WHERE _id == :id")
    fun getMovieDetails(id: Long): List<MovieDetailsWithGenresAndActors>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieDetails(item: MovieDetailsDB)
}