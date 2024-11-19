package org.example.demo.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.avatar
import demo.composeapp.generated.resources.back
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.demo.util.CourseRequest
import org.example.demo.util.CourseResponse
import org.example.demo.util.CoursesRequest
import org.example.demo.util.JoinCourseRequest
import org.example.demo.util.LQuestionRequest
import org.example.demo.util.LQuestionResponse
import org.example.demo.util.LessonsRequest
import org.example.demo.util.LessonsResponse
import org.example.demo.util.client
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun ClassPage(navController: NavController,
              name: String,
              role: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        var showDialog by remember { mutableStateOf(false) }
        Scaffold(
            topBar = {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(painterResource(Res.drawable.back), "back")
                }
            },
            bottomBar = {
            },
            floatingActionButton = {
                if (role == "teacher") {
                    Button(onClick = {
                        showDialog = true
                    }) {
                        Text("+ 添加题目")
                    }
                }
            },
            modifier = Modifier.fillMaxHeight().widthIn(max = 700.dp)
        ) {
            val listState = rememberLazyListState()
            val lquestions = remember { mutableStateListOf<LQuestionResponse>() }
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
                //修改为课堂对应题目列表的位置
                val result: List<LQuestionResponse> = client.post("/$role/courses/0") {
                    contentType(ContentType.Application.Json)
                    setBody(LQuestionRequest(name))
                }.body()
                lquestions.clear()
                delay(100L)
                result.forEach {
                    lquestions.add(it)
                }
                refreshing = false
                initialized = true
            }
//
            fun load() = scope.launch {
                if(initialized) {
                    if (loading) {
                        return@launch
                    }
                    loading = true
                    //修改为课程题库列表位置
                    val result: List<LQuestionResponse> = client.post("/$role/courses/${lquestions.size}"){
                        contentType(ContentType.Application.Json)
                        setBody(LQuestionRequest(name))
                    }.body()
                    result.forEach {
                        lquestions.add(it)
                    }
                    loading = false
                }
            }

//            val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = ::refresh)
//            val loadState = rememberPullRefreshState(refreshing = loading, onRefresh = ::load)

            //从这里弹窗选择要添加的题目id
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showDialog = false
                    },
                    confirmButton = {
                        Button(onClick = {
                            scope.launch {
                                //修改为题目加入课堂的地址
//                                client.post("/student/course/join") {
//                                    contentType(ContentType.Application.Json)
//                                    setBody(JoinCourseRequest(name, joinCourseId))
//                                }
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
                            showDialog = false
                        }) {
                            Text("取消")
                        }
                    },
                    title = {
                        Text("输入题目ID加入课堂")
                    },
                    text = {
//                        TextField(
//                            value = joinCourseId.toString(),
//                            onValueChange = { value: String ->
//                                value.toIntOrNull()?.let { intValue ->
//                                    joinCourseId = intValue
//                                }
//                            }
//                        )
                    }
                )
            }



            Box(
                modifier = Modifier.fillMaxSize().padding(it),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(contentPadding = PaddingValues(5.dp)) {
                    (1..10).forEach {
                        item {
                            Card(modifier = Modifier.padding(5.dp).clickable{
                                navController.navigate("QuestionPage")
                            }) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(5.dp)
                                ) {

                                    Text("题目$it")
                                    Text("[图片]")
                                }
                            }
                        }
                    }
                    items(lquestions) {lquestion ->
                        lQuestionCard(
                            lquestion.name,
                            lquestion.img,
                            modifier = Modifier.padding(5.dp).fillMaxWidth().clickable {
                                //要显示具体题目的页面，现在还是统一的
//                                navController.navigate("questionPage/${lquestion.img}/${lquestion.name}")
                                navController.navigate("questionPage")
                            }
                        )
                    }
                }
//                PullRefreshIndicator(refreshing = refreshing, state = pullRefreshState, Modifier.align(Alignment.TopCenter))
//                PullRefreshIndicator(
//                    refreshing = loading,
//                    state = loadState,
//                    Modifier.align(Alignment.BottomCenter).rotate(180f)
//                )
                //把按钮放到前面去了
//                Button(
//                    onClick = {
//                        // 这里添加点击按钮后的导航或其他逻辑，比如导航到添加题目页面
//                        navController.navigate("AddQuestionPage")
//                    },
//                    modifier = Modifier
//                        .align(Alignment.BottomEnd)
//                        .padding(16.dp)
//                ) {
//                    Text("添加题目")
//                }

            }
//            LaunchedEffect(Unit) { refresh() }
        }
    }
}
@Composable
fun lQuestionCard(
    courseName: String,
    teacherName: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Text(courseName, style = MaterialTheme.typography.titleMedium)
            Text(teacherName, style = MaterialTheme.typography.bodyMedium)
        }
    }
}