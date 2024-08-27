package com.example.movieapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.MovieAdapter
import com.example.movieapp.MovieDetailActivity
import com.example.movieapp.R
import com.example.movieapp.RetrofitClient
import com.example.movieapp.models.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var userId: String
    private lateinit var userName: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovieAdapter
    private lateinit var searchView: SearchView
    private var movies: List<Movie> = listOf()

    companion object {
        private const val ARG_USER_ID = "userId"
        private const val ARG_USER_NAME = "userName"

        fun newInstance(userId: String, userName: String): HomeFragment {
            val fragment = HomeFragment()
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
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        userId = arguments?.getString(ARG_USER_ID) ?: ""
        userName = arguments?.getString(ARG_USER_NAME) ?: "Guest"

        recyclerView = view.findViewById(R.id.recycler_view)
        searchView = view.findViewById(R.id.searchView)
        recyclerView.layoutManager = GridLayoutManager(context, 2) // 2 sütunlu grid layout
        adapter = MovieAdapter(movies, { movie ->
            val intent = Intent(activity, MovieDetailActivity::class.java)
            intent.putExtra("movie", movie)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }, { movie ->
        }, showWatchedButton = false)
        recyclerView.adapter = adapter

        fetchMovies()

        setupSearchView()

        val searchEditText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(resources.getColor(android.R.color.white))
        searchEditText.setHintTextColor(resources.getColor(android.R.color.white))

        return view
    }

    private fun fetchMovies() {
        RetrofitClient.instance.getMovies().enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                if (response.isSuccessful) {
                    movies = response.body() ?: listOf()
                    adapter.updateMovies(movies)
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredMovies = movies.filter { movie ->
                    movie.title.contains(newText ?: "", ignoreCase = true)
                }
                adapter.updateMovies(filteredMovies)
                return true
            }
        })
    }
}
