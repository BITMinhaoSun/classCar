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
import org.example.demo.util.CourseChange
import org.example.demo.util.CourseRequest
import org.example.demo.util.client
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun ChangeCoursePage(
    navController: NavController,
    name: String,
    role: String,
    course_name:String ,
    courseDescription:String,
    id:Int
) {
    println(course_name)
    println(courseDescription)
    val scope = rememberCoroutineScope()
    var courseName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

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
                        try {
                            client.post("/teacher/course/change") {
                                contentType(ContentType.Application.Json)
                                setBody(CourseChange(courseName, description, id))
                            }
                        } catch (_: Exception) { }
                        navController.popBackStack()
                    }
                }) {
                    Text("修改课程")
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
                            courseName,
                            onValueChange = { courseName = it },
                            placeholder = { Text(course_name) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent,
                                errorBorderColor = Color.Transparent,
                            ),
                            textStyle = MaterialTheme.typography.titleLarge
                        )
                        OutlinedTextField(
                            description,
                            onValueChange = { description = it },
                            placeholder = { Text(courseDescription) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent,
                                errorBorderColor = Color.Transparent,
                            )
                        )
                    }
                }
            }
        }
    }
}