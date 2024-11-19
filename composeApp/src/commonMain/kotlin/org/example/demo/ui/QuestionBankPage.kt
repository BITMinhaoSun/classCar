package org.example.demo.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.avatar
import demo.composeapp.generated.resources.back
import demo.composeapp.generated.resources.course
import demo.composeapp.generated.resources.my
import demo.composeapp.generated.resources.question_bank
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import org.example.demo.util.CourseRequest
import org.example.demo.util.client
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import org.jetbrains.compose.resources.painterResource


@Composable
@Preview
fun QuestionBankPage(navController: NavController) {
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
                NavigationBar {
                    NavigationBarItem(
                        icon = {
                            Icon(painterResource(Res.drawable.course), "course")
                        },
                        label ={ Text("课程") },
                        selected = false,
                        onClick = {navController.navigate("coursePage")}
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(painterResource(Res.drawable.question_bank), "question_bank")
                        },
                        label ={ Text("题库") },
                        selected = true,
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
            modifier = Modifier.fillMaxHeight().widthIn(max = 700.dp)
        ) {
//            Box(
//                modifier = Modifier.fillMaxSize().padding(it),
//                contentAlignment = Alignment.Center
//            ) {
//                LazyColumn(contentPadding = PaddingValues(5.dp)) {
//                    item {
//                        Card(modifier = Modifier.padding(5.dp).clickable {
//                            navController.navigate("QuestionPage")
//                        }) {
//                            Column(
//                                modifier = Modifier.fillMaxWidth().padding(5.dp)
//                            ) {
//                                Text("完整的计算机系统由（　　）组成。")
//                                Text("[图片]")
//                            }
//                        }
//                    }
//                    (2..10).forEach {
//                        item {
//                            Card(modifier = Modifier.padding(5.dp).clickable{
//                                navController.navigate("QuestionPage")
//                            }) {
//                                Column(
//                                    modifier = Modifier.fillMaxWidth().padding(5.dp)
//                                ) {
//                                    Text("题目$it")
//                                    Text("[图片]")
//                                }
//                            }
//                        }
//                    }
//                }
//                Button(
//                    onClick = {
//                        // 这里添加点击按钮后的导航或其他逻辑，比如导航到创建题目页面
//                        navController.navigate("CreateQuestionPage")
//                    },
//                    modifier = Modifier
//                        .align(Alignment.BottomEnd)
//                        .padding(16.dp)
//                ) {
//                    Text("+创建题目")
//                }
//            }
//        }
//    }
//}
            Box(
                modifier = Modifier.fillMaxSize().padding(it),
                contentAlignment = Alignment.TopCenter
            ) {
                Column {
                    // 课程下拉栏
                    var expandedCourse by remember { mutableStateOf(false) }
                    var selectedCourse by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = selectedCourse,
                        onValueChange = { selectedCourse = it },
                        label = { Text("选择课程") },
                        trailingIcon = {
                            IconButton(onClick = { expandedCourse = true }) {
                                Icon(
                                    painterResource(Res.drawable.avatar),
                                    "avatar",
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = expandedCourse,
                        onDismissRequest = { expandedCourse = false },
                        modifier = Modifier.width(100.dp)
                    ) {
                        listOf("课程 1", "课程 2", "课程 3").forEach { course ->
                            DropdownMenuItem(
                                text = { Text(course) },
                                onClick = {
                                    selectedCourse = course
                                    expandedCourse = false
                                }
                            )
                        }
                    }
                    var selectedSection by remember { mutableStateOf("") }
                    if (selectedCourse.isNotEmpty()) {
                        // 章节下拉栏
                        var expandedSection by remember { mutableStateOf(false) }

                        OutlinedTextField(
                            value = selectedSection,
                            onValueChange = { selectedSection = it },
                            label = { Text("选择章节") },
                            trailingIcon = {
                                IconButton(onClick = { expandedSection = true }) {
                                    Icon(
                                        painterResource(Res.drawable.avatar),
                                        "avatar",
                                        modifier = Modifier.size(10.dp)
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        DropdownMenu(
                            expanded = expandedSection,
                            onDismissRequest = { expandedSection = false },
                            modifier = Modifier.width(100.dp)
                        ) {
                            listOf("章节 1", "章节 2", "章节 3").forEach { section ->
                                DropdownMenuItem(
                                    text = { Text(section) },
                                    onClick = {
                                        selectedSection = section
                                        expandedSection = false
                                    }
                                )
                            }
                        }
                    }
                    if (selectedCourse.isNotEmpty() and selectedSection.isNotEmpty()) {
                        // 知识点下拉栏
                        var expandedKnowledgePoint by remember { mutableStateOf(false) }
                        var selectedKnowledgePoint by remember { mutableStateOf("") }
                        OutlinedTextField(
                            value = selectedKnowledgePoint,
                            onValueChange = { selectedKnowledgePoint = it },
                            label = { Text("选择知识点") },
                            trailingIcon = {
                                IconButton(onClick = {
                                    expandedKnowledgePoint = true
                                }) {
                                    Icon(
                                        painterResource(Res.drawable.avatar),
                                        "avatar",
                                        modifier = Modifier.size(10.dp)
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        DropdownMenu(
                            expanded = expandedKnowledgePoint,
                            onDismissRequest = { expandedKnowledgePoint = false },
                            modifier = Modifier.width(100.dp)
                        ) {
                            listOf("知识点 1", "知识点 2", "知识点 3").forEach { knowledgePoint ->
                                DropdownMenuItem(
                                    text = { Text(knowledgePoint) },
                                    onClick = {
                                        selectedKnowledgePoint = knowledgePoint
                                        expandedKnowledgePoint = false
                                    }
                                )
                            }
                        }
                    }
                    // 题目列表
                    LazyColumn(contentPadding = PaddingValues(5.dp)) {
                        item {
                            Card(modifier = Modifier.padding(5.dp).clickable {
                                navController.navigate("QuestionPage")
                            }) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(5.dp)
                                ) {
                                    Text("完整的计算机系统由（　　）组成。")
                                    Text("[图片]")
                                }
                            }
                        }
                        (2..10).forEach {
                            item {
                                Card(modifier = Modifier.padding(5.dp).clickable {
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
                    }

                }
                Button(
                    onClick = {
                        // 这里添加点击按钮后的导航或其他逻辑，比如导航到创建题目页面
                        navController.navigate("CreateQuestionPage")
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Text("+创建题目")
                }
            }
        }
    }
}