package org.example.demo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import demo.composeapp.generated.resources.qr_code
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.demo.ui.component.*
import org.example.demo.util.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import qrgenerator.qrkitpainter.rememberQrKitPainter

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

        var showQrCode by remember { mutableStateOf(false) }
        if (showQrCode) {
            AlertDialog(
                onDismissRequest = {
                    showQrCode = false
                },
                confirmButton = {
                },
                dismissButton = {
                    Button(onClick = {
                        showQrCode = false
                    }) {
                        Text("关闭")
                    }
                },
                title = {
                    Text("扫码加入课程")
                },
                text = {
                    val painter = rememberQrKitPainter(data = id.toString())
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painter,
                            contentDescription = null,
                            alignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            )
        }
        Scaffold(
            topBar = {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(painterResource(Res.drawable.back), "back")
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("课程码：$id", modifier = Modifier.padding(5.dp))
                        Spacer(modifier = Modifier.width(20.dp))
                        IconButton(onClick = {
                            showQrCode = true
                        }) {
                            Icon(painterResource(Res.drawable.qr_code), "qr_code")
                        }
                        Text("显示二维码", color = Color.Gray)
                    }
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
                        Tab(
                            selected = selected == 4,
                            onClick = { selected = 4 }
                        ) {
                            Text("学生", modifier = Modifier.padding(10.dp))
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
                        4 -> {
                            StudentList(
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