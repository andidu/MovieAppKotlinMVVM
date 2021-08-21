package com.adorastudios.movieappkotlinmvvm.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.adorastudios.movieappkotlinmvvm.data.locale.LocaleDataSource
import com.adorastudios.movieappkotlinmvvm.data.remote.RemoteDataSource

class RefreshDatabaseWorker (
    private val remoteDataSource: RemoteDataSource,
    private val localeDataSource: LocaleDataSource,
    context: Context,
    parameters: WorkerParameters
) : CoroutineWorker(context, parameters) {
    override suspend fun doWork(): Result {
        val movieFromNetwork = remoteDataSource.loadMovies()
        localeDataSource.insertMovies(movieFromNetwork)
        return Result.success()
    }
}