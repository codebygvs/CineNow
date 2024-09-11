package com.devspacecinenow

import com.google.gson.annotations.SerializedName


@kotlinx.serialization.Serializable
data class MovieDTO (
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path")
    val postPath: String,
    @SerializedName("vote_average")
    val voteAverage: Double,

){
    val posterFullPath: String
        get () = "https://image.tmdb.org/t/p/w300$postPath"
}