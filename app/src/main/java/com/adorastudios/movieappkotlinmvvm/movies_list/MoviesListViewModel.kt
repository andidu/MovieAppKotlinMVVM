package com.adorastudios.movieappkotlinmvvm.movies_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview
import io.reactivex.subjects.PublishSubject

abstract class MoviesListViewModel : ViewModel() {

    companion object {
        internal const val DEBOUNCE_DELAY_TIME_MS = 500L
    }

    abstract val movies: MutableLiveData<MoviesListViewState>
    abstract val loaded: MutableLiveData<LoadedFrom>
    abstract val queryInput: PublishSubject<String>
    abstract val searchResultOutput: MutableLiveData<MoviesResult>
    abstract val searchStateOutput: MutableLiveData<SearchState>
    abstract val searchActive: MutableLiveData<Boolean>

    abstract fun stopSearch()
    abstract fun startSearch()

    abstract fun finishLoading()
    abstract fun showNotification(list: List<MoviePreview>) :Boolean
}