package com.example.movieapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Comment(val movieTitle: String, val userName: String, val comment: String)

class CommentsAdapter(private var comments: List<Comment>) : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieTitle: TextView = itemView.findViewById(R.id.movie_title)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val comment: TextView = itemView.findViewById(R.id.comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentComment = comments[position]
        holder.movieTitle.text = currentComment.movieTitle
        holder.userName.text = currentComment.userName
        holder.comment.text = currentComment.comment
    }

    override fun getItemCount() = comments.size

    fun updateComments(newComments: List<Comment>) {
        comments = newComments
        notifyDataSetChanged()
    }
}
