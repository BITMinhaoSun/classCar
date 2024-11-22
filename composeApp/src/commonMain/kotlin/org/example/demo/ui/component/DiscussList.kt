package org.example.demo.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.demo.util.DiscussRequest
import org.example.demo.util.DiscussResponse
import org.example.demo.util.client
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun DiscussList(
    navController: NavController,
    id: Int,
    courseName: String,
    description: String,
    teacher: String,
    name: String,
    role: String
) {
    val discusses = remember { mutableStateListOf<DiscussResponse>() }
    val scope = rememberCoroutineScope()
    fun search() = scope.launch {

        val result: List<DiscussResponse> = client.post("/discuss/search") {
            contentType(ContentType.Application.Json)
            setBody(DiscussRequest(10))

        }.body()
        if (result!= null && result.isNotEmpty()) {
            for (discuss in result) {
                println("讨论名称: ${discuss.name}")
                println("讨论内容: ${discuss.content}")
                println("------------------------")
            }
        } else {
            println("未查询到任何讨论内容")
        }
        discusses.clear()
        delay(100L)
        result.forEach {
            discusses.add(it)
        }
    }

    Box(
        Modifier.fillMaxSize()
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(discusses) { discuss ->
                DiscussCard(
                    discuss.name,
                    discuss.content,
                    modifier = Modifier.padding(5.dp).fillMaxWidth().clickable {
                        print("")
                    }
                )
            }
        }
    }
    LaunchedEffect(Unit) { search() }
}

@Composable
fun DiscussCard(
    content: String,
    name:  String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text( name, style = MaterialTheme.typography.titleMedium)
            Text(content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}