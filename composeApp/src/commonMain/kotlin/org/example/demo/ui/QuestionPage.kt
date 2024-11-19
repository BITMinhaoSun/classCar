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
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import org.example.demo.util.QuestionRequest
import org.example.demo.util.QuestionResponse
import org.example.demo.util.client
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QuestionPage(navController: NavController,name:String, role:String) {
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
            modifier = Modifier.fillMaxHeight().widthIn(max = 700.dp)
        ) {
            val listState = rememberLazyListState()
            val questions = remember { mutableStateListOf<QuestionResponse>() }
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
                //换成问题内容路径
                val result: List<QuestionResponse> = client.post("/$role/courses/0") {
                    contentType(ContentType.Application.Json)
                    setBody(QuestionRequest(name))
                }.body()
                questions.clear()
                delay(100L)
                result.forEach {
                    questions.add(it)
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
                    //换成问题内容路径
                    val result: List<QuestionResponse> = client.post("/$role/courses/${questions.size}"){
                        contentType(ContentType.Application.Json)
                        setBody(QuestionRequest(name))
                    }.body()
                    result.forEach {
                        questions.add(it)
                    }
                    loading = false
                }
            }

            val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = ::refresh)
            val loadState = rememberPullRefreshState(refreshing = loading, onRefresh = ::load)





            Box(
                modifier = Modifier.fillMaxSize().padding(it),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {

                        Text("完整的计算机系统由（　）组成。")
                        Spacer(modifier = Modifier.height(8.dp))
                        var selectedOption by remember { mutableStateOf("") }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedOption == "A",
                                onClick = { selectedOption = "A" }
                            )
                            Text("A．运算器、控制器、存储器、输入设备和输出设备")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedOption == "B",
                                onClick = { selectedOption = "B" }
                            )
                            Text("B．主机和外部设备")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedOption == "C",
                                onClick = { selectedOption = "C" }
                            )
                            Text("C．硬件系统和软件系统")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedOption == "D",
                                onClick = { selectedOption = "D" }
                            )
                            Text("D．主机箱、显示器、键盘、鼠标、打印机")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        // 根据角色判断显示哪些按钮
                        if (role == "student") {
                            Button(
                                onClick = {
                                    // 这里可以处理确认选择的逻辑
                                    println("Selected option: $selectedOption")
                                },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("确认提交")
                            }
                        } else if (role == "teacher") {
                            Button(
                                onClick = {
                                    // 这里可以处理确认选择的逻辑
                                    println("Selected option: $selectedOption")
                                },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("发布题目")
                            }

                            Button(
                                onClick = {
                                    // 这里可以处理确认选择的逻辑
                                    navController.navigate("AnswerStatisticsPage")
                                },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("查看答题情况")
                            }
                        }
                        //按钮按情况向上面那样分好
//                        Button(
//                            onClick = {
//                                // 这里可以处理确认选择的逻辑
//                                println("Selected option: $selectedOption")
//                            },
//                            modifier = Modifier.align(Alignment.End)
//                        ) {
//                            Text("发布题目")
//                        }
////                        Button(
////                            onClick = {
////                                // 这里可以处理确认选择的逻辑
////                                println("Selected option: $selectedOption")
////                            },
////                            modifier = Modifier.align(Alignment.End)
////                        ) {
////                            Text("确认提交")
////                        }
//                        Button(
//                            onClick = {
//                                // 这里可以处理确认选择的逻辑
//                                navController.navigate("AnswerStatisticsPage")
//                            },
//                            modifier = Modifier.align(Alignment.End)
//                        ) {
//                            Text("查看答题情况")
//                        }
                    }
                }
            }
        }
    }
}