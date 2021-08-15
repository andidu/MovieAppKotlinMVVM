package com.adorastudios.movieappkotlinmvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adorastudios.movieappkotlinmvvm.data.MoviePreview
import com.adorastudios.movieappkotlinmvvm.movie_details.MovieDetailsFragment
import com.adorastudios.movieappkotlinmvvm.movies_list.MoviesListFragment
import com.adorastudios.movieappkotlinmvvm.movie_repository.MovieRepository
import com.adorastudios.movieappkotlinmvvm.movie_repository.MovieRepositoryImpl
import com.adorastudios.movieappkotlinmvvm.movie_repository.MovieRepositoryProvider

class MainActivity : AppCompatActivity(),
    MovieRepositoryProvider,
    MoviesListFragment.ClickMovieListener,
    MovieDetailsFragment.ClickBackListener{

    private val movieRepository: MovieRepositoryImpl = MovieRepositoryImpl()

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