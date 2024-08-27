package com.example.movieapp.managers

import com.example.movieapp.models.Movie
import com.google.firebase.firestore.FirebaseFirestore

class WishlistManager {

    private val db = FirebaseFirestore.getInstance()

    fun addToWishlist(userId: String, movie: Movie) {
        db.collection("users").document(userId).collection("wishlist").document(movie.title).set(movie)
    }

    fun removeFromWishlist(userId: String, movie: Movie) {
        db.collection("users").document(userId).collection("wishlist").document(movie.title).delete()
    }

    fun getWishlist(userId: String, callback: (List<Movie>) -> Unit) {
        db.collection("users").document(userId).collection("wishlist").get()
            .addOnSuccessListener { documents ->
                val movies = documents.mapNotNull { it.toObject(Movie::class.java) }
                callback(movies)
            }
    }

    fun getWatchedMovies(userId: String, callback: (List<Movie>) -> Unit) {
        db.collection("users").document(userId).collection("watched").get()
            .addOnSuccessListener { documents ->
                val movies = documents.mapNotNull { it.toObject(Movie::class.java) }
                callback(movies)
            }
    }

    fun addToWatched(userId: String, movie: Movie) {
        db.collection("users").document(userId).collection("watched").document(movie.title).set(movie)
    }
}
