package org.example.demo.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.demo.util.ChapterRequest
import org.example.demo.util.ChapterResponse
import org.example.demo.util.client
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun ChapterList(
    navController: NavController,
    id: Int,
    courseName: String,
    description: String,
    teacher: String,
    name: String,
    role: String
) {
    val chapters = remember { mutableStateListOf<ChapterResponse>() }
    val scope = rememberCoroutineScope()
    fun search() = scope.launch {

        val result: List<ChapterResponse> = client.post("/chapter/search") {
            contentType(ContentType.Application.Json)
            setBody(ChapterRequest(courseName))

        }.body()
        if (result!= null && result.isNotEmpty()) {
            for (chapter in result) {
                println("课程名称: ${chapter.course_name}")
                println("章节名称: ${chapter.name}")
                println("章节内容: ${chapter.content}")
                println("------------------------")
            }
        } else {
            println("未查询到任何章节内容")
        }
        chapters.clear()
        delay(100L)
        result.forEach {
            chapters.add(it)
        }
    }

    Scaffold(
        floatingActionButton = {
            Button(onClick = {
                val course_name = courseName
                println(course_name)
                navController.navigate("createchapterPage/${course_name}")
                println("创建章节")
            }) {
                Text("添加章节")
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(chapters) { chapter ->
                    ChapterCard(
                        navController,
                        name=chapter.name,
                        courseName = courseName,
                        content = chapter.content,
                        keypoint_name =name,
                        modifier = Modifier.padding(5.dp).fillMaxWidth().clickable {
                            print("")
                        }
                    )
                }
            }
        }
    }
    LaunchedEffect(Unit) { search() }
}


@Composable
fun ChapterCard(
    navController: NavController,
    content: String,
    courseName: String,
    name:  String,
    keypoint_name: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable {
            // 在这里定义点击后的逻辑，此处仅示例打印日志
            println("Hello world")
            navController.navigate("chapterDetail/${courseName}/${name}/${content}/${keypoint_name}")
        }

    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text( name, style = MaterialTheme.typography.titleMedium)
            Text(content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}