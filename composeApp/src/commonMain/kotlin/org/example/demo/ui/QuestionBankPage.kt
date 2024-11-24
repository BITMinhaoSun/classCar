package org.example.demo.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
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
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import io.ktor.client.call.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import org.example.demo.util.*

import org.jetbrains.compose.resources.painterResource


@Composable
@Preview
fun QuestionBankPage(
    name:String,
    role:String,
    navController: NavController,
    reference: Boolean,
    onClickQuestion: suspend CoroutineScope.(Int) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        val questions = remember { mutableStateListOf<QuestionsResponse>() }
        var filted_questions = remember { mutableStateListOf<QuestionsResponse>() }
        val scope = rememberCoroutineScope()
        Scaffold(
            topBar = {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(painterResource(Res.drawable.back), "back")
                }
            },
            bottomBar = {
                if (!reference) {
                    NavigationBar {
                        NavigationBarItem(
                            icon = {
                                Icon(painterResource(Res.drawable.course), "course")
                            },
                            label = { Text("课程") },
                            selected = false,
                            onClick = { navController.navigate("coursePage") }
                        )
                        NavigationBarItem(
                            icon = {
                                Icon(painterResource(Res.drawable.question_bank), "question_bank")
                            },
                            label = { Text("题库") },
                            selected = true,
                            onClick = { navController.navigate("questionBankPage/${false}/${-1}") }
                        )
                        NavigationBarItem(
                            icon = {
                                Icon(painterResource(Res.drawable.my), "my")
                            },
                            label = { Text("我的") },
                            selected = false,
                            onClick = { navController.navigate("myInfoPage") }
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxHeight().widthIn(max = 700.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(it),
                contentAlignment = Alignment.TopCenter
            ) {
                val courses = remember { mutableStateListOf<CourseResponse>() }
                var expandedCourse by remember { mutableStateOf(false) }
                var selectedCourse by remember { mutableStateOf(" ") }
                var selectedCourse_id by remember { mutableStateOf(-1) }
                // -1代表全部
                val lessons = remember { mutableStateListOf<SearchLessonResponse>() }
                var selectedLesson by remember { mutableStateOf("") }
                var selectedLesson_id by remember { mutableStateOf(-1) }
                //-1代表全部
                var expandedLesson by remember { mutableStateOf(false) }
                var buttonClicked by remember { mutableStateOf(false) }
                var delete_buttonClicked by remember { mutableStateOf(false) }
                var text by remember { mutableStateOf("") }
                Column {
                    Text(text = "搜索题目: $text")
                    Row() {
                    TextField(
                        value = text,
                        onValueChange = { newText ->
                            text = newText
                            // 在这里可以进行文字检测相关的逻辑处理
                            // 例如，检查是否包含特定字符、是否符合某种格式等
                            if (newText.contains("error")) {
                                // 如果检测到特定字符，可进行相应提示或操作
                                // 这里简单打印一条信息
                                println("检测到错误相关字符")
                            }
                        },
                        modifier = Modifier.weight(0.8f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                        Button(onClick = {
                            buttonClicked = true
                        }) {
                            Text("搜索")
                        }
                    }


                    //=============================================================================
                    //课程下拉栏从这开始
                    // 课程下拉栏


                    fun searchCourse() = scope.launch {
                        //  selectedCourse_id = -1

                        val result: List<CourseResponse> = client.post("/$role/courses/0") {
                            contentType(ContentType.Application.Json)
                            setBody(CoursesRequest(name))
                        }.body()
                        courses.clear()
                        delay(100L)
                        result.forEach {
                            courses.add(it)
                        }
                    }
                    OutlinedTextField(
                        value = selectedCourse,
                        onValueChange = { selectedCourse = it },
                        enabled = false,
                        label = { Text("选择课程") },
                        trailingIcon = {
                            IconButton(onClick = {
                                searchCourse()
                                expandedCourse = true
                            }) {
                                Icon(
                                    painterResource(Res.drawable.avatar),
                                    "avatar",
                                    modifier = Modifier.size(10.dp)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 0.dp).clickable {
                            searchCourse()
                            expandedCourse = true
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.DarkGray,
                            disabledLabelColor = Color.DarkGray,
                            disabledTrailingIconColor = Color.DarkGray,
                        )
                    )
                    DropdownMenu(
                        expanded = expandedCourse,
                        onDismissRequest = { expandedCourse = false },
                        modifier = Modifier.width(100.dp)
                    ) {
                        courses.forEach { course ->
                            DropdownMenuItem(
                                text = { Text(course.name) },
                                onClick = {
                                    selectedCourse = course.name
                                    selectedCourse_id = course.id
                                    expandedCourse = false
                                }
                            )
                        }
                        //选择全部，对于这些参数更新为默认值
                        DropdownMenuItem(
                            text = { Text("全部课程") },
                            onClick = {
                                selectedCourse = "全部课程"
                                selectedCourse_id = -1
                                expandedCourse = false
                                selectedLesson_id = -1
                                selectedLesson = " "
                            }
                        )

                    }
                    //===========================================上面是课程的

                    fun searchLesson() = scope.launch {
                        val result: List<SearchLessonResponse> = client.post("/lesson/search") {
                            contentType(ContentType.Application.Json)
                            setBody(SearchLessonRequest(selectedCourse_id))
                        }.body()
                        lessons.clear()
                        delay(100L)
                        result.forEach {
                            lessons.add(it)
                        }
                    }
                    LaunchedEffect(selectedCourse_id) {
                        scope.launch {
                            selectedLesson = " "
                            selectedLesson_id = -1
                        }
                        }
                    if (selectedCourse_id != -1) {
                        // 课堂下拉栏
                        OutlinedTextField(
                            value = selectedLesson,
                            onValueChange = { selectedLesson = it },
                            enabled = false,
                            label = { Text("选择课堂") },
                            trailingIcon = {
                                IconButton(onClick = {
                                    expandedLesson = true
                                    searchLesson()
                                }) {
                                    Icon(
                                        painterResource(Res.drawable.avatar),
                                        "avatar",
                                        modifier = Modifier.size(10.dp)
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth().clickable {
                                expandedLesson = true
                                searchLesson()
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = Color.DarkGray,
                                disabledLabelColor = Color.DarkGray,
                                disabledTrailingIconColor = Color.DarkGray,
                            )
                        )
                        DropdownMenu(
                            expanded = expandedLesson,
                            onDismissRequest = { expandedLesson = false },
                            modifier = Modifier.width(100.dp)
                        ) {
                            lessons.forEach { lesson ->
                                DropdownMenuItem(
                                    text = { Text(lesson.name) },
                                    onClick = {
                                        selectedLesson = lesson.name
                                        selectedLesson_id = lesson.lesson_id
                                        expandedLesson = false
                                    }
                                )
                            }
                            DropdownMenuItem(
                                text = { Text("全部课堂") },
                                onClick = {
                                    selectedLesson = "全部课堂"
                                    selectedLesson_id = -1
                                    expandedLesson = false
                                }
                            )

                        }
                    }


                    //课堂到上面结束
                    //===================================================
                    //selectedLesson_id
                    //selectedCourse_id
                    LaunchedEffect(  selectedLesson_id,
                        selectedCourse_id,buttonClicked,delete_buttonClicked) {
                        scope.launch {
                            buttonClicked = false
                            delete_buttonClicked = false

                            questions.clear()
                            if(selectedCourse_id == -1){
                                val result0: List<QuestionsResponse> = client.get("/${role}/questionsBank/all").body()
                                questions.clear()
                                result0.forEach {question ->
                                if(question.description.contains(text)){
                                    questions.add(question)
                                }
                                }
                            }
                            if(selectedCourse_id != -1
                                && selectedLesson_id == -1){
                                val result0: List<QuestionsResponse> = client.get("/${role}/questionsBank/$selectedCourse_id").body()
                                questions.clear()
//                                result0.forEach {
//                                    questions.add(it)
//                                }
                                result0.forEach {question ->
                                    if(question.description.contains(text)){
                                        questions.add(question)
                                    }
                                }
                            }
                            if(selectedCourse_id != -1
                                && selectedLesson_id != -1){
                                val result0: List<QuestionsResponse> = client.get("/${role}/questions/$selectedLesson_id").body()
                                questions.clear()
//                                result0.forEach {
//                                    questions.add(it)
//                                }
                                result0.forEach {question ->
                                    if(question.description.contains(text)){
                                        questions.add(question)
                                    }
                                }
                            }
                        }
                    }
                    //考虑不用这种方法
//                    LaunchedEffect(text,questions) {
//                                //搜索有问题
//                        scope.launch {
//                            val Text = text
//                            filted_questions.clear()
//                           // filted_questions = questions
//                            questions.forEach { question ->
//                                val description = question.description // 同样将描述信息转换为小写
//                                if (description.contains(Text)) {
//                                    filted_questions.add(question)
//                                }
//                            }
//                        }
//                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxHeight().widthIn(max = 700.dp)
                    ) {
                        items(questions) { question->
                            QuestionBriefCard(
                                question.id,
                                question.description,
                                role,
                                onClick = {
                                    if (reference) {
                                        scope.launch {
                                            onClickQuestion(question.id)
                                        }
                                    }
                                },
                                onDelete = {
                                    println("=======================")
                                    println(question.id)
                                    println("=======================")
                                    println("Request Body: ${DeleteQuestionRequest(question.id)}")
                                    scope.launch {
                                        client.post("/teacher/question/delete/${question.id}")

                                    }
                                    delete_buttonClicked = true
                                },
                                Modifier.padding(5.dp).fillMaxWidth(),
                                question.released,
                                question.closed
                            )
                        }
                    }
                    //
                    // 题目列表
//                    LazyColumn(contentPadding = PaddingValues(5.dp)) {
//                        item {
//                            Card(modifier = Modifier.padding(5.dp).clickable {
//                                navController.navigate("QuestionPage")
//                            }) {
//                                Column(
//                                    modifier = Modifier.fillMaxWidth().padding(5.dp)
//                                ) {
//                                    Text("完整的计算机系统由（　　）组成。")
//                                    Text("[图片]")
//                                }
//                            }
//                        }
//                        (2..10).forEach {
//                            item {
//                                Card(modifier = Modifier.padding(5.dp).clickable {
//                                    navController.navigate("QuestionPage")
//                                }) {
//                                    Column(
//                                        modifier = Modifier.fillMaxWidth().padding(5.dp)
//                                    ) {
//                                        Text("题目$it")
//                                        Text("[图片]")
//                                    }
//                                }
//                            }
//                        }
//                    }

                }
                if(role == "teacher"){
                    if (selectedLesson_id != -1) {
                        Button(
                            onClick = {
                                navController.navigate("createQuestionPage/${selectedLesson_id}")

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
    }
}
