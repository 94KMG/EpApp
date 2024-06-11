package com.example.epapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.epapp.ui.theme.EpAppTheme
// stinrgs.xml import
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.epapp.model.Movie
import com.example.epapp.model.QuestionWithAnswers
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EpAppTheme {
                MainScreen()
            }
        }
    }
}

// 화면 간 이동을 관리
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val gson = Gson()
    NavHost(navController = navController, startDestination = "question") {
        // 화면 이름 지정
        composable("question") { QuestionScreen(navController = navController, gson = gson) }
        composable(
            route = "result/{jsonData}", //jsonData를 넘겨줄 것을 명시
            arguments = listOf(navArgument("jsonData") {
                type = NavType.StringType
            }) // Type은 String
        ) { NavBackStackEntry ->
            ResultScreen(
                data = NavBackStackEntry.arguments?.getString("jsonData") ?: "",
                navController = navController,
                gson = gson
            )
        }
        composable(
            route = "user_info/{mbti}/{movies}",
            arguments = listOf(
                navArgument("mbti") { type = NavType.StringType },
                navArgument("movies") { type = NavType.StringType }
            )
        ) { NavBackStackEntry ->
            UserInfoScreen(
                mbti = NavBackStackEntry.arguments?.getString("mbti") ?: "",
                movies = NavBackStackEntry.arguments?.getString("movies") ?: "",
            )
        }
    }
}

@Composable
fun QuestionScreen(navController: NavController, gson: Gson) {
    // 질문 대답 데이터 생성
    val qeustionWithAnswers = listOf(
        QuestionWithAnswers(1, stringResource(id = R.string.question_1), "예", "아니요"),
        QuestionWithAnswers(2, stringResource(id = R.string.question_2), "예", "아니요"),
        QuestionWithAnswers(3, stringResource(id = R.string.question_3), "예", "아니요"),
        QuestionWithAnswers(4, stringResource(id = R.string.question_4), "예", "아니요"),
        QuestionWithAnswers(5, stringResource(id = R.string.question_5), "예", "아니요"),
        QuestionWithAnswers(6, stringResource(id = R.string.question_6), "예", "아니요"),
        QuestionWithAnswers(7, stringResource(id = R.string.question_7), "예", "아니요"),
        QuestionWithAnswers(8, stringResource(id = R.string.question_8), "예", "아니요"),
        QuestionWithAnswers(9, stringResource(id = R.string.question_9), "예", "아니요"),
        QuestionWithAnswers(10, stringResource(id = R.string.question_10), "예", "아니요"),
        QuestionWithAnswers(11, stringResource(id = R.string.question_11), "예", "아니요"),
        QuestionWithAnswers(12, stringResource(id = R.string.question_12), "예", "아니요"),
        QuestionWithAnswers(13, stringResource(id = R.string.question_13), "예", "아니요"),
        QuestionWithAnswers(14, stringResource(id = R.string.question_14), "예", "아니요"),
        QuestionWithAnswers(15, stringResource(id = R.string.question_15), "예", "아니요"),
        QuestionWithAnswers(16, stringResource(id = R.string.question_16), "예", "아니요"),
        QuestionWithAnswers(17, stringResource(id = R.string.question_17), "예", "아니요"),
        QuestionWithAnswers(18, stringResource(id = R.string.question_18), "예", "아니요"),
        QuestionWithAnswers(19, stringResource(id = R.string.question_19), "예", "아니요"),
        QuestionWithAnswers(20, stringResource(id = R.string.question_20), "예", "아니요"),
        QuestionWithAnswers(21, stringResource(id = R.string.question_21), "예", "아니요"),
        QuestionWithAnswers(22, stringResource(id = R.string.question_22), "예", "아니요"),
        QuestionWithAnswers(23, stringResource(id = R.string.question_23), "예", "아니요"),
        QuestionWithAnswers(24, stringResource(id = R.string.question_24), "예", "아니요"),
        QuestionWithAnswers(25, stringResource(id = R.string.question_25), "예", "아니요"),
        QuestionWithAnswers(26, stringResource(id = R.string.question_26), "예", "아니요"),
        QuestionWithAnswers(27, stringResource(id = R.string.question_27), "예", "아니요"),
        QuestionWithAnswers(28, stringResource(id = R.string.question_28), "예", "아니요"),
        QuestionWithAnswers(29, stringResource(id = R.string.question_29), "예", "아니요"),
        QuestionWithAnswers(30, stringResource(id = R.string.question_30), "예", "아니요"),
        QuestionWithAnswers(31, stringResource(id = R.string.question_31), "예", "아니요"),
        QuestionWithAnswers(32, stringResource(id = R.string.question_32), "예", "아니요"),
    )

    // Button 컴포저블 상태변수 저장 데이터
    var buttonPushed by remember { mutableStateOf(false) }

    // LazyColumn 사용
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            items(qeustionWithAnswers) { questionWithAnswer ->
                Text(text = questionWithAnswer.question)
                Row {
                    Button(
                        onClick = {
                            buttonPushed = !buttonPushed
                            questionWithAnswer.mbti = determineMBTI(questionWithAnswer.id, true)
                        },
                        colors = if (buttonPushed) {
                            ButtonDefaults.buttonColors(
                                contentColor = Color.LightGray,
                                containerColor = Color.Gray
                            )
                        } else {
                            ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = Color.Blue
                            )
                        }
                    ) {
                        Text(text = questionWithAnswer.yesAnswer)
                    }
                    Button(
                        onClick = {
                            buttonPushed = !buttonPushed
                            questionWithAnswer.mbti = determineMBTI(questionWithAnswer.id, false)
                        },
                        colors = if (buttonPushed) {
                            ButtonDefaults.buttonColors(
                                contentColor = Color.LightGray,
                                containerColor = Color.Gray
                            )
                        } else {
                            ButtonDefaults.buttonColors(
                                contentColor = Color.White,
                                containerColor = Color.Blue
                            )
                        }
                    ) {
                        Text(text = questionWithAnswer.noAnswer)
                    }

                }

            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // 데이터 리스트라고 명시하기 위해서 그냥 씀
            val dataList = qeustionWithAnswers
            // list형태 String 형태로 변환
            val jsonData = gson.toJson(dataList)
            // Json 문자열 내에 포함된 특수 문자로 JSON 형식이 깨짐(JsonSyntaxException) 보완 용도
            val encodedJsonData = URLEncoder.encode(jsonData, StandardCharsets.UTF_8.toString())
            // Navigation으로 전달
            navController.navigate("result/$encodedJsonData")
        }) {
            Text(text = "결과확인")
        }
    }
}

