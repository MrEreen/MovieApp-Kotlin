package com.example.movieapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.models.Movie

class MovieAdapter(
    private var movies: List<Movie>,
    private val onClick: (Movie) -> Unit,
    private val onWatchedClick: (Movie) -> Unit,
    private val showWatchedButton: Boolean = true
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View, private val onClick: (Movie) -> Unit, private val onWatchedClick: (Movie) -> Unit, private val showWatchedButton: Boolean) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.movie_title)
        private val image: ImageView = itemView.findViewById(R.id.movie_image)
        private val year: TextView = itemView.findViewById(R.id.movie_year)
        private lateinit var currentMovie: Movie

        init {
            itemView.setOnClickListener {
                onClick(currentMovie)
            }
        }

        fun bind(movie: Movie) {
            currentMovie = movie
            title.text = movie.title
            year.text = movie.year.toString()
            Glide.with(itemView.context).load(movie.image).into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(itemView, onClick, onWatchedClick, showWatchedButton)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}
