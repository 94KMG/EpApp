package com.example.epapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.gson.Gson

@Composable
fun ResultScreen(data: String) {
    val gson = Gson()
    // json 문자열을 QuestionWtihAnswers 리스트로 변환
    val questionsList: List<QuestionWithAnswers> =
        gson.fromJson(data, Array<QuestionWithAnswers>::class.java).toList()
    // 변환된 데이터를 사용하여 UI를 구성
    var eCount = 0
    var iCount = 0
    var nCount = 0
    var sCount = 0
    var fCount = 0
    var tCount = 0
    var jCount = 0
    var pCount = 0
    var mbti = ""
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

    }
}



