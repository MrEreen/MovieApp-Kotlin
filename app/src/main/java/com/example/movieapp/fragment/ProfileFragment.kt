package com.example.movieapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.MovieAdapter
import com.example.movieapp.R
import com.example.movieapp.managers.WishlistManager
import com.example.movieapp.models.Movie

class ProfileFragment : Fragment() {

    private lateinit var userId: String
    private lateinit var userName: String
    private lateinit var wishlistRecyclerView: RecyclerView
    private lateinit var watchedRecyclerView: RecyclerView
    private lateinit var wishlistAdapter: MovieAdapter
    private lateinit var watchedAdapter: MovieAdapter
    private lateinit var wishlistManager: WishlistManager
    private lateinit var wishlistButton: Button
    private lateinit var watchedButton: Button
    private lateinit var wishlistCountTextView: TextView
    private lateinit var watchedCountTextView: TextView

    companion object {
        private const val ARG_USER_ID = "userId"
        private const val ARG_USER_NAME = "userName"

        fun newInstance(userId: String, userName: String): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            args.putString(ARG_USER_ID, userId)
            args.putString(ARG_USER_NAME, userName)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        userId = arguments?.getString(ARG_USER_ID) ?: ""
        userName = arguments?.getString(ARG_USER_NAME) ?: "Guest"

        val userNameTextView: TextView = view.findViewById(R.id.userNameTextView)
        userNameTextView.text = userName

        wishlistCountTextView = view.findViewById(R.id.wishlistCountTextView)
        watchedCountTextView = view.findViewById(R.id.watchedCountTextView)

        wishlistRecyclerView = view.findViewById(R.id.wishlist_recycler_view)
        watchedRecyclerView = view.findViewById(R.id.watched_recycler_view)

        wishlistButton = view.findViewById(R.id.wishlist_button)
        watchedButton = view.findViewById(R.id.watched_button)

        wishlistRecyclerView.layoutManager = LinearLayoutManager(context)
        watchedRecyclerView.layoutManager = LinearLayoutManager(context)

        wishlistAdapter = MovieAdapter(emptyList(), { movie ->
        }, { movie ->
            wishlistManager.removeFromWishlist(userId, movie)
            wishlistManager.addToWatched(userId, movie)
            fetchWishlist()
            updateCounts()
        }, showWatchedButton = true)

        watchedAdapter = MovieAdapter(emptyList(), { movie ->

        }, { movie ->
        }, showWatchedButton = false)

        wishlistRecyclerView.adapter = wishlistAdapter
        watchedRecyclerView.adapter = watchedAdapter

        wishlistManager = WishlistManager()

        wishlistButton.setOnClickListener {
            fetchWishlist()
            wishlistRecyclerView.visibility = View.VISIBLE
            watchedRecyclerView.visibility = View.GONE
        }

        watchedButton.setOnClickListener {
            fetchWatchedMovies()
            watchedRecyclerView.visibility = View.VISIBLE
            wishlistRecyclerView.visibility = View.GONE
        }

        updateCounts()

        return view
    }

    private fun fetchWishlist() {
        wishlistManager.getWishlist(userId) { movies ->
            wishlistAdapter.updateMovies(movies)
        }
    }

    private fun fetchWatchedMovies() {
        wishlistManager.getWatchedMovies(userId) { movies ->
            watchedAdapter.updateMovies(movies)
        }
    }

    private fun updateCounts() {
        wishlistManager.getWishlist(userId) { movies ->
            wishlistCountTextView.text = "Wishlist: ${movies.size}"
        }

        wishlistManager.getWatchedMovies(userId) { movies ->
            watchedCountTextView.text = "Watched: ${movies.size}"
        }
    }
}
