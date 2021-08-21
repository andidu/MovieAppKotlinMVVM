package com.adorastudios.movieappkotlinmvvm.movie_details

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.adorastudios.movieappkotlinmvvm.R
import com.adorastudios.movieappkotlinmvvm.model.Genre
import com.adorastudios.movieappkotlinmvvm.model.MovieDetails
import com.adorastudios.movieappkotlinmvvm.data.repository.MovieRepositoryProvider
import com.google.android.material.transition.MaterialContainerTransform
import kotlin.math.roundToInt


/**
 * Fragment to be used on main screen for showing movie's details
 *
 */
class MovieDetailsFragment : Fragment() {

    private var listener: ClickBackListener? = null
    private val viewModel: MovieDetailsViewModel by viewModels {
        MovieDetailsViewModelFactory(
            (requireActivity() as MovieRepositoryProvider).provideMovieRepository()
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ClickBackListener) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 300
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(ContextCompat.getColor(requireContext(), R.color.black))

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerViewActors)
        recycler.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recycler.adapter = ActorsListAdapter()

        view.findViewById<TextView>(R.id.textViewBack).setOnClickListener { listener?.onClick() }

        val id = arguments?.getSerializable(PARAM_MOVIE_ID) as? Long ?: return
        viewModel.loadMovie(id)

        viewModel.movie.observe(this.viewLifecycleOwner) { state ->
            when (state) {
                is MovieDetailsState.MovieLoaded -> bindUI(view, state.movie)
                is MovieDetailsState.MovieNotLoaded -> showMovieNotFound()
                is MovieDetailsState.MovieError -> showMovieError()
            }
        }

    }

    private fun bindUI(view: View, movie: MovieDetails) {
        updateMovieInfo(movie)
        val adapter =
            view.findViewById<RecyclerView>(R.id.recyclerViewActors).adapter as ActorsListAdapter
        adapter.submitList(movie.actors)
    }

    private fun updateMovieInfo(movie: MovieDetails) {
        view?.findViewById<TextView>(R.id.textViewMovie)?.text = movie.title
        view?.findViewById<TextView>(R.id.textViewReviewAmount)?.text = getString(
            if (movie.reviewCount == 1) { R.string.review_x } else {
                R.string.reviews_x }, movie.reviewCount
        )
        view?.findViewById<TextView>(R.id.textViewStoryline)?.text = movie.storyLine
        view?.findViewById<TextView>(R.id.textViewAge)?.text = getString(R.string.age_more, movie.age)
        var genres = ""
        for (g: Genre in movie.genres) {
            genres = genres + g.name + " "
        }
        view?.findViewById<TextView>(R.id.textViewGenres)?.text = genres

        view?.findViewById<ImageView>(R.id.imageViewMovie)?.load(movie.detailImageUrl)
        val starImages = listOf<ImageView?>(
            view?.findViewById(R.id.imageViewStar1),
            view?.findViewById(R.id.imageViewStar2),
            view?.findViewById(R.id.imageViewStar3),
            view?.findViewById(R.id.imageViewStar4),
            view?.findViewById(R.id.imageViewStar5)
        )

        starImages.forEachIndexed { index, image ->
            image?.let {
                if ((movie.rating / 2.0).roundToInt() > index) {
                    image.setImageResource(R.drawable.ic_star_icon_pink)
                } else {
                    image.setImageResource(R.drawable.ic_star_icon)
                }
            }
        }
    }

    private fun showMovieError() {
        Toast.makeText(requireContext(), R.string.error_movie, Toast.LENGTH_LONG).show()
    }

    private fun showMovieNotFound() {
        Toast.makeText(requireContext(), R.string.error_movie_not_found, Toast.LENGTH_LONG).show()
    }

    override fun onDetach() {
        listener = null
        super.onDetach()
    }

    interface ClickBackListener {
        fun onClick()
    }

    companion object {
        private const val PARAM_MOVIE_ID = "movie_id"

        @JvmStatic
        fun newInstance(movieId: Long) = MovieDetailsFragment().also {
            val args = bundleOf(PARAM_MOVIE_ID to movieId)
            it.arguments = args

        }
    }
}