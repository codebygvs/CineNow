package com.devspacecinenow

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun MovieListScreen(navController: NavHostController) {
    var nowPlayingMovies by remember { mutableStateOf<List<MovieDTO>>(emptyList()) }
    var topRatedMovies by remember { mutableStateOf<List<MovieDTO>>(emptyList()) }
    var popularMovies by remember { mutableStateOf<List<MovieDTO>>(emptyList()) }
    var upcomingMovies by remember { mutableStateOf<List<MovieDTO>>(emptyList()) }

    val apiService = RetrofitClient.retrofitInstance.create(ApiService::class.java)

    val callUpComingMovies = apiService.upcomingMovies()

    callUpComingMovies.enqueue(object : Callback<MovieResponse> {
        override fun onResponse(
            call: Call<MovieResponse>,
            response: Response<MovieResponse>
        ) {
            if (response.isSuccessful) {
                val movies = response.body()?.results
                if (movies != null) {
                    upcomingMovies = movies
                }
            } else {
                Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            Log.d("MainActivity", "Network Error :: ${t.message}")
        }
    })

    val callNowPlaying = apiService.getNowPlayingMovies()


    callNowPlaying.enqueue(object : Callback<MovieResponse> {
        override fun onResponse(
            call: Call<MovieResponse>,
            response: Response<MovieResponse>
        ) {
            if (response.isSuccessful) {

                val movies = response.body()?.results

                if (movies != null) {
                    nowPlayingMovies = movies
                }

            } else {
                Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
            }
        }


        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            Log.d("MainActivity", "NetWork Error :: ${t.message}")
        }

    })

    val calltopRatedMovies = apiService.topRatedMovies()

    calltopRatedMovies.enqueue(object : Callback<MovieResponse> {
        override fun onResponse(
            call: Call<MovieResponse>,
            response: Response<MovieResponse>
        ) {
            if (response.isSuccessful) {
                val movies = response.body()?.results
                if (movies != null) {
                    topRatedMovies = movies
                }
            } else {
                Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            Log.d("MainActivity", "Network Error :: ${t.message}")
        }

    })

    val callPopularMovies = apiService.popularMovies()
    callPopularMovies.enqueue(object : Callback<MovieResponse> {
        override fun onResponse(
            call: Call<MovieResponse>,
            response: Response<MovieResponse>
        ) {
            if (response.isSuccessful) {
                val movies = response.body()?.results
                if (movies != null) {
                    popularMovies = movies
                }
            } else {
                Log.d("MainActivity", "Request Error :: ${response.errorBody()}")
            }
        }

        override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
            Log.d("MainActivity", "Network Error :: ${t.message}")
        }

    })

    MovieListContent(
        upcomingMovies = upcomingMovies,
        nowPlayingMovies = nowPlayingMovies,
        topRatedMovies = topRatedMovies,
        popularMovies = popularMovies
    ) { itemClicked ->
        navController.navigate(route = "movieDetail/${itemClicked.id}")
    }

}

@Composable
private fun MovieListContent(
    upcomingMovies: List<MovieDTO>,
    nowPlayingMovies: List<MovieDTO>,
    topRatedMovies: List<MovieDTO>,
    popularMovies: List<MovieDTO>,
    onClick: (MovieDTO) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            fontSize = 40.sp,
            fontWeight = FontWeight.SemiBold,
            text = "CineNow"
        )
        MovieSession(
            label = "Upcoming",
            movieList = upcomingMovies,
            onClick = onClick
        )


        MovieSession(
            label = "Now Playing",
            movieList = nowPlayingMovies,
            onClick = onClick
        )

        MovieSession(
            label = "Top Rated",
            movieList = topRatedMovies,
            onClick = onClick
        )
        MovieSession(
            label = "Popular",
            movieList = popularMovies,
            onClick = onClick
        )
    }
}

@Composable
private fun MovieSession(
    label: String,
    movieList: List<MovieDTO>,
    onClick: (MovieDTO) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            fontSize = 24.sp,
            text = label,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.size(8.dp))
        MovieList(movieList = movieList, onClick = onClick)
    }

}

@Composable
private fun MovieList(
    movieList: List<MovieDTO>,
    onClick: (MovieDTO) -> Unit
) {
    LazyRow {
        items(movieList) {
            MovieItem(
                movieDTO = it,
                onClick = onClick
            )

        }
    }
}

@Composable
private fun MovieItem(
    movieDTO: MovieDTO,
    onClick: (MovieDTO) -> Unit
) {

    Column(
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .clickable {
                onClick.invoke(movieDTO)
            }
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(end = 4.dp)
                .width(120.dp)
                .height(150.dp),
            contentScale = ContentScale.Crop,
            model = movieDTO.posterFullPath,
            contentDescription = "${movieDTO.title} Poster image"
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            text = movieDTO.title
        )
        Text(
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            text = movieDTO.overview
        )
    }

}
