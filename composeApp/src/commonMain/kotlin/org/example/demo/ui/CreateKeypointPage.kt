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
import org.example.demo.util.KeypointAddRequest
import org.example.demo.util.ReplyAddRequest
import org.example.demo.util.client
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun CreateKeypointPage(
    navController: NavController,
    name: String,
    course_name: String,
    content: String,
    keypoint_name: String,
) {
    val scope = rememberCoroutineScope()
    var keypoint_content by remember { mutableStateOf("") }
    var keypointname by remember { mutableStateOf("") }

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
                        client.post("/keypoint/add") {
                            contentType(ContentType.Application.Json)
                            setBody(
                                KeypointAddRequest(
                                    course_name = course_name,
                                    name = name,
                                    keypoint_content = keypoint_content,
                                    content = content,
                                    keypoint_name= keypointname,
                                )
                            )
                        }
                        navController.popBackStack()
                    }
                }) {
                    Text("创建知识点")
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
                            keypointname,
                            onValueChange = { keypointname = it },
                            placeholder = { Text("知识点名称") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent,
                                errorBorderColor = Color.Transparent,
                            ),
                            textStyle = MaterialTheme.typography.titleLarge
                        )
                        OutlinedTextField(
                            keypoint_content,
                            onValueChange = { keypoint_content = it },
                            placeholder = { Text("知识点内容") },
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