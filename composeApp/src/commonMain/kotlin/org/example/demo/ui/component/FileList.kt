package org.example.demo.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
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
import demo.composeapp.generated.resources.delete
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.core.PlatformDirectory
import io.github.vinceglb.filekit.core.baseName
import io.github.vinceglb.filekit.core.extension
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.demo.getPlatform
import org.example.demo.util.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun FileList(
    navController: NavController,
    id: Int,
    courseName: String,
    description: String,
    teacher: String,
    name: String,
    role: String
) {
    val listState = rememberLazyListState()
    val files = remember { mutableStateListOf<FileDescriptionResponse>() }
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
        val result: List<FileDescriptionResponse> = client.get("/file/description/${id}/0").body()
        files.clear()
        delay(100L)
        result.forEach {
            files.add(it)
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
            val result: List<FileDescriptionResponse> = client.get("/file/description/${id}/${files.size}").body()
            result.forEach {
                files.add(it)
            }
            loading = false
        }
    }

    var directory: PlatformDirectory? by remember { mutableStateOf(null) }

    val filePicker = rememberFilePickerLauncher(
        title = "选择上传的资料"
    ) {
        it?.let {
            scope.launch {
                refreshing = true
                client.post("/file/upload/${id}") {
                    contentType(ContentType.Application.Json)
                    setBody(UploadFileRequest(
                        it.baseName,
                        it.extension,
                        it.readBytes()
                    ))
                }
                refreshing = false
                delay(100L)
                refresh()
            }
        }
    }

    val directoryPicker = rememberDirectoryPickerLauncher(
        title = "选择保存文件的位置",
        initialDirectory = "./download",
    ) {
        directory = it
    }

    val fileSaver = rememberFileSaverLauncher {}

    val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = ::refresh)
    val loadState = rememberPullRefreshState(refreshing = loading, onRefresh = ::load)

    Scaffold(
        floatingActionButton = {
            if (role == "teacher") {
                Button(onClick = {
                    filePicker.launch()
                }) {
                    Text("+ 上传资料")
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                state = listState,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.pullRefresh(pullRefreshState, enabled = getPlatform().name == "Android").fillMaxHeight().widthIn(max = 700.dp)
            ) {
                item {
                    Text("refresh", color = Color.Gray,
                        modifier = Modifier.clickable { refresh() }
                    )
                }

                items(files) {file ->
                    FileCard(
                        file.baseName,
                        file.extension,
                        file.id,
                        role,
                        onClick = {
                            scope.launch {
                                refreshing = true
                                val downloadFile: DownloadFileResponse = client.get("/file/download/${it}").body()
                                fileSaver.launch(
                                    bytes = downloadFile.file,
                                    baseName = file.baseName,
                                    extension = file.extension,
                                    initialDirectory = directory?.path
                                )
                                refreshing = false
                            }
                        },
                        onDelete = {
                            scope.launch {
                                client.delete("/file/delete/${it}")
                                delay(100L)
                                refresh()
                            }
                        },
                        Modifier.padding(5.dp).fillMaxWidth()
                    )
                }

                item {
                    Text("load more", color = Color.Gray, modifier = Modifier.clickable { load() })
                    LaunchedEffect(Unit) { load() }
                }
            }

            PullRefreshIndicator(refreshing = refreshing, state = pullRefreshState, Modifier.align(Alignment.TopCenter))
            PullRefreshIndicator(
                refreshing = loading,
                state = loadState,
                Modifier.align(Alignment.BottomCenter).rotate(180f)
            )
        }
        LaunchedEffect(Unit) { refresh() }
    }
}

@Composable
fun FileCard(
    baseName: String,
    extension: String,
    fileId: Int,
    role: String,
    onClick: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    modifier: Modifier,
) {
    Card(modifier = modifier.clickable {
        onClick(fileId)
    }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.padding(5.dp)
            ) {
                Text(baseName, style = MaterialTheme.typography.bodyMedium)
                Text("类型: $extension", style = MaterialTheme.typography.bodySmall)
            }
            if (role == "teacher") {
                IconButton(onClick = { onDelete(fileId) }) {
                    Icon(painterResource(Res.drawable.delete), "delete")
                }
            }
        }
    }
}