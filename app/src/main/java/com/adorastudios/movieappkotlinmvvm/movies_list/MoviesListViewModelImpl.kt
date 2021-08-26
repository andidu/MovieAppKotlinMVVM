package com.adorastudios.movieappkotlinmvvm.movies_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.adorastudios.movieappkotlinmvvm.data.Failure
import com.adorastudios.movieappkotlinmvvm.data.Result
import com.adorastudios.movieappkotlinmvvm.data.Success
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview
import com.adorastudios.movieappkotlinmvvm.data.repository.MovieRepository
import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import com.adorastudios.movieappkotlinmvvm.movies_list.MoviesResult.ErrorResult
import com.adorastudios.movieappkotlinmvvm.notifications.Notifications
import com.adorastudios.movieappkotlinmvvm.scheduler.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.cast
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.rxObservable
import java.util.concurrent.TimeUnit
import kotlin.random.Random

@ExperimentalCoroutinesApi
class MoviesListViewModelImpl(
    schedulerProvider: SchedulerProvider,
    private val repository: MovieRepository,
    private val notifications: Notifications
) : MoviesListViewModel() {

    private val moviesList: MutableLiveData<MoviesListViewState> = MutableLiveData()
    override val movies: MutableLiveData<MoviesListViewState> get() = moviesList

    private val loadedFrom: MutableLiveData<LoadedFrom> = MutableLiveData()
    override val loaded: MutableLiveData<LoadedFrom> get() = loadedFrom

    override val queryInput = PublishSubject.create<String>()

    override val searchResultOutput = MutableLiveData<MoviesResult>()
    override val searchStateOutput = MutableLiveData<SearchState>()

    private val subscriptions = CompositeDisposable()

    private val random = Random(System.currentTimeMillis())
    private var notification: Boolean = true

    override val searchActive: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        queryInput
            .doOnEach {
                if (it.value?.isNotEmpty() == true) startSearch()
                searchStateOutput.postValue(SearchState.Loading)
            }
            .debounce(DEBOUNCE_DELAY_TIME_MS, TimeUnit.MILLISECONDS, schedulerProvider.time())
            .switchMap(::doSearch)
            .observeOn(schedulerProvider.ui())
            .doOnEach { searchStateOutput.value = SearchState.Ready }
            .subscribeBy(
                onNext = searchResultOutput::setValue,
                onError = { searchResultOutput.value = MoviesResult.TerminalError })
            .addTo(subscriptions)
        init()
    }

    private fun init() {
        loadedFrom.postValue(LoadedFrom.Nowhere)
        loadMovies()
        notifications.initialize()
        notification = true
    }

    private fun doSearch(query: String): Observable<MoviesResult> {
        if (searchActive.value == false) return Observable.just(MoviesResult.Ignore)
        return if (query.isEmpty()) {
            Observable.just(MoviesResult.EmptyQuery)
        } else {
            rxObservable { send(repository.searchMovies(query)) }
                .map {
                    if (it.isEmpty()) {
                        MoviesResult.EmptyResult
                    } else {
                        MoviesResult.ValidResult(it)
                    }
                }
                .cast<MoviesResult>()
                .onErrorReturn(::ErrorResult)
        }
    }

    override fun stopSearch() {
        if (searchStateOutput.value is SearchState.Ready) {
            searchActive.postValue(false)
        }
    }

    override fun startSearch() {
        searchActive.postValue(true)
    }

    //load movies:
    private fun loadMovies() {
        viewModelScope.launch {
            handleMoviePreviewRemoteResult(repository.loadMoviesRemote())
        }
    }

    private fun handleMoviePreviewRemoteResult(result: Result<List<MoviePreview>>) {
        when (result) {
            is Success -> {
                moviesList.postValue(MoviesListViewState.MoviesLoaded(result.data))
                loadedFrom.postValue(LoadedFrom.FromRemote)
            }
            is Failure -> loadMoviesFromDB()
        }
    }

    private fun loadMoviesFromDB() {
        viewModelScope.launch {
            handleMoviePreviewLocaleResult(repository.loadMoviesLocale())
        }
    }

    private fun handleMoviePreviewLocaleResult(result: Result<List<MoviePreview>>) {
        when (result) {
            is Success -> {
                moviesList.postValue(MoviesListViewState.MoviesLoaded(result.data))
                loadedFrom.postValue(LoadedFrom.FromLocale)
            }
            is Failure -> moviesList.postValue(MoviesListViewState.FailedToLoad(result.exception))
        }
    }

    override fun finishLoading() {
        loadedFrom.postValue(LoadedFrom.Nowhere)
    }

    //show notification:
    override fun showNotification(list: List<MoviePreview>) :Boolean {
        if (!notification || list.isEmpty()) return false
        val randomId = list[random.nextInt(list.size)].id
        viewModelScope.launch {
            handleMovieDetailsResult(repository.loadMovieRemote(randomId))
        }
        return true
    }

    private fun handleMovieDetailsResult(result: Result<MovieDetails>) {
        if (result is Success) {
            notifications.showNotification(result.data)
        }
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}