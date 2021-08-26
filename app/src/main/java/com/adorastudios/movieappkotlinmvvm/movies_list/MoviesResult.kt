package com.adorastudios.movieappkotlinmvvm.movies_list

import com.adorastudios.movieappkotlinmvvm.model.MoviePreview

sealed class MoviesResult {
    data class ValidResult(val result: List<MoviePreview>) : MoviesResult()
    object EmptyResult : MoviesResult()
    object EmptyQuery : MoviesResult()
    data class ErrorResult(val e: Throwable) : MoviesResult()
    object TerminalError : MoviesResult()
    object Ignore : MoviesResult()
}