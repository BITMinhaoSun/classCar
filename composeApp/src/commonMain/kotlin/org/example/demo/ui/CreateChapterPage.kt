package org.example.demo.ui


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.bundle.bundleOf
import androidx.navigation.NavController
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.back
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import org.example.demo.util.ChapterAddRequest
import org.example.demo.util.CourseRequest
import org.example.demo.util.DiscussAddRequest
import org.example.demo.util.client
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun CreateChapterPage(
    navController: NavController,
    name: String,
    course_name: String,
) {


    val scope = rememberCoroutineScope()
    var content by remember { mutableStateOf("") }
    var chaptername by remember { mutableStateOf("") }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Scaffold(
            topBar = {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(painterResource(Res.drawable.back), "back")
                }
            },
            bottomBar = {
                Button(onClick = {
                    scope.launch {
                        //修改为课堂路径
                        try {
                            client.post("/chapter/add") {
                                contentType(ContentType.Application.Json)
                                setBody(ChapterAddRequest(course_name, chaptername, content))
                            }
                        } catch (_: Exception) { }
                        navController.popBackStack()
                    }
                }) {
                    Text("创建章节")
                }
            },
            modifier = Modifier.fillMaxHeight().widthIn(max = 700.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(it),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        OutlinedTextField(
                            chaptername,
                            onValueChange = { chaptername = it },
                            placeholder = { Text("章节名称") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent,
                                errorBorderColor = Color.Transparent,
                            ),
                            textStyle = MaterialTheme.typography.titleLarge
                        )
                        OutlinedTextField(
                            content,
                            onValueChange = { content = it },
                            placeholder = { Text("章节内容") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent,
                                errorBorderColor = Color.Transparent,
                            ),
                            textStyle = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}