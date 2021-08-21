package com.adorastudios.movieappkotlinmvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adorastudios.movieappkotlinmvvm.data.locale.LocaleDataSource
import com.adorastudios.movieappkotlinmvvm.data.locale.MovieDatabase
import com.adorastudios.movieappkotlinmvvm.data.locale.room.RoomDataSource
import com.adorastudios.movieappkotlinmvvm.data.remote.RemoteDataSource
import com.adorastudios.movieappkotlinmvvm.data.remote.network_module.NetworkModule
import com.adorastudios.movieappkotlinmvvm.data.remote.retrofit.RetrofitDataSource
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview
import com.adorastudios.movieappkotlinmvvm.movie_details.MovieDetailsFragment
import com.adorastudios.movieappkotlinmvvm.movies_list.MoviesListFragment
import com.adorastudios.movieappkotlinmvvm.data.repository.MovieRepository
import com.adorastudios.movieappkotlinmvvm.data.repository.MovieRepositoryImpl
import com.adorastudios.movieappkotlinmvvm.data.repository.MovieRepositoryProvider

class MainActivity : AppCompatActivity(),
    MovieRepositoryProvider,
    MoviesListFragment.ClickMovieListener,
    MovieDetailsFragment.ClickBackListener {

    private lateinit var networkModule: NetworkModule
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var database: MovieDatabase
    private lateinit var localeDataSource: LocaleDataSource
    private lateinit var movieRepository: MovieRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkModule = NetworkModule()
        remoteDataSource = RetrofitDataSource(networkModule.api)
        database = MovieDatabase.create(this)
        localeDataSource = RoomDataSource(database)
        movieRepository = MovieRepositoryImpl(remoteDataSource, localeDataSource)

        if (savedInstanceState == null) {
            toMovieList()
        }
    }

    private fun toMovieList() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment,
                MoviesListFragment.newInstance(),
                MoviesListFragment::class.java.simpleName
            )
            .addToBackStack("transition:${MoviesListFragment::class.java.simpleName}")
            .commit()
    }

    private fun toMovieDetails(movie: MoviePreview) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment,
                MovieDetailsFragment.newInstance(movie.id),
                MoviesListFragment::class.java.simpleName
            )
            .addToBackStack("transition:${MovieDetailsFragment::class.java.simpleName}")
            .commit()
    }

    private fun fromMovieDetails() {
        supportFragmentManager.popBackStack()
    }

    override fun provideMovieRepository(): MovieRepository {
        return movieRepository
    }

    override fun onClick(movie: MoviePreview) {
        toMovieDetails(movie)
    }

    override fun onClick() {
        fromMovieDetails()
    }
}