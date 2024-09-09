package com.devspacecinenow

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.devspacecinenow.ui.theme.CineNowTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun MovieDetailScreen(
    movieId: String,
    navHostController: NavHostController
) {
    var movieDto by remember { mutableStateOf<MovieDTO?>(null) }
    var voteAverage by remember { mutableDoubleStateOf(0.0) }

    LaunchedEffect(movieId) {

        val apiService = RetrofitClient.retrofitInstance.create(ApiService::class.java)

        apiService.getMovieById(movieId).enqueue(object : Callback<MovieDTO> {
            override fun onResponse(call: Call<MovieDTO>, response: Response<MovieDTO>) {
                if (response.isSuccessful) {
                    movieDto = response.body()
                    Log.d(
                        "API Response",
                        "Movie: ${movieDto?.title}, Vote Average: ${movieDto?.voteAverage}"
                    )
                    voteAverage = (movieDto?.voteAverage ?: 0.0).toDouble()
                } else {
                    Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<MovieDTO>, t: Throwable) {
                Log.d("MainActivity", "Network Error :: ${t.message}")
            }

        })
    }

    if (movieDto != null) {
        Log.d("Movie Details", "Displaying movie: ${movieDto!!.title}")
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    navHostController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back Button"
                    )
                }

                Spacer(
                    modifier = Modifier.width(8.dp),

                    )

                Text(
                    text = movieDto!!.title,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            MovieDetailContent(movieDto!!, voteAverage)
        }
    } else {
        Log.d("Movie Details", "No movie data available")
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}


@Composable
private fun MovieDetailContent(movie: MovieDTO, voteAverage: Double) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .height(200.dp)
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
            model = movie.posterFullPath,
            contentDescription = "${movie.title} Poster image"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = movie.overview,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            RatingBar(voteAverage)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "(${voteAverage} Average Grade)",
                fontSize = 14.sp,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(8.dp))

        }
    }
}

@Composable
fun RatingBar(voteAverage: Double) {

    val ratingAverage = (voteAverage / 2).toInt()

    Column {
        Row {
            for (i in 1..5) {
                if (i <= ratingAverage) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Filled Star",
                        tint = Color.Yellow,
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = "Empty Star",
                        tint = Color.Gray,
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun MovieDetailPreview() {
    CineNowTheme {
        val movie = MovieDTO(
            id = 9,
            title = "Title",
            postPath = "",
            overview = "Long overview movie",
            voteAverage = 7.3
        )

        MovieDetailContent(movie = movie, voteAverage = 7.3)
    }
}