// MBTI 유형을 결정 로직을 별도의 함수로 분리
fun determineMBTI(questionId: Int, isYes: Boolean): String {
    // 질문과 "예" 혹은 "아니요"의 답변에 따라 MBTI 유형 결정
    return when {
        questionId == 1 && isYes -> "E"
        questionId == 1 && !isYes -> "I"
        questionId == 2 && isYes -> "E"
        questionId == 2 && !isYes -> "I"
        questionId == 3 && isYes -> "S"
        questionId == 3 && !isYes -> "N"
        questionId == 4 && isYes -> "N"
        questionId == 4 && !isYes -> "S"
        questionId == 5 && isYes -> "T"
        questionId == 5 && !isYes -> "F"
        questionId == 6 && isYes -> "T"
        questionId == 6 && !isYes -> "F"
        questionId == 7 && isYes -> "J"
        questionId == 7 && !isYes -> "P"
        questionId == 8 && isYes -> "J"
        questionId == 8 && !isYes -> "P"
        questionId == 9 && isYes -> "E"
        questionId == 9 && !isYes -> "I"
        questionId == 10 && isYes -> "E"
        questionId == 10 && !isYes -> "I"
        questionId == 11 && isYes -> "E"
        questionId == 11 && !isYes -> "I"
        questionId == 12 && isYes -> "I"
        questionId == 12 && !isYes -> "E"
        questionId == 13 && isYes -> "I"
        questionId == 13 && !isYes -> "E"
        questionId == 14 && isYes -> "I"
        questionId == 14 && !isYes -> "E"
        questionId == 15 && isYes -> "S"
        questionId == 15 && !isYes -> "N"
        questionId == 16 && isYes -> "S"
        questionId == 16 && !isYes -> "N"
        questionId == 17 && isYes -> "S"
        questionId == 17 && !isYes -> "N"
        questionId == 18 && isYes -> "N"
        questionId == 18 && !isYes -> "S"
        questionId == 19 && isYes -> "N"
        questionId == 19 && !isYes -> "S"
        questionId == 20 && isYes -> "N"
        questionId == 20 && !isYes -> "S"
        questionId == 21 && isYes -> "T"
        questionId == 21 && !isYes -> "F"
        questionId == 22 && isYes -> "T"
        questionId == 22 && !isYes -> "F"
        questionId == 23 && isYes -> "F"
        questionId == 23 && !isYes -> "T"
        questionId == 24 && isYes -> "F"
        questionId == 24 && !isYes -> "T"
        questionId == 25 && isYes -> "F"
        questionId == 25 && !isYes -> "T"
        questionId == 26 && isYes -> "F"
        questionId == 26 && !isYes -> "T"
        questionId == 27 && isYes -> "J"
        questionId == 27 && !isYes -> "P"
        questionId == 28 && isYes -> "J"
        questionId == 28 && !isYes -> "P"
        questionId == 29 && isYes -> "J"
        questionId == 29 && !isYes -> "P"
        questionId == 30 && isYes -> "P"
        questionId == 30 && !isYes -> "J"
        questionId == 31 && isYes -> "P"
        questionId == 31 && !isYes -> "J"
        questionId == 32 && isYes -> "N"
        questionId == 32 && !isYes -> "S"
        else -> ""
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    EpAppTheme {
        MainScreen()
    }
}
