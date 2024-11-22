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
import org.example.demo.util.DiscussRequest
import org.example.demo.util.DiscussResponse
import org.example.demo.util.client
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun DiscussList(
    navController: NavController,
    id: Int,
    courseName: String,
    description: String,
    teacher: String,
    name: String,
    role: String
) {
    val discusses = remember { mutableStateListOf<DiscussResponse>() }
    val scope = rememberCoroutineScope()
    fun search() = scope.launch {

        val result: List<DiscussResponse> = client.post("/discuss/search") {
            contentType(ContentType.Application.Json)
            setBody(DiscussRequest(courseName))

        }.body()
        if (result!= null && result.isNotEmpty()) {
            for (discuss in result) {
                println("讨论课程: ${discuss.course_name}")
                println("讨论名称: ${discuss.name}")
                println("讨论内容: ${discuss.content}")
                println("------------------------")
            }
        } else {
            println("未查询到任何讨论内容")
        }
        discusses.clear()
        delay(100L)
        result.forEach {
            discusses.add(it)
        }
    }

    Scaffold(
        floatingActionButton = {
            Button(onClick = {
                val course_name = courseName
                println(course_name)
                navController.navigate("createDiscussPage/${course_name}")
                println("创建论坛贴")
            }) {
                Text("发布帖子")
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(discusses) { discuss ->
                    DiscussCard(
                        navController,
                        name=discuss.name,
                        courseName = courseName,
                        content = discuss.content,
                        replyName =name,
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
fun DiscussCard(
    navController: NavController,
    content: String,
    courseName: String,
    name:  String,
    replyName: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable {
            // 在这里定义点击后的逻辑，此处仅示例打印日志
            println("Hello world")
            navController.navigate("discussDetail/${courseName}/${name}/${content}/${replyName}")
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