package com.adorastudios.movieappkotlinmvvm.movies_list

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adorastudios.movieappkotlinmvvm.R
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview
import com.adorastudios.movieappkotlinmvvm.data.repository.MovieRepositoryProvider
import com.adorastudios.movieappkotlinmvvm.notifications.NotificationsImpl

/**
 * Fragment to be used on the main screen to provide user with list of movies
 * Content shall be shown in two columns for vertical layout and 5 for horizontal layout
 */
class MoviesListFragment : Fragment() {

    private var listener: ClickMovieListener? = null
    private val viewModel: MoviesListViewModel by viewModels {
        MoviesListViewModelFactory(
            (requireActivity() as MovieRepositoryProvider).provideMovieRepository(),
            NotificationsImpl(requireContext())
        )
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerViewMovies)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(context, 5)
        } else {
            GridLayoutManager(context, 2)
        }

        val adapter = MoviesListAdapter { movieData, card ->
            listener?.onClick(movieData, card)
        }

        recycler.adapter = adapter

        viewModel.movies.observe(this.viewLifecycleOwner) {
            if (it is MoviesListViewState.MoviesLoaded) {
                adapter.submitList(it.movies)
                viewModel.showNotification(it.movies)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.movie_list_updated),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_while_loading_movies),
                    Toast.LENGTH_LONG
                ).show()
            }
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