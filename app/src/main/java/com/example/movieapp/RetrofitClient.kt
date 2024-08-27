package com.example.movieapp

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://imdb-top-100-movies.p.rapidapi.com/"

    private val client = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("x-rapidapi-key", "43d4c76db4msh22d99a7916525ebp16b384jsn53ffdcb52f29")
                .addHeader("x-rapidapi-host", "imdb-top-100-movies.p.rapidapi.com")
                .build()
            chain.proceed(request)
        }
    }.build()

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}
