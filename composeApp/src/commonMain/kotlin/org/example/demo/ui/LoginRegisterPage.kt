package org.example.demo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.invisible
import demo.composeapp.generated.resources.visible
import demo.composeapp.generated.resources.back
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import org.example.demo.util.LoginRequest
import org.example.demo.util.RegisterRequest
import org.example.demo.util.client
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun LoginPage(
    navController: NavController,
    onLogin: (String) -> Unit,
    role: String
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var name: String by remember { mutableStateOf("") }
    val roleName = when (role) {
        "teacher" -> { "老师" }
        "student" -> { "学生" }
        else -> { "" }
    }
    var password: String by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }

    var loggingIn by remember { mutableStateOf(false) }
    fun login() = scope.launch {
        loggingIn = true
        val result = client.post("/$role/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(name, password))
        }
        scope.launch { snackbarHostState.showSnackbar(result.bodyAsText(), duration = SnackbarDuration.Short) }
        // 如果登录成功
        if (result.status == HttpStatusCode.OK) {
            onLogin(name)
            navController.navigate("coursePage")
        }
        loggingIn = false
    }
    val loginState = rememberPullRefreshState(refreshing = loggingIn, onRefresh = ::login)

    Box {
        IconButton(
            onClick = { navController.popBackStack() }
        ) {
            Icon(painterResource(Res.drawable.back), "back")
        }
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${roleName}登录", style = MaterialTheme.typography.headlineSmall)
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = { Text("用户名") },
                placeholder = { Text("请输入用户名") },
                singleLine = true,
            )
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = { Text("密码") },
                placeholder = { Text("请输入密码") },
                singleLine = true,
                visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation('●'),
                trailingIcon = {
                    if (visible) {
                        IconButton(onClick = { visible = false }) {
                            Icon(painterResource(Res.drawable.visible), "visible")
                        }
                    } else {
                        IconButton(onClick = { visible = true }) {
                            Icon(painterResource(Res.drawable.invisible), "invisible")
                        }
                    }
                },
            )
            Button(onClick = { login() }) {
                Text("登录")
            }
            OutlinedButton(
                onClick = { navController.navigate("registerPage") },
                border = null
            ) {
                Text("没有账号？注册")
            }
            SnackbarHost(hostState = snackbarHostState)
        }
        PullRefreshIndicator(refreshing = loggingIn, state = loginState, Modifier.align(Alignment.TopCenter))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun RegisterPage(
    navController: NavController,
    role: String
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var name: String by remember { mutableStateOf("") }
    val roleName = when (role) {
        "teacher" -> { "老师" }
        "student" -> { "学生" }
        else -> { "" }
    }
    var password: String by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }

    var registering by remember { mutableStateOf(false) }
    fun register() = scope.launch {
        registering = true
        val result = client.post("/$role/register") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(name, password))
        }
        scope.launch { snackbarHostState.showSnackbar(result.bodyAsText(), duration = SnackbarDuration.Short) }
        // 如果注册成功
        if (result.status == HttpStatusCode.OK) {
            navController.popBackStack()
        }
        registering = false
    }
    val registerState = rememberPullRefreshState(refreshing = registering, onRefresh = ::register)

    Box {
        IconButton(
            onClick = { navController.popBackStack() }
        ) {
            Icon(painterResource(Res.drawable.back), "back")
        }
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("注册新${roleName}", style = MaterialTheme.typography.headlineSmall)
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                },
                label = { Text("用户名") },
                placeholder = { Text("请输入用户名") },
                singleLine = true,
            )
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = { Text("密码") },
                placeholder = { Text("请输入密码") },
                singleLine = true,
                visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation('●'),
                trailingIcon = {
                    if (visible) {
                        IconButton(onClick = { visible = false }) {
                            Icon(painterResource(Res.drawable.visible), "visible")
                        }
                    } else {
                        IconButton(onClick = { visible = true }) {
                            Icon(painterResource(Res.drawable.invisible), "invisible")
                        }
                    }
                },
            )
            Button(onClick = { register() }) {
                Text("注册")
            }
            SnackbarHost(hostState = snackbarHostState)
        }
        PullRefreshIndicator(refreshing = registering, state = registerState, Modifier.align(Alignment.TopCenter))
    }
}