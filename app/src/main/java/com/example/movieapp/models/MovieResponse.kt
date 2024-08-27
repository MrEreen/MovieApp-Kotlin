package com.example.movieapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val title: String = "",
    val image: String = "",
    val description: String = "",
    val imdb_link: String = "",
    val year :String=""
) : Parcelable {

    constructor() : this("", "", "", "")
}
