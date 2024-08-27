package com.example.movieapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.movieapp.managers.WishlistManager
import com.example.movieapp.models.Movie
import com.google.firebase.firestore.FirebaseFirestore

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var userId: String
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        db = FirebaseFirestore.getInstance()
        val movie: Movie? = intent.getParcelableExtra<Movie>("movie")
        userId = intent.getStringExtra("userId") ?: ""

        if (movie == null) {
            finish()
            return
        }

        val poster: ImageView = findViewById(R.id.movie_poster)
        val title: TextView = findViewById(R.id.movie_title)
        val description: TextView = findViewById(R.id.movie_description)
        val addToWishlistButton: Button = findViewById(R.id.add_to_wishlist_button)
        val commentInput: EditText = findViewById(R.id.comment_input)
        val submitCommentButton: Button = findViewById(R.id.submit_comment_button)

        title.text = movie.title
        description.text = movie.description
        Glide.with(this)
            .load(movie.image)
            .into(poster)

        addToWishlistButton.setOnClickListener {
            addToWishlist(userId, movie)
        }

        submitCommentButton.setOnClickListener {
            val commentText = commentInput.text.toString()
            if (commentText.isNotEmpty()) {
                addComment(movie.title, userId, commentText)
            }
        }
    }

    private fun addToWishlist(userId: String, movie: Movie) {
        val wishlistManager = WishlistManager()
        wishlistManager.addToWishlist(userId, movie)
    }

    private fun addComment(movieTitle: String, userId: String, commentText: String) {
        val comment = mapOf(
            "userId" to userId,
            "comment" to commentText,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("comments").document(movieTitle).collection("comments").add(comment)
            .addOnSuccessListener {
                Log.d("MovieDetailActivity", "Comment added successfully")
            }
            .addOnFailureListener { e ->
                Log.e("MovieDetailActivity", "Error adding comment", e)
            }
    }
}
