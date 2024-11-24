package org.example.demo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.back
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import org.example.demo.util.AnswerQuestionRequest
import org.example.demo.util.StudentQuestionDetailResponse
import org.example.demo.util.client
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun StudentQuestionPage(
    navController: NavController,
    questionId: Int,
    name: String,
    role: String
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var question by remember { mutableStateOf(StudentQuestionDetailResponse(
        0, "", listOf(""), "",
        "", 0, 0, true, true
    )) }
    var answer by remember { mutableStateOf("") }

    fun refresh() = scope.launch {
        question = client.get("/student/question/detail/${questionId}/${name}").body()
        println("stdAns: ${question.standardAnswer}")
    }

    Scaffold(
        topBar = {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(painterResource(Res.drawable.back), "back")
            }
        },
        floatingActionButton = {
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(it),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(10.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = if (question.closed) "答题已结束" else "回答问题",
                        onValueChange = {  },
                        readOnly = true,
                        placeholder = { Text("题目详情") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent,
                            errorBorderColor = Color.Transparent,
                        ),
                        textStyle = MaterialTheme.typography.titleLarge
                    )
                }
                item {
                    // description input
                    OutlinedTextField(
                        question.description,
                        onValueChange = {  },
                        readOnly = true,
                        placeholder = { Text("题目内容") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            disabledBorderColor = Color.Transparent,
                            errorBorderColor = Color.Transparent,
                        ),
                        textStyle = MaterialTheme.typography.bodyLarge
                    )
                }

                // options selection
                items(question.options) {
                    InputChip(
                        selected = if (!question.closed) answer == it else question.standardAnswer == it,
                        onClick = { answer = it },
                        label = { Text(it) },
                        trailingIcon = {},
                        shape = CircleShape,
                        border = null,
                        elevation = InputChipDefaults.inputChipElevation(elevation = 3.dp),
                        colors = InputChipDefaults.inputChipColors(
                            containerColor = if (question.myAnswer == it) Color(0xffe69090) else Color.White,
                            selectedContainerColor = if (!question.closed) Color(0xff90c8e1) else Color(0xff90e19f)
                        ),
                        modifier = Modifier.align(Alignment.Center).padding(start = 20.dp, end = 20.dp)
                    )
                }

                item {
                    // submit
                    if (!question.closed) {
                        Button(
                            onClick = {
                                scope.launch {
                                    client.post("/student/question/answer") {
                                        contentType(ContentType.Application.Json)
                                        setBody(
                                            AnswerQuestionRequest(
                                                name,
                                                question.id,
                                                answer
                                            )
                                        )
                                    }
                                    navController.popBackStack()
                                }
                            },
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                        ) {
                            Text("提交答案")
                        }
                    } else {
                        Text("标准答案：${question.standardAnswer}")
                        if (question.myAnswer == "") {
                            Text("你没有作答")
                        } else {
                            Text("你的答案：${question.myAnswer}")
                        }
                    }
                }
            }
        }
        LaunchedEffect(Unit) { refresh() }
    }
}