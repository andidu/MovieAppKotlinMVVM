package com.adorastudios.movieappkotlinmvvm.data.locale

import android.provider.BaseColumns

object MovieDbContract {

    const val DATABASE_NAME = "movies.db"

    object MoviePreviewsTable {
        const val TABLE_NAME = "movie_previews"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_IMAGE = "image"
        const val COLUMN_NAME_AGE = "age"
        const val COLUMN_NAME_TIME = "time"
        const val COLUMN_NAME_REVIEWS = "reviews"
        const val COLUMN_NAME_RATING = "rating"
        const val COLUMN_NAME_LIKE = "like"
    }

    object MovieDetailsTable {
        const val TABLE_NAME = "movie_details"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_STORY = "story_line"
        const val COLUMN_NAME_IMAGE = "image"
        const val COLUMN_NAME_AGE = "age"
        const val COLUMN_NAME_GENRES = "genres"
        const val COLUMN_NAME_RATING = "rating"
        const val COLUMN_NAME_LIKE = "like"
        const val COLUMN_NAME_ACTORS = "actors"
        const val COLUMN_NAME_REVIEWS = "reviews"
    }

    object ActorTable {
        const val TABLE_NAME = "actors"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_IMAGE = "image"
    }

    object GenreTable {
        const val TABLE_NAME = "genres"

        const val COLUMN_NAME_ID = BaseColumns._ID
        const val COLUMN_NAME_NAME = "name"
    }
}