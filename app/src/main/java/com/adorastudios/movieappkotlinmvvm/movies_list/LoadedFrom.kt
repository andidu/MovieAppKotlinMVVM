package com.adorastudios.movieappkotlinmvvm.movies_list

sealed class LoadedFrom {
    object FromRemote : LoadedFrom()
    object FromLocale : LoadedFrom()
    object Nowhere : LoadedFrom()
}