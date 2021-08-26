package com.adorastudios.movieappkotlinmvvm.movies_list

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adorastudios.movieappkotlinmvvm.R
import com.adorastudios.movieappkotlinmvvm.data.repository.MovieRepositoryProvider
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview
import com.adorastudios.movieappkotlinmvvm.notifications.NotificationsImpl
import com.adorastudios.movieappkotlinmvvm.scheduler.SchedulerProvider
import com.jakewharton.rxbinding3.widget.textChanges
import kotlinx.coroutines.ExperimentalCoroutinesApi
import pl.droidsonroids.gif.GifImageView

/**
 * Fragment to be used on the main screen to provide user with list of movies
 * Content shall be shown in two columns for vertical layout and 5 for horizontal layout
 */
class MoviesListFragment : Fragment() {

    private var listener: ClickMovieListener? = null
    private val viewModelImpl: MoviesListViewModel by viewModels {
        MoviesListViewModelFactory(
            SchedulerProvider.Impl(),
            (requireActivity() as MovieRepositoryProvider).provideMovieRepository(),
            NotificationsImpl(requireContext())
        )
    }
    private lateinit var gifImage: GifImageView
    private lateinit var svgImage: ImageView
    private lateinit var separatorView: View
    private lateinit var editText: EditText
    private lateinit var moviesList: RecyclerView
    private lateinit var moviesSearch: RecyclerView

    private lateinit var adapterList: MoviesListAdapter
    private lateinit var adapterSearch: MoviesListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ClickMovieListener) {
            listener = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies_list, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }

        this.gifImage = view.findViewById(R.id.imageViewSearchAnim)
        this.svgImage = view.findViewById(R.id.imageViewSearchStat)
        this.svgImage.setOnClickListener {
            viewModelImpl.stopSearch()
        }
        this.separatorView = view.findViewById(R.id.viewSearch)
        this.moviesList = view.findViewById(R.id.recyclerViewMovies)
        this.moviesSearch = view.findViewById(R.id.recyclerViewMoviesSearch)

        moviesList.setHasFixedSize(true)
        moviesList.layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(context, 5)
            } else {
                GridLayoutManager(context, 2)
            }

        val adapter = MoviesListAdapter { movieData, card ->
            listener?.onClick(movieData, card)
        }

        moviesList.adapter = adapter
        this.adapterList = adapter

        moviesSearch.setHasFixedSize(true)
        moviesSearch.layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                GridLayoutManager(context, 5)
            } else {
                GridLayoutManager(context, 2)
            }

        val adapter2 = MoviesListAdapter { movieData, card ->
            listener?.onClick(movieData, card)
        }

        moviesSearch.adapter = adapter2
        this.adapterSearch = adapter2

        //viewModel.init()
        viewModelImpl.loaded.observe(this.viewLifecycleOwner, ::handleLoading)
        viewModelImpl.movies.observe(this.viewLifecycleOwner, ::handleMovies)

        editText = view.findViewById(R.id.editTextSearch)
        editText.textChanges()
            .map { it.toString() }
            .distinctUntilChanged()
            .subscribe(viewModelImpl.queryInput)

        viewModelImpl.searchResultOutput.observe(this.viewLifecycleOwner, ::handleMoviesListResult)
        viewModelImpl.searchStateOutput.observe(this.viewLifecycleOwner, ::handleLoadingState)
        viewModelImpl.searchActive.observe(this.viewLifecycleOwner, ::handleSearch)
    }

    private fun handleLoading(loadedFrom: LoadedFrom) {
        if (loadedFrom is LoadedFrom.FromRemote) {
            Toast.makeText(
                requireContext(),
                getString(R.string.movie_list_updated),
                Toast.LENGTH_SHORT
            ).show()
            viewModelImpl.finishLoading()
        } else if (loadedFrom is LoadedFrom.FromLocale) {
            Toast.makeText(
                requireContext(),
                getString(R.string.unable_to_update_movie_list),
                Toast.LENGTH_SHORT
            ).show()
            viewModelImpl.finishLoading()
        }
    }

    private fun handleMovies(moviesListViewState: MoviesListViewState) {
        if (moviesListViewState is MoviesListViewState.MoviesLoaded) {
            adapterList.submitList(moviesListViewState.movies)
            viewModelImpl.showNotification(moviesListViewState.movies)
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.error_while_loading_movies),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun handleMoviesListResult(result: MoviesResult) {
        when (result) {
            is MoviesResult.ValidResult -> {
                adapterSearch.submitList(result.result)
            }
            is MoviesResult.ErrorResult -> {
                adapterSearch.submitList(emptyList())
                Toast.makeText(
                    requireContext(),
                    getString(R.string.search_error_result),
                    Toast.LENGTH_SHORT
                ).show()
            }
            is MoviesResult.EmptyResult -> {
                adapterSearch.submitList(emptyList())
                Toast.makeText(
                    requireContext(),
                    getString(R.string.search_empty_result),
                    Toast.LENGTH_SHORT
                ).show()
            }
            is MoviesResult.EmptyQuery -> {
                adapterSearch.submitList(emptyList())
                Toast.makeText(
                    requireContext(),
                    getString(R.string.search_empty_query),
                    Toast.LENGTH_SHORT
                ).show()
            }
            is MoviesResult.Ignore -> {
                adapterSearch.submitList(emptyList())
            }
            is MoviesResult.TerminalError -> {
                adapterSearch.submitList(emptyList())
                Toast.makeText(requireContext(), getString(R.string.search_terminal_error), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun handleLoadingState(state: SearchState) {
        if (viewModelImpl.searchActive.value == false) return
        when (state) {
            is SearchState.Loading -> {
                svgImage.visibility = View.GONE
                gifImage.visibility = View.VISIBLE
                separatorView.visibility = View.VISIBLE
                moviesList.visibility = View.GONE
                moviesSearch.visibility = View.VISIBLE
            }
            is SearchState.Ready -> {
                svgImage.visibility = View.VISIBLE
                gifImage.visibility = View.GONE
                separatorView.visibility = View.VISIBLE
                moviesList.visibility = View.GONE
                moviesSearch.visibility = View.VISIBLE
            }
        }
    }

    private fun handleSearch(b: Boolean) {
        if (!b) {
            svgImage.visibility = View.GONE
            gifImage.visibility = View.GONE
            separatorView.visibility = View.GONE
            editText.setText("")
            moviesList.visibility = View.VISIBLE
            moviesSearch.visibility = View.GONE
        }
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    interface ClickMovieListener {
        fun onClick(movie: MoviePreview, card: View)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MoviesListFragment()
    }
}