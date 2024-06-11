package com.example.epapp.model

data class QuestionWithAnswers(
    // 정확한 식별을 위해 id 변수 추가
    val id: Int,
    val question: String,
    val yesAnswer: String,
    val noAnswer: String,
    var mbti: String = ""
)