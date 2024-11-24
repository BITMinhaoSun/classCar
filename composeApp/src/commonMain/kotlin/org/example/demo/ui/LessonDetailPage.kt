package org.example.demo.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.back
import demo.composeapp.generated.resources.delete
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.demo.getPlatform
import org.example.demo.util.DeleteQuestionRequest
import org.example.demo.util.QuestionsResponse
import org.example.demo.util.client
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun LessonDetailPage(
    navController: NavController,
    lessonId: Int,
    lessonName: String,
    lessonDescription: String,
    role: String,
    name: String,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val questions = remember { mutableStateListOf<QuestionsResponse>() }
    Scaffold(
        topBar = {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(painterResource(Res.drawable.back), "back")
            }
        },
        floatingActionButton = {
            if (role == "teacher") {
                Button(onClick = {
                    navController.navigate("createQuestionPage/${lessonId}")
                }) {
                    Text("+ 新建题目")
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxHeight().widthIn(max = 700.dp)
            ) {
                items(questions) { question->
                    QuestionBriefCard(
                        question.id,
                        question.description,
                        role,
                        onClick = {/*TODO*/},
                        onDelete = {
                            println("=======================")
                            println(question.id)
                            println("=======================")
                            println("Request Body: ${DeleteQuestionRequest(question.id)}")
                            scope.launch {
                                client.post("/teacher/question/delete/${question.id}")

                            }
                        },
                        Modifier.padding(5.dp).fillMaxWidth()
                    )
                }
            }
        }
        LaunchedEffect(Unit) {
            scope.launch {
                val result0: List<QuestionsResponse> = client.get("/${role}/questions/${lessonId}").body()
                questions.clear()
                result0.forEach {
                    questions.add(it)
                }

                if (role == "student") {
                    while (true) {
                        delay(2000L)
                        val result: List<QuestionsResponse> = client.get("/${role}/questions/${lessonId}").body()
                        if (result.size > questions.size) {
                            questions.clear()
                            result.forEach {
                                questions.add(it)
                            }
                            snackbarHostState.showSnackbar("有新的题目")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionBriefCard(
    questionId: Int,
    description: String,
    role: String,
    onClick: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    modifier: Modifier,
) {
    Card(modifier = modifier.clickable {
        onClick(questionId)
    }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.padding(5.dp)
            ) {
                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (role == "teacher") {
                IconButton(onClick = { onDelete(questionId) }) {
                    Icon(painterResource(Res.drawable.delete), "delete")
                }
            }
        }
    }
}