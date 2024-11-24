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
import demo.composeapp.generated.resources.delete
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.demo.util.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun ChapterDetailPage(navController: NavController,
                      course_name: String,
                      name: String,
                      content: String,
                      role:String,
                      keypoint_name:String
) {
    val keypoints = remember { mutableStateListOf<KeypointSearchResponse>() }
    val scope = rememberCoroutineScope()
    fun search() = scope.launch {

        val result: List<KeypointSearchResponse> = client.post("/keypoint/search") {
            contentType(ContentType.Application.Json)
            setBody(KeypointSearchRequest(
                content = content,
                name = name,
                course_name = course_name,
            ))

        }.body()
        if (result!= null && result.isNotEmpty()) {
            for (keypoint in result) {
                println("课程名称: ${keypoint.course_name}")
                println("知识点名称: ${keypoint.keypoint_name}")
                println("知识点内容: ${keypoint.keypoint_content}")
                println("------------------------")
            }
        } else {
            println("未查询到任何讨论内容")
        }
        keypoints.clear()
        delay(100L)
        result.forEach {
            keypoints.add(it)
        }
    }
    fun deleteKeypoint(    name: String,
                           content: String,
                           course_name: String,
                           keypoint_content: String,
                           keypoint_name: String) = scope.launch {
        println("delete keypoint $keypoint_content")
        println("delete keypoint $keypoint_name")
        client.post("/keypoint/delete"){
            contentType(ContentType.Application.Json)
            setBody(
                DeleteKeypointRequest(
                    name=name,
                    content=content,
                    course_name=course_name,
                    keypoint_content=keypoint_content,
                    keypoint_name=keypoint_name
                )
            )
        }
        delay(100L)
        search() // 删除后刷新列表
    }
    Scaffold(
        topBar = {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(painterResource(Res.drawable.back), "back")
            }
            //  modifier = Modifier.fillMaxHeight().widthIn(max = 700.dp)

        },
        floatingActionButton = {
            Button(onClick = {
                val course_name = course_name
                navController.navigate("createkeypointPage/${course_name}/${name}/${content}/${keypoint_name}")
                println("创建知识点")
            }) {
                Text("添加知识点")
            }
        },
        modifier = Modifier.fillMaxSize(),

        ) {
        Box(
            Modifier.fillMaxSize().padding(top = 56.dp) // 根据实际topBar高度调整
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(keypoints) { keypoint ->
                    KeypointCard(
                        navController,
                        name = keypoint.keypoint_name,
                        content = keypoint.keypoint_content,
                        role =role,
                        onDelete = { deleteKeypoint(name=keypoint.name,
                            content=keypoint.content,
                            course_name=keypoint.course_name,
                            keypoint_content=keypoint.keypoint_content,
                            keypoint_name=keypoint.keypoint_name) },
                        // content = reply.reply_content,
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
fun KeypointCard(
    navController: NavController,
    content: String,
    name:  String,
    role: String,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(name, style = MaterialTheme.typography.titleMedium)
                Text(content, style = MaterialTheme.typography.bodyMedium)
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