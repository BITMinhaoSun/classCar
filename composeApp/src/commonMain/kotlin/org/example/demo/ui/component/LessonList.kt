package org.example.demo.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.demo.util.*
import org.jetbrains.compose.ui.tooling.preview.Preview
//
//@OptIn(ExperimentalMaterialApi::class)
//@Composable
//@Preview
//fun LessonList(
//    navController: NavController,
//    id: Int,
//    courseName: String,
//    description: String,
//    teacher: String,
//    name: String,
//    role: String
//) {
//    val lessons = remember { mutableStateListOf<LessonsResponse>() }
//    var initialized:Boolean by remember { mutableStateOf(false) }
//    var refreshing by remember { mutableStateOf(false) }
//    var loading by remember { mutableStateOf(false) }
//    val scope = rememberCoroutineScope()
//    fun refresh() = scope.launch {
//        if (refreshing) {
//            return@launch
//        }
//        refreshing = true
//        initialized = false
//
//        //把lesson路径补上
//        val result: List<LessonsResponse> = client.post("/$role/courses/0") {
//            contentType(ContentType.Application.Json)
//            setBody(LessonsRequest(name))
//        }.body()
//        lessons.clear()
//        delay(100L)
//        result.forEach {
//            lessons.add(it)
//        }
//        refreshing = false
//        initialized = true
//    }
//
//    fun load() = scope.launch {
//        if(initialized) {
//            if (loading) {
//                return@launch
//            }
//            loading = true
//            //换上lesson路径
//            val result: List<LessonsResponse> = client.post("/$role/courses/${lessons.size}"){
//                contentType(ContentType.Application.Json)
//                setBody(LessonsRequest(name))
//            }.body()
//            result.forEach {
//                lessons.add(it)
//            }
//            loading = false
//        }
//    }
//
//    val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = ::refresh)
//    val loadState = rememberPullRefreshState(refreshing = loading, onRefresh = ::load)
//
//
//    Scaffold(
//        floatingActionButton = {
//            if (role == "teacher") {
//                Button(onClick = {
//                    navController.navigate("createLessonPage")
//                }) {
//                    Text("+ 创建课堂")
//                }
//            }
//        },
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            LazyColumn(modifier = Modifier.fillMaxSize()) {
//                (1..10).reversed().forEach {
//                    item {
//                        Column(modifier = Modifier.padding(5.dp)) {
//                            Text("X月X日", modifier = Modifier.padding(5.dp))
//                            Card(modifier = Modifier.padding(5.dp).fillParentMaxWidth().clickable {
//                                navController.navigate("classPage")
//                            }) {
//                                Text("第${it}周", modifier = Modifier.padding(5.dp))
//                            }
//                        }
//                    }
//                }
////            item {
////                Text("refresh", color = Color.Gray,
////                    modifier = Modifier.clickable { refresh() }
////                )
////            }
////            items(lessons) {lesson ->
////                LessonCard(
////                    lesson.name,
////                    lesson.id,
////                    lesson.teacher,
////                    modifier = Modifier.padding(5.dp).fillMaxWidth().clickable {
////                        navController.navigate("classPage/${lesson.id}/${lesson.name}/${lesson.description}/${lesson.teacher}")
////                    }
////                )
////            }
////            item {
////                Text("load more", color = Color.Gray, modifier = Modifier.clickable { load() })
////                LaunchedEffect(Unit) { load() }
////            }
//            }
//            PullRefreshIndicator(refreshing = refreshing, state = pullRefreshState, Modifier.align(Alignment.TopCenter))
//            PullRefreshIndicator(
//                refreshing = loading,
//                state = loadState,
//                Modifier.align(Alignment.BottomCenter).rotate(180f)
//            )
//        }
//    }
//
//    LaunchedEffect(Unit) { refresh() }
//}

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun LessonList(
    navController: NavController,
    id: Int,
    courseName: String,
    description: String,
    teacher: String,
    name: String,
    role: String
) {
    val lessons = remember { mutableStateListOf<SearchLessonResponse>() }
    val scope = rememberCoroutineScope()
    fun search() = scope.launch {

        val result: List<SearchLessonResponse> = client.post("/lesson/search") {
            contentType(ContentType.Application.Json)
            setBody(SearchLessonRequest(id))

        }.body()
        if (result!= null && result.isNotEmpty()) {
            for (discuss in result) {
                println("讨论课程: ${discuss.course_id}")
                println("讨论名称: ${discuss.name}")
                println("讨论内容: ${discuss.description}")
                println("------------------------")
            }
        } else {
            println("未查询到任何课堂内容")
        }
        lessons.clear()
        delay(100L)
        result.forEach {
            lessons.add(it)
        }
    }

    Scaffold(
        floatingActionButton = {
            if (role == "teacher") {
                Button(onClick = {
                    println("going to createLessonPage" + id)
                    println("id的类型是: ${id::class.simpleName}")
                    navController.navigate("createLessonPage/${id}")
                    println("创建课程")
                }) {
                    Text("创建课程")
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(lessons) { lesson ->
                    LessonCard(
                        navController,
                        id = lesson.lesson_id,
                        lessonName = lesson.name,
                        description = lesson.description,
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
fun LessonCard(
    navController: NavController,
    lessonName: String,
    id: Int,
    description: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.clickable {
        // 在这里定义点击后的逻辑，此处仅示例打印日志
        println("Hello world,Im going to some Unkown Pages ")
        navController.navigate("lessonDetailPage/${id}/${lessonName}/${description}")
    }) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text( lessonName, style = MaterialTheme.typography.titleMedium)
            Text(description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}