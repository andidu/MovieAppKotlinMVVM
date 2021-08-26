package com.adorastudios.movieappkotlinmvvm.movies_list

sealed class SearchState {
    object Loading : SearchState()
    object Ready : SearchState()
}