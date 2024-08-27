package com.example.movieapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.Comment
import com.example.movieapp.CommentsAdapter
import com.example.movieapp.R
import com.google.firebase.firestore.FirebaseFirestore

class CommentsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CommentsAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_comments, container, false)

        db = FirebaseFirestore.getInstance()

        recyclerView = view.findViewById(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = CommentsAdapter(emptyList())
        recyclerView.adapter = adapter

        fetchComments()

        return view
    }

    private fun fetchComments() {
        db.collectionGroup("comments").get().addOnSuccessListener { documents ->

                val comments = mutableListOf<Comment>()

                for (document in documents) {
                    val movieTitle = document.reference.parent.parent?.id ?: ""
                    val commentText = document.getString("comment") ?: ""
                    val userId = document.getString("userId") ?: ""


                    db.collection("users").document(userId).get()
                        .addOnSuccessListener { userDocument ->
                            val userName = userDocument.getString("name") ?: "Unknown"
                            comments.add(Comment(movieTitle, userName, commentText))
                            if (comments.size == documents.size()) {
                                adapter.updateComments(comments)
                            }
                        }

                }
            }
    }
}
