package com.adorastudios.movieappkotlinmvvm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.work.*
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
import com.adorastudios.movieappkotlinmvvm.notifications.Notifications
import com.adorastudios.movieappkotlinmvvm.notifications.NotificationsImpl
import com.adorastudios.movieappkotlinmvvm.worker.RefreshDatabaseWorker

class MainActivity : AppCompatActivity(),
    MovieRepositoryProvider,
    MoviesListFragment.ClickMovieListener,
    MovieDetailsFragment.ClickBackListener {

    private lateinit var networkModule: NetworkModule
    private lateinit var remoteDataSource: RemoteDataSource
    private lateinit var database: MovieDatabase
    private lateinit var localeDataSource: LocaleDataSource
    private lateinit var movieRepository: MovieRepositoryImpl
    private val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkModule = NetworkModule()
        remoteDataSource = RetrofitDataSource(networkModule.api)
        database = MovieDatabase.create(this)
        localeDataSource = RoomDataSource(database)
        movieRepository = MovieRepositoryImpl(remoteDataSource, localeDataSource)

        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val work = PeriodicWorkRequestBuilder<RefreshDatabaseWorker>(8,
            java.util.concurrent.TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        val workManager = WorkManager.getInstance(this)
        workManager.enqueueUniquePeriodicWork("name1", ExistingPeriodicWorkPolicy.REPLACE, work)

        if (savedInstanceState == null) {
            toMovieList()
            intent?.let {
                handleIntent(it)
            }
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

    private fun toMovieDetails(id: Long, card: View) {
        supportFragmentManager.beginTransaction()
            .addSharedElement(card, getString(R.string.transition_details))
            .replace(
                R.id.fragment,
                MovieDetailsFragment.newInstance(id),
                MoviesListFragment::class.java.simpleName
            )
            .addToBackStack("transition:${MovieDetailsFragment::class.java.simpleName}")
            .commit()
    }

    private fun toMovieDetails(id: Long) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment,
                MovieDetailsFragment.newInstance(id),
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

    override fun onClick(movie: MoviePreview, card: View) {
        toMovieDetails(movie.id, card)
    }

    override fun onClick() {
        fromMovieDetails()
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_VIEW -> {
                val id = intent.data?.lastPathSegment?.toLongOrNull()
                id?.let {
                    NotificationsImpl(context).dismissNotification(id)
                    toMovieDetails(id)
                }
            }
        }
    }
}