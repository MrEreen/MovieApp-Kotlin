package com.example.movieapp

import com.example.movieapp.models.Movie
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("https://imdb-top-100-movies.p.rapidapi.com/")
    fun getMovies(): Call<List<Movie>>
}