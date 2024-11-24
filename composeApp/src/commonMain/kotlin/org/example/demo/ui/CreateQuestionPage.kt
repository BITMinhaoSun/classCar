package org.example.demo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.navigation.NavController
import demo.composeapp.generated.resources.*
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.add
import demo.composeapp.generated.resources.back
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import org.example.demo.util.AddQuestionRequest
import org.example.demo.util.CourseRequest
import org.example.demo.util.JoinCourseRequest
import org.example.demo.util.client
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun CreateQuestionPage(
    navController: NavController,
    lessonId: Int,
    name: String,
    role: String
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var description by remember { mutableStateOf("") }
    val options = remember { mutableStateListOf<String>() }
    var answer by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var newOption by remember { mutableStateOf("") }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            confirmButton = {
                Button(onClick = {
                    options.add(newOption)
                    showDialog = false
                    newOption = ""
                }) {
                    Text("添加选项")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text("取消")
                }
            },
            title = {
                Text("输入选项内容")
            },
            text = {
                TextField(
                    value = newOption,
                    onValueChange = { newOption = it }
                )
            }
        )
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
                    // description input
                    OutlinedTextField(
                        description,
                        onValueChange = { description = it },
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
                        onClick = { answer = it },
                        label = { Text(it) },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    options.remove(it)
                                    if (answer == it) {
                                        answer = ""
                                    }
                                },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(painterResource(Res.drawable.delete_circle), "delete_circle")
                            }
                        },
                        shape = CircleShape,
                        border = null,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                item {
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            showDialog = true
                        }) {
                            Icon(painterResource(Res.drawable.add), "add")
                        }
                        Text("添加选项")
                    }
                }

                item {
                    // submit
                    Button(onClick = {
                        scope.launch {
                            if (answer in options) {
                                client.post("/teacher/question/add/${lessonId}") {
                                    contentType(ContentType.Application.Json)
                                    setBody(
                                        AddQuestionRequest(
                                            description = description,
                                            options = options,
                                            answer = answer,
                                            lessonId = lessonId
                                        )
                                    )
                                }
                                navController.popBackStack()
                            } else {
                                snackbarHostState.showSnackbar("请选择题目答案")
                            }
                        }
                    }) {
                        Text("新建题目")
                    }
                }
            }
        }
    }
}