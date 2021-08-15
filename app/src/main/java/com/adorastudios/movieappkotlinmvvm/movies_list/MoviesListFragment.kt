package com.adorastudios.movieappkotlinmvvm.movies_list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adorastudios.movieappkotlinmvvm.R
import com.adorastudios.movieappkotlinmvvm.data.MoviePreview
import com.adorastudios.movieappkotlinmvvm.movie_repository.MovieRepositoryProvider

/**
 * Fragment to be used on the main screen to provide user with list of movies
 * Content shall be shown in two columns for vertical layout and TODO for horizontal layout
 */
class MoviesListFragment : Fragment() {

    private var listener: ClickMovieListener? = null
    private val viewModel: MoviesListViewModel by viewModels { MoviesListViewModelFactory(
        (requireActivity() as MovieRepositoryProvider).provideMovieRepository())}

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

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerViewMovies)
        recycler.layoutManager = GridLayoutManager(context, 2)

        val adapter = MoviesListAdapter {movieData
            -> listener?.onClick(movieData)
        }

        recycler.adapter = adapter

        viewModel.movies.observe(this.viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }


    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    interface ClickMovieListener {
        fun onClick(movie: MoviePreview)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MoviesListFragment()
    }
}