package com.devspacecinenow.detail.presentation.ui

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.devspacecinenow.common.model.MovieDTO
import com.devspacecinenow.detail.presentation.MovieDetailViewModel
import com.devspacecinenow.ui.theme.CineNowTheme

@Composable
fun MovieDetailScreen(
    Id: String,
    navHostController: NavHostController,
    viewModel: MovieDetailViewModel
) {
    val movieDto by viewModel.uiMovieById.collectAsState()
    viewModel.fetchMovieDetail(Id)



    movieDto?.let {
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
                    viewModel.cleanMovieId()
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
                    text = movieDto?.title?: "Unknown Title",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            MovieDetailContent(it)
        }
    }
}


@Composable
private fun MovieDetailContent(movie: MovieDTO?) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (movie != null) {
            AsyncImage(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop,
                model = movie.posterFullPath,
                contentDescription = "${movie.title} Poster image"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (movie != null) {
            Text(
                text = movie.overview,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.width(8.dp))

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
            overview = "Long overview movie"

        )
        MovieDetailContent(movie = movie)
    }
}