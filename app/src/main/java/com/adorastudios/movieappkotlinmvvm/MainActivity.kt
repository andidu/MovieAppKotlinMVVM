package com.adorastudios.movieappkotlinmvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    MovieDetailsFragment.ClickBackListener{

    private val networkModule = NetworkModule()
    private val remoteDataSource = RetrofitDataSource(networkModule.api)
    private val movieRepository: MovieRepositoryImpl = MovieRepositoryImpl(remoteDataSource)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            toMovieList()
        }
    }

    private fun toMovieList() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment,
                MoviesListFragment.newInstance(),
                MoviesListFragment::class.java.simpleName)
            .addToBackStack("transition:${MoviesListFragment::class.java.simpleName}")
            .commit()
    }

    private fun toMovieDetails(movie: MoviePreview) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment,
                MovieDetailsFragment.newInstance(movie.id),
                MoviesListFragment::class.java.simpleName)
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