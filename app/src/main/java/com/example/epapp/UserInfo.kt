package com.example.epapp

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.epapp.model.Movie
import com.google.gson.Gson
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun UserInfoScreen(mbti: String, movies: String) {
    val gson = Gson()
    //incodig된 문자열 decoding
    val decodedMovies = URLDecoder.decode(movies, StandardCharsets.UTF_8.toString())
    // 디코딩된 JSON 문자열을 로그에 출력
    Log.d("Decoded JSON: $decodedMovies", "decodedMovies")

    //Json 문자열을 Movies 리스트로 변환
    val movieList: List<Movie> = gson.fromJson(decodedMovies, Array<Movie>::class.java).toList()

    Column(
        // 고정적인 높이를 제공한다
        modifier = Modifier.height(300.dp),
    ) {
        Text(text = "나의 MBTI는 $mbti")
        Text(text = "나의 추천 받았던 영화 목록")
        LazyColumn{
            items(movieList) { movie ->
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = movie.title)
                    val painter =
                        rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500${movie.poster_path}")
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.size(128.dp)
                    )
                }
            }
        }

    }
}