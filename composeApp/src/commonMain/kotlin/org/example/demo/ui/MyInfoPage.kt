package org.example.demo.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.back
import demo.composeapp.generated.resources.teacher
import demo.composeapp.generated.resources.avatar
import demo.composeapp.generated.resources.avatar_option1
import demo.composeapp.generated.resources.avatar_option2
import demo.composeapp.generated.resources.avatar_option3
import demo.composeapp.generated.resources.course
import demo.composeapp.generated.resources.my
import demo.composeapp.generated.resources.question_bank
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.demo.util.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun MyInfoPage(navController: NavController,name: String) {
    var schoolName by mutableStateOf("你的学校")
    var studentName by mutableStateOf("姓名")
    var phoneNumber by mutableStateOf("你的手机号码")
    var e_mail by mutableStateOf("你的邮箱")
    var selectedAvatarResource by remember { mutableStateOf(Res.drawable.avatar) } // 当前选中的头像资源

    val avatarOptions = listOf(
        Res.drawable.avatar,
        Res.drawable.avatar_option1,
        Res.drawable.avatar_option2,
        Res.drawable.avatar_option3
    ) // 备选头像资源列表

    var showAvatarDropdown by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
//        val userInfoViewModel: UserInfoViewModel = viewModel()
        Scaffold(
            topBar = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(painterResource(Res.drawable.back), "back")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "个人主页",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            },
            bottomBar = {
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
                        selected = false,
                        onClick = { navController.navigate("questionBankPage/${false}/${-1}") }
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(painterResource(Res.drawable.my), "my")
                        },
                        label = { Text("我的") },
                        selected = true,
                        onClick = { navController.navigate("myInfoPage") }
                    )
                }
            },
            floatingActionButton = {
                Button(onClick = {
                    println("Dont click the button")
                    navController.navigate("updateInfoPage")
                    println("Dont")
                }) {
                    Text("修改信息")
                }
            },
            modifier = Modifier.fillMaxHeight().widthIn(max = 700.dp),

            ) {


            //================================
            val infos = remember { mutableStateListOf<SearchInfoResponse>() }
            val scope = rememberCoroutineScope()
            fun search() = scope.launch {
                try {
                    val result: List<SearchInfoResponse> = client.post("/userInfo/search") {
                        contentType(ContentType.Application.Json)
                        setBody(SearchInfoRequest(name))
                    }.body()
                    infos.clear()
                    delay(100L)
                    result.forEach {
                        infos.add(it)
                        println("添加信息: ${it.name}")
                        schoolName = it.school
                        studentName = it.name
                        //   name = it.name
                        phoneNumber = it.phone_number
                        e_mail = it.e_mail
                        //     selectedAvatarResource by remember { mutableStateOf(Res.drawable.avatar) } // 当前选中的头像资源
                        selectedAvatarResource = avatarOptions.elementAtOrNull(it.avatar) ?: Res.drawable.avatar
                    }
                } catch (_: Exception) { }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(selectedAvatarResource),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(80.dp)
                        .graphicsLayer(
                            shadowElevation = 4f,
                            shape = CircleShape
                        )
                        .clickable {
                            showAvatarDropdown = true
                        }
                )
                Spacer(modifier = Modifier.height(16.dp))
//                Text(
//                    text = "学校：$schoolName",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurface,
//                )
//                Spacer(modifier = Modifier.height(12.dp))
//                Text(
//                    text = "姓名：$studentName",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurface,
//                )
//                Spacer(modifier = Modifier.height(12.dp))
//                Text(
//                    text = "手机号：$phoneNumber",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurface,
//                )
//                Spacer(modifier = Modifier.height(12.dp))
//                Text(
//                    text = "邮箱：$e_mail",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurface,
//                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .animateContentSize() // 内容大小变化时添加动画
                ) {   var isVisible by remember { mutableStateOf(true) } // 控制显示与隐藏状态
                    AnimatedVisibility(visible = isVisible) {
                        Column {
                            FloatingText(text = "早上好！$studentName")
                            OutlinedTextField(
                                value = "姓名：$studentName",
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.padding(2.dp),
                                textStyle = MaterialTheme.typography.bodyMedium,
                            )
                   //         Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = "学校：$schoolName",
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.padding(2.dp),
                                textStyle = MaterialTheme.typography.bodyMedium,
                            )

                            //==

//                                Text(
//                                    text = "学校：$schoolName",
//                                    style = MaterialTheme.typography.bodyMedium,
//                                    color = MaterialTheme.colorScheme.onSurface,
//                                    modifier = Modifier.padding(4.dp)
//                                )

                   //         Spacer(modifier = Modifier.height(12.dp))
//                            Text(
//                                text = "姓名：$studentName",
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = MaterialTheme.colorScheme.onSurface,
//                                modifier = Modifier.padding(4.dp)
//                            )
                    //        Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = "手机号：$phoneNumber",
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.padding(2.dp),
                                textStyle = MaterialTheme.typography.bodyMedium,
                            )
//                            Text(
//                                text = "手机号：$phoneNumber",
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = MaterialTheme.colorScheme.onSurface,
//                                modifier = Modifier.padding(4.dp)
//                            )

                        //    Spacer(modifier = Modifier.height(12.dp))
//                            Text(
//                                text = "邮箱：$e_mail",
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = MaterialTheme.colorScheme.onSurface,
//                                modifier = Modifier.padding(4.dp)
//                            )
                            OutlinedTextField(
                                value = "邮箱：$e_mail",
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.padding(2.dp),
                                textStyle = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 按钮切换动画可见性
                    Button(
                        onClick = { isVisible = !isVisible },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = if (isVisible) "隐藏信息" else "显示信息")
                    }
                }
            }

            LaunchedEffect(Unit) { search() }
            }


        }
    }

@Composable
fun FloatingText(text: String) {
    val offsetX = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        offsetX.animateTo(-1000f, animationSpec = infiniteRepeatable(
            animation = tween(60000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ))
    }

    Text(
        text = text,
        modifier = Modifier.offset(x = offsetX.value.dp),
        color = Color.Black ,
        style = androidx.compose.ui.text.TextStyle(fontSize = 30.sp)
    )
}
