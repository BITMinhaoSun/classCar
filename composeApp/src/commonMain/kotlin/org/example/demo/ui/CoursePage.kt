package org.example.demo.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import demo.composeapp.generated.resources.*
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.course
import demo.composeapp.generated.resources.my
import demo.composeapp.generated.resources.question_bank
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.demo.getPlatform
import org.example.demo.util.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
@Preview
fun CoursePage(navController: NavController, name: String, role: String) {
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(painterResource(Res.drawable.back), "back")
                }
                Text("课程")
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = {
                        Icon(painterResource(Res.drawable.course), "course")
                    },
                    label ={ Text("课程") },
                    selected = true,
                    onClick = {navController.navigate("coursePage")}
                )
                NavigationBarItem(
                    icon = {
                        Icon(painterResource(Res.drawable.question_bank), "question_bank")
                    },
                    label ={ Text("题库") },
                    selected = false,
                    onClick = { navController.navigate("questionBankPage") }
                )
                NavigationBarItem(
                    icon = {
                        Icon(painterResource(Res.drawable.my), "my")
                    },
                    label ={ Text("我的") },
                    selected = false,
                    onClick = { navController.navigate("myInfoPage") }
                )
            }
        },
        floatingActionButton = {
            Button(onClick = {
                when (role) {
                    "teacher" -> {navController.navigate("createCoursePage")}
                    "student" -> {showDialog = true}
                }
            }) {
                when (role) {
                    "teacher" -> { Text("+ 创建课程") }
                    "student" -> { Text("+ 加入课程") }
                }

            }
        }
    ) {
        val listState = rememberLazyListState()
        val courses = remember { mutableStateListOf<CourseResponse>() }
        var initialized:Boolean by remember { mutableStateOf(false) }
        var refreshing by remember { mutableStateOf(false) }
        var loading by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        fun refresh() = scope.launch {
            if (refreshing) {
                return@launch
            }
            refreshing = true
            initialized = false
            val result: List<CourseResponse> = client.post("/$role/courses/0") {
                contentType(ContentType.Application.Json)
                setBody(CoursesRequest(name))
            }.body()
            courses.clear()
            delay(100L)
            result.forEach {
                courses.add(it)
            }
            refreshing = false
            initialized = true
        }

        fun load() = scope.launch {
            if(initialized) {
                if (loading) {
                    return@launch
                }
                loading = true
                val result: List<CourseResponse> = client.post("/$role/courses/${courses.size}"){
                    contentType(ContentType.Application.Json)
                    setBody(CoursesRequest(name))
                }.body()
                result.forEach {
                    courses.add(it)
                }
                loading = false
            }
        }

        val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = ::refresh)
        val loadState = rememberPullRefreshState(refreshing = loading, onRefresh = ::load)


        var joinCourseId by remember { mutableStateOf(0) }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    joinCourseId = 0
                    showDialog = false
                },
                confirmButton = {
                    Button(onClick = {
                        scope.launch {
                            client.post("/student/course/join") {
                                contentType(ContentType.Application.Json)
                                setBody(JoinCourseRequest(name, joinCourseId))
                            }
//                            delay(200L)
                            showDialog = false
                            refresh()
                        }
                    }) {
                        Text("加入")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        joinCourseId = 0
                        showDialog = false
                    }) {
                        Text("取消")
                    }
                },
                title = {
                    Text("输入课堂ID加入课堂")
                },
                text = {
                    TextField(
                        value = joinCourseId.toString(),
                        onValueChange = { value: String ->
                            value.toIntOrNull()?.let { intValue ->
                                joinCourseId = intValue
                            }
                        }
                    )
                }
            )
        }

        Box(
            modifier = Modifier.fillMaxSize().padding(it),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                state = listState,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.pullRefresh(pullRefreshState, enabled = getPlatform().name == "Android").fillMaxHeight().widthIn(max = 700.dp)
            ) {
                item {
                    Text("refresh", color = Color.Gray,
                        modifier = Modifier.clickable { refresh() }
                    )
                }
                items(courses) {course ->
                    CourseCard(
                        course.name,
                        course.id,
                        course.teacher,
                        modifier = Modifier.padding(5.dp).fillMaxWidth().clickable {
                            navController.navigate("courseDetailPage/${course.id}/${course.name}/${course.description}/${course.teacher}")
                        }
                    )
                }
                item {
                    Text("load more", color = Color.Gray, modifier = Modifier.clickable { load() })
                    LaunchedEffect(Unit) { load() }
                }
            }
            PullRefreshIndicator(refreshing = refreshing, state = pullRefreshState, Modifier.align(Alignment.TopCenter))
            PullRefreshIndicator(
                refreshing = loading,
                state = loadState,
                Modifier.align(Alignment.BottomCenter).rotate(180f)
            )
        }
        LaunchedEffect(Unit) { refresh() }
    }
}

@Composable
fun CourseCard(
    courseName: String,
    id: Int,
    teacherName: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(courseName, style = MaterialTheme.typography.titleMedium)
            Text("ID: $id", style = MaterialTheme.typography.bodySmall)
            Text(teacherName, style = MaterialTheme.typography.bodyMedium)
        }
    }
}