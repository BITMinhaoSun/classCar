package org.example.demo.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.back
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.demo.ui.component.ChapterList
import org.example.demo.ui.component.DiscussList
import org.example.demo.ui.component.FileList
import org.example.demo.ui.component.LessonList
import org.example.demo.util.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun CourseDetailPage(
    navController: NavController,
    id: Int,
    courseName: String,
    description: String,
    teacher: String,
    name: String,
    role: String
) {
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
            },
            floatingActionButton = {
                Column {
                    if (role == "teacher") {
                        Button(onClick = {
                            navController.navigate("createLessonPage")
                        }) {
                            Text("+ 创建课堂")
                        }
                    }
                    Button(onClick = {
                        navController.navigate("createDiscussPage")
                        // 这里添加点击按钮后执行分享页面相关逻辑，示例代码中暂未具体实现分享的具体操作
                        println("执行分享课程页面操作")
                    }) {
                        Text("开始讨论")
                    }
                }
            },
            modifier = Modifier.fillMaxHeight().widthIn(max = 700.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(it),
                contentAlignment = Alignment.Center
            ) {
                var selected by remember { mutableStateOf(0) }

                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        courseName,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(5.dp)
                    )
                    Text("课程码：$id", modifier = Modifier.padding(5.dp))
                    Text("教师：$teacher", modifier = Modifier.padding(5.dp))
                    Text(description, modifier = Modifier.padding(5.dp))
                    TabRow(
                        selectedTabIndex = selected
                    ) {
                        Tab(
                            selected = selected == 0,
                            onClick = { selected = 0 }
                        ) {
                            Text("课堂", modifier = Modifier.padding(10.dp))
                        }
                        Tab(
                            selected = selected == 1,
                            onClick = { selected = 1 }
                        ) {
                            Text("讨论", modifier = Modifier.padding(10.dp))
                        }
                        Tab(
                            selected = selected == 2,
                            onClick = { selected = 2 }
                        ) {
                            Text("章节", modifier = Modifier.padding(10.dp))
                        }
                        Tab(
                            selected = selected == 3,
                            onClick = { selected = 3 }
                        ) {
                            Text("资料", modifier = Modifier.padding(10.dp))
                        }
                    }
                    when (selected) {
                        0 -> {
                            LessonList(
                                navController,
                                id,
                                courseName,
                                description,
                                teacher,
                                name,
                                role
                            )
                        }
                        1 -> {
                            DiscussList(
                                navController,
                                id,
                                courseName,
                                description,
                                teacher,
                                name,
                                role
                            )
                        }
                        2 -> {
                            ChapterList(
                                navController,
                                id,
                                courseName,
                                description,
                                teacher,
                                name,
                                role
                            )
                        }
                        3 -> {
                            FileList(
                                navController,
                                id,
                                courseName,
                                description,
                                teacher,
                                name,
                                role
                            )
                        }
                    }
                }
            }
        }
    }
}