package com.adorastudios.movieappkotlinmvvm.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.adorastudios.movieappkotlinmvvm.MainActivity
import com.adorastudios.movieappkotlinmvvm.R
import com.adorastudios.movieappkotlinmvvm.model.MovieDetails

class NotificationsImpl(private val context: Context) : Notifications {

    companion object {
        const val CHANNEL_NAME = "top_movies"
        const val REQUEST_CONTENT = 1
        const val MOVIE_TAG = "movie"
    }

    private val notificationManagerCompat: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    override fun initialize() {
        if (notificationManagerCompat.getNotificationChannel(CHANNEL_NAME) == null) {
            notificationManagerCompat.createNotificationChannel(
                NotificationChannelCompat.Builder(CHANNEL_NAME, NotificationManagerCompat.IMPORTANCE_HIGH)
                    .setName("Movie suggestions")
                    .setDescription("A notification with a suggested movie will pop up every time you open an app")
                    .build()
            )
        }
    }

    @WorkerThread
    override fun showNotification(movieDetails: MovieDetails) {
        val contentUri = ("https://android.example.com/chat/${movieDetails.id}").toUri()
        val builder = NotificationCompat.Builder(context, CHANNEL_NAME)
            .setContentTitle("Would you like to watch a movie?")
            .setContentText(movieDetails.title)
            .setSmallIcon(R.drawable.ic_baseline_local_movies_24)
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(movieDetails.title + "\n" + movieDetails.storyLine)
            )
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    REQUEST_CONTENT,
                    Intent(context, MainActivity::class.java)
                        .setAction(Intent.ACTION_VIEW)
                        .setData(contentUri),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
        notificationManagerCompat.notify(MOVIE_TAG, movieDetails.id.toInt(), builder.build())
    }

    override fun dismissNotification(movieId: Long) {
        notificationManagerCompat.cancel(MOVIE_TAG, movieId.toInt())
    }
}