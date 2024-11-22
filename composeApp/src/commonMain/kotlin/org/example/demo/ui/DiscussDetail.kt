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
import org.example.demo.ui.component.DiscussCard
import org.example.demo.util.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun DiscussDetailPage(navController: NavController,
                      course_name: String,
                        name: String,
                      content: String,
                      reply_name:String,
) {
    val replies = remember { mutableStateListOf<ReplySearchResponse>() }
    val scope = rememberCoroutineScope()
    fun search() = scope.launch {

        val result: List<ReplySearchResponse> = client.post("/reply/search") {
            contentType(ContentType.Application.Json)
            setBody(ReplySearchRequest(
                content = content,
                name = name,
                course_name = course_name,
            ))

        }.body()
        if (result!= null && result.isNotEmpty()) {
            for (reply in result) {
                println("讨论课程: ${reply.course_name}")
                println("讨论名称: ${reply.name}")
                println("讨论内容: ${reply.content}")
                println("------------------------")
            }
        } else {
            println("未查询到任何讨论内容")
        }
        replies.clear()
        delay(100L)
        result.forEach {
            replies.add(it)
        }
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
                navController.navigate("createReplyPage/${course_name}/${name}/${content}/${reply_name}")
                println("创建论坛贴")
            }) {
                Text("发布回复")
            }
        },
        modifier = Modifier.fillMaxSize(),

    ) {
        Box(
            Modifier.fillMaxSize().padding(top = 56.dp) // 根据实际topBar高度调整
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(replies) { reply ->
                    ReplyCard(
                        navController,
                        name = reply.name,
                        content = reply.reply_content,
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
fun ReplyCard(
    navController: NavController,
    content: String,
    name:  String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier

    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text( name, style = MaterialTheme.typography.titleMedium)
            Text(content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}