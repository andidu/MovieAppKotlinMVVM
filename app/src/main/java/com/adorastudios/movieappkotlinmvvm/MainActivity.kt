package com.adorastudios.movieappkotlinmvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adorastudios.movieappkotlinmvvm.data.MoviePreview
import com.adorastudios.movieappkotlinmvvm.movies_list.MoviesListFragment
import com.adorastudios.movieappkotlinmvvm.movies_list.movie_repository.MovieRepository
import com.adorastudios.movieappkotlinmvvm.movies_list.movie_repository.MovieRepositoryImpl
import com.adorastudios.movieappkotlinmvvm.movies_list.movie_repository.MovieRepositoryProvider

class MainActivity : AppCompatActivity(),
    MovieRepositoryProvider,
    MoviesListFragment.ClickMovieListener {

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
            .replace(R.id.fragmentMoviesList,
                MoviesListFragment.newInstance(),
                MoviesListFragment::class.java.simpleName)
            .addToBackStack("transition:${MoviesListFragment::class.java.simpleName}")
            .commit()
    }

    private fun toMovieDetails() {

    }

    private fun fromMovieDetails() {

    }

    override fun provideMovieRepository(): MovieRepository {
        return movieRepository
    }

    override fun onClick(movie: MoviePreview) {

    }
}