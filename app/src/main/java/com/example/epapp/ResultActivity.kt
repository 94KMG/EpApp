package com.example.epapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.gson.gson
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

// Ktor 클라이언트 설정
val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        // Ktor 클라이언트를 통해 서버와 데이터를 주고받을 때 사용
        gson {
            setPrettyPrinting()
            disableHtmlEscaping()
        }
    }
}

@Composable
fun ResultScreen(data: String) {
    val gson = Gson()
    // incoding된 문자열 decoding
    val decodedData = URLDecoder.decode(data, StandardCharsets.UTF_8.toString())
    // json 문자열을 QuestionWtihAnswers 리스트로 변환
    val questionsList: List<QuestionWithAnswers> =
        gson.fromJson(decodedData, Array<QuestionWithAnswers>::class.java).toList()
    // 변환된 데이터를 사용하여 UI를 구성
    var eCount by remember { mutableStateOf(0) }
    var iCount by remember { mutableStateOf(0) }
    var nCount by remember { mutableStateOf(0) }
    var sCount by remember { mutableStateOf(0) }
    var fCount by remember { mutableStateOf(0) }
    var tCount by remember { mutableStateOf(0) }
    var jCount by remember { mutableStateOf(0) }
    var pCount by remember { mutableStateOf(0) }
    var mbti = ""
    // 버튼 실행시 DisplayMovies 실행 할 상태변수
    var showMovies by remember { mutableStateOf(false) }
    for (question in questionsList) {
        when (question.mbti) {
            "E" -> eCount++
            "I" -> iCount++
            "N" -> nCount++
            "S" -> sCount++
            "F" -> fCount++
            "T" -> tCount++
            "J" -> jCount++
            "P" -> pCount++
        }
    }
    if (eCount > iCount) {
        mbti += "E"
    } else {
        mbti += "I"
    }

    if (nCount > sCount) {
        mbti += "N"
    } else {
        mbti += "S"
    }

    if (fCount > tCount) {
        mbti += "F"
    } else {
        mbti += "T"
    }

    if (jCount > pCount) {
        mbti += "J"
    } else {
        mbti += "P"
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 각 mbti에 해당하는 설명도 나중에 추가
        Text(text = "당신의 MBTI는 $mbti 입니다")
        Button(onClick = {
            showMovies = true // 버튼 클릭 시 showMovie 상태를 true로 설정
        }) {
            Text(text = "당신의 MBTI에 어울리는 영화를 추천받겠습니까?")
        }
        if (showMovies) {
            DisplayMovies(mbti)
        }

    }
}

data class Movie(val title: String, val poster_path: String)
data class MovieResponse(val results: List<Movie>)

@Composable
fun DisplayMovies(mbti: String) {
    val genreMap = mapOf(
        "INTJ" to 28, // 액션
        "ENFP" to 35, // 코미디
        "INFJ" to 18, // 드라마
        "ENTP" to 878, // SF
        "ISFP" to 10402, // 음악
        "ESTP" to 12, // 모험
        "ISTJ" to 53, // 스릴러
        "ESFJ" to 10751, // 가족
        "INFP" to 14, // 판타지
        "ESTJ" to 80, // 범죄
        "ISFJ" to 10749, // 로맨스
        "ENTJ" to 9648, // 미스터리
        "ESFP" to 27, // 공포
        "ISTP" to 16, // 애니메이션
        "ENFJ" to 36, // 역사
        "INTP" to 99  // 다큐멘터리
        // 추가적인 MBTI와 장르 매칭
    )
    // 값이 비어 있을 경우 액션
    val selectedGenreId = genreMap[mbti] ?: 28 // 기본 장르는 액션
    val apiKey = "1b49f4e5f7493e2d0f7047dbfb72a6d1"

    // 장르에 따른 영화리스트 가져오기
    val movieList = remember {
        mutableStateOf(listOf<Movie>())
    }
    // 장르가 바뀔 때마다 실행
    LaunchedEffect(key1 = selectedGenreId) {
        val url =
            "https://api.themoviedb.org/3/discover/movie?api_key=$apiKey&with_genres=$selectedGenreId&language=ko-KR"
        try {
            val response: HttpResponse = client.get(url)
            val responseBody: String = response.bodyAsText()
            val movieResponse: MovieResponse =
                Gson().fromJson(responseBody, MovieResponse::class.java)
            movieList.value = movieResponse.results
        } catch (e: Exception) {
            println("네트워크 요청 실패: ${e.message}")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        movieList.value.forEach { movie ->
            Text(text = movie.title) // 영화 제목 출력
            Image(
                painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500${movie.poster_path}"),
                contentDescription = null
            ) // 영화 포스터 이미지 출력
        }
    }

}



