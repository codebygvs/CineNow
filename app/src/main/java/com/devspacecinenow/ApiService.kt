package com.devspacecinenow

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("now_playing?language=en-US&page=1")
    fun getNowPlayingMovies(): Call<MovieResponse>

    @GET("top_rated?language=en-US&page=1")
    fun topRatedMovies(): Call<MovieResponse>

    @GET("popular?language=en-US&page=1")
    fun popularMovies(): Call<MovieResponse>

    @GET("upcoming?language=en-US&page=1")
    fun upcomingMovies(): Call<MovieResponse>

    @GET("{movie_id}?language=en-US")
    fun getMovieById(@Path("movie_id") movieId: String): Call<MovieDTO>

}