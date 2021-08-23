package com.adorastudios.movieappkotlinmvvm.data.locale.room

import androidx.room.*

@Dao
interface MovieDao {
    //Insert:
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetails(item: MovieDetailsDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(items: Iterable<MoviePreviewsDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActors(actors: List<ActorDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<GenreDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActorMovie(actorMovieDB: List<ActorMovieDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenreMovie(genreMovieDB: List<GenreMovieDB>)

    @Transaction
    suspend fun insertAsTransaction(
        movie: MovieDetailsDB,
        actors: List<ActorDB>,
        actorsMovie: List<ActorMovieDB>
    ) {
        insertMovieDetails(movie)
        insertActors(actors)
        insertActorMovie(actorsMovie)
    }

    @Transaction
    suspend fun insertAsTransaction(
        movies: List<MoviePreviewsDB>,
        genres: List<GenreDB>,
        genresMovie: List<GenreMovieDB>
    ) {
        insertMovies(movies)
        insertGenres(genres)
        insertGenreMovie(genresMovie)
    }

    //Select:
    @Query("SELECT * FROM movie_previews")
    fun getMovies(): List<MoviePreviewWithGenres>

    @Query("SELECT * FROM movie_details WHERE _id == :id")
    fun getMovieDetails(id: Long): List<MovieDetailsWithGenresAndActors>

    @Query("select actors._id, actors.name, actors.image from actors join actor_movie on actors._id = actor_movie.actor_id where actor_movie.movie_id = :movieId ")
    suspend fun getCastByMovieId(movieId: Int): List<ActorDB>

    @Query("select genres._id, genres.name from genres join genre_movie on genres._id = genre_movie.genre_id where genre_movie.movie_id = :movieId ")
    suspend fun getGenresByMovieId(movieId: Int): List<GenreDB>
}