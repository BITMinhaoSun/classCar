package org.example.demo.ui




import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.back
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import org.example.demo.util.CourseRequest
import org.example.demo.util.DiscussAddRequest
import org.example.demo.util.ReplyAddRequest
import org.example.demo.util.client
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun CreateReplyPage(
    navController: NavController,
    name: String,
    course_name: String,
    content: String,
    reply_name: String,
) {
    val scope = rememberCoroutineScope()
    var reply_content by remember { mutableStateOf("") }

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
                            client.post("/reply/add") {
                                contentType(ContentType.Application.Json)
                                setBody(
                                    ReplyAddRequest(
                                        course_name = course_name,
                                        name = name,
                                        reply_content = reply_content,
                                        content = content,
                                        reply_name = reply_name,
                                    )
                                )
                            }
                        } catch (_: Exception) { }
                        navController.popBackStack()
                    }
                }) {
                    Text("回复论坛贴")
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
                            reply_content,
                            onValueChange = { reply_content = it },
                            placeholder = { Text("回复内容") },
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