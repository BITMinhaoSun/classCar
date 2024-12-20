package org.example.demo.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.delete
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.demo.util.StudentofCoursesRequest
import org.example.demo.util.StudentofCourseResponse
import org.example.demo.util.client
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun StudentList(
    navController: NavController,
    id: Int,
    courseName: String,
    description: String,
    teacher: String,
    name: String,
    role: String
) {
    val students = remember { mutableStateListOf<StudentofCourseResponse>() }
    val scope = rememberCoroutineScope()
    fun search() = scope.launch {

        try {
            val result: List<StudentofCourseResponse> = client.post("/teacher/studentofcourse") {
                contentType(ContentType.Application.Json)
                setBody(StudentofCoursesRequest(id))

            }.body()
            if (result != null && result.isNotEmpty()) {
                for (stu in result) {
                    println("学生: ${stu.name}")
                    println("------------------------")
                }
            } else {
                println("未查询到任何学生")
            }
            students.clear()
            delay(100L)
            result.forEach {
                students.add(it)
            }
        } catch (_: Exception) { }
    }
    fun deleteStudent(courseId: Int,studentName:String) = scope.launch {
        println("delete student $courseId")
        println(studentName)
        try {
            client.delete("/student/delete/$courseId/$studentName")
        } catch (_: Exception) { }
        delay(100L)
        search() // 删除后刷新列表
    }
    Scaffold(
        floatingActionButton = {
            if (role == "teacher") {
                Button(onClick = {
//                println("going to createLessonPage"+id)
//                println("id的类型是: ${id::class.simpleName}")
                navController.navigate("createStudentofCoursePage/${id}")
//                println("创建课程")
                }) {
                    Text("拉取学生")
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(students) { student ->
                    StudentCard(
                        navController,
                        id = id,
                        studentName = student.name,
                        role =role,
                        onDelete = { deleteStudent(id,student.name) },
                        modifier = Modifier.padding(5.dp).fillMaxWidth().clickable {
                            print("")
                        },

                        )
                }
            }
        }
    }
    LaunchedEffect(Unit) { search() }
}

@Composable
fun StudentCard(
    navController: NavController,
    studentName: String,
    id: Int,
    role: String,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier
        .clickable {
        // 在这里定义点击后的逻辑，此处仅示例打印日志
        println("Hello world,Im going to some Unkown Pages ")
        //   navController.navigate("discussDetail/${courseName}/${name}/${content}/${replyName}")
    }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(studentName, style = MaterialTheme.typography.titleMedium)
            }
            if (role == "teacher") {
                IconButton(
                    onClick = {
                        onDelete(

                        )
                    }) {
//                    println("1111111111")
                    Icon(painterResource(Res.drawable.delete), "delete")
                }
            }
        }
    }
}