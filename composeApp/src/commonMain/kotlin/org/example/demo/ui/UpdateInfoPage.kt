package org.example.demo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.back
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import org.example.demo.util.DiscussAddRequest
import org.example.demo.util.UpdateInfoRequest
import org.example.demo.util.client
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun UpdateInfoPage(
    navController: NavController,
    name: String,
) {
    val scope = rememberCoroutineScope()
    var school by remember { mutableStateOf("") }
    var e_mail by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var avatar by remember { mutableStateOf(1) }
    var expanded by remember { mutableStateOf(false) }
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
                Button(onClick = {
                    scope.launch {
                        //修改为课堂路径
                        try {
                            client.post("/userInfo/update") {
                                contentType(ContentType.Application.Json)
                                setBody(UpdateInfoRequest(name, school, phoneNumber, e_mail, avatar))
                            }
                        } catch (_: Exception) { }
                        navController.popBackStack()
                    }
                }) {
                    Text("修改信息")
                }
            },
            modifier = Modifier.fillMaxHeight().widthIn(max = 700.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(it),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        OutlinedTextField(
                            school,
                            onValueChange = {school = it },
                            placeholder = { Text("学校") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent,
                                errorBorderColor = Color.Transparent,
                            ),
                            textStyle = MaterialTheme.typography.titleLarge
                        )
                    }
                    item {
                        OutlinedTextField(
                            e_mail,
                            onValueChange = {e_mail = it },
                            placeholder = { Text("邮箱") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent,
                                errorBorderColor = Color.Transparent,
                            ),
                            textStyle = MaterialTheme.typography.titleLarge
                        )
                    }
                    item {
                        OutlinedTextField(
                            phoneNumber,
                            onValueChange = {phoneNumber = it },
                            placeholder = { Text("电话号码") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                disabledBorderColor = Color.Transparent,
                                errorBorderColor = Color.Transparent,
                            ),
                            textStyle = MaterialTheme.typography.titleLarge
                        )
                    }
                    item{
                        Column {
                            Button(onClick = { expanded = true }) {
                                Text("选择头像")
                            }
                            DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            // 选项1
                            DropdownMenuItem(onClick = {
                                avatar = 1
                                expanded = false
                            }) {
                                Text("选项1")
                            }
                            // 选项2
                            DropdownMenuItem(onClick = {
                                avatar = 2
                                expanded = false
                            }) {
                                Text("选项2")
                            }
                            // 选项3
                            DropdownMenuItem(onClick = {
                                avatar = 3
                                expanded = false
                            }) {
                                Text("选项3")
                            }
                        }

                        }


                    }

                }
            }
        }
    }
}