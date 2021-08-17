package com.adorastudios.movieappkotlinmvvm.movies_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.adorastudios.movieappkotlinmvvm.R
import com.adorastudios.movieappkotlinmvvm.model.Genre
import com.adorastudios.movieappkotlinmvvm.model.MoviePreview
import kotlin.math.roundToInt

class MoviesListAdapter(private val onClickMovie: (item: MoviePreview) -> Unit) :
    ListAdapter<MoviePreview, MoviesListAdapter.MovieViewHolder>(
        DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_movie,parent, false))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.onBind(getItem(position), onClickMovie)
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val starImages: Array<ImageView> = arrayOf(
            itemView.findViewById(R.id.imageViewStar1),
            itemView.findViewById(R.id.imageViewStar2),
            itemView.findViewById(R.id.imageViewStar3),
            itemView.findViewById(R.id.imageViewStar4),
            itemView.findViewById(R.id.imageViewStar5)
        )
        private val movieName: TextView = itemView.findViewById(R.id.textViewMovie)
        private val movieGenres: TextView = itemView.findViewById(R.id.textViewGenres)
        private val reviewNumber: TextView = itemView.findViewById(R.id.textViewReviewAmount)

        private val ageText: TextView = itemView.findViewById(R.id.textViewAge)
        private val durationText: TextView = itemView.findViewById(R.id.textViewDuration)
        private val likeImage: ImageView = itemView.findViewById(R.id.imageViewLike)

        private val movieImage: ImageView = itemView.findViewById(R.id.imageViewMovie)

        fun onBind(movie: MoviePreview, onClickMovie: (item: MoviePreview) -> Unit) {
            movieName.text = movie.title
            var genres = ""
            for (g: Genre in movie.genres) {
                genres = genres + g.name + " "
            }
            movieGenres.text = genres
            reviewNumber.text = if (movie.reviewCount == 1) {
                itemView.context.getString(R.string.review_x, movie.reviewCount)
            } else {
                itemView.context.getString(R.string.reviews_x, movie.reviewCount)
            }

            ageText.text = itemView.context.getString(R.string.age_more, movie.age)
            durationText.text = itemView.context.getString(R.string._min, movie.runningTime)

            likeImage.setImageResource(
                if (movie.isLiked) {
                    R.drawable.ic_pink_like
                } else {
                    R.drawable.ic_like
                }
            )

            starImages.forEachIndexed { index, image ->
                image.let {
                    if ((movie.rating / 2.0).roundToInt() > index) {
                        image.setImageResource(R.drawable.ic_star_icon_pink)
                    } else {
                        image.setImageResource(R.drawable.ic_star_icon)
                    }
                }
            }

            movieImage.load(movie.imageUrl)

            itemView.setOnClickListener {
                onClickMovie(movie)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MoviePreview>() {
        override fun areItemsTheSame(oldItem: MoviePreview, newItem: MoviePreview): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MoviePreview, newItem: MoviePreview): Boolean {
            return oldItem == newItem
        }
    }
}