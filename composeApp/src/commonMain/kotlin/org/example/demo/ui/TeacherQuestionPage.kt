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
import demo.composeapp.generated.resources.add
import demo.composeapp.generated.resources.back
import demo.composeapp.generated.resources.delete_circle
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import org.example.demo.util.AddQuestionRequest
import org.example.demo.util.client
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun TeacherQuestionPage(
    navController: NavController,
    questionId: Int,
    description: String,
    options: List<String>,
    answer: String,
    lessonId: Int,
    courseId: Int,
    questionReleased: Boolean,
    questionClosed: Boolean,
    name: String,
    role: String
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var released by remember { mutableStateOf(questionReleased) }
    var closed by remember { mutableStateOf(questionClosed) }

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
                        "题目详情",
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
                        description,
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
                items(options) {
                    InputChip(
                        selected = answer == it,
                        onClick = { },
                        label = { Text(it) },
                        trailingIcon = {},
                        shape = CircleShape,
                        border = null,
                        elevation = InputChipDefaults.inputChipElevation(elevation = 3.dp),
                        colors = InputChipDefaults.inputChipColors(
                            containerColor = Color.White,
                            selectedContainerColor = Color(0xff90e19f)
                        ),
                        modifier = Modifier.align(Alignment.Center).padding(start = 20.dp, end = 20.dp)
                    )
                }

                item {
                    // submit
                    if (released) {
                        if (closed) {
                            Button(
                                onClick = {
                                    scope.launch {

                                    }
                                },
                                modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                            ) {
                                Text("查看答题统计")
                            }
                        } else {
                            Button(
                                onClick = {
                                    scope.launch {
                                        client.post("/teacher/question/close/${questionId}")
                                    }
                                    closed = true
                                },
                                modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                            ) {
                                Text("结束答题")
                            }
                        }
                    } else {
                        Button(
                            onClick = {
                                scope.launch {
                                    client.post("/teacher/question/release/${questionId}")
                                }
                                released = true
                            },
                            modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                        ) {
                            Text("发布题目")
                        }
                    }
                }
            }
        }
    }
}