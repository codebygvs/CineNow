package com.devspacecinenow.detail.data

import com.devspacecinenow.common.model.MovieDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailService {
    @GET("{movie_id}?language=en-US")
    suspend fun getMovieById(@Path("movie_id") movieId: String): Response<MovieDTO>
}