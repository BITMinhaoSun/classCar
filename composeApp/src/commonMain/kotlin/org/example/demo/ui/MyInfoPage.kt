package org.example.demo.ui
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.back
import demo.composeapp.generated.resources.teacher
import demo.composeapp.generated.resources.avatar
import demo.composeapp.generated.resources.course
import demo.composeapp.generated.resources.my
import demo.composeapp.generated.resources.question_bank
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun MyInfoPage(navController: NavController) {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Scaffold(
//            topBar = {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    IconButton(
//                        onClick = { navController.popBackStack() },
//                        modifier = Modifier.padding(start = 8.dp)
//                    ) {
//                        Icon(painterResource(Res.drawable.back), "back")
//                    }
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        "个人主页",
//                        style = MaterialTheme.typography.titleMedium,
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
//                }
//            },
//            modifier = Modifier.fillMaxHeight().widthIn(max = 700.dp)
//        ) {
//            Box(
//                modifier = Modifier.fillMaxSize().padding(it),
//                contentAlignment = Alignment.TopCenter
//            ) {
//                // 添加头像和昵称
////                Image(
////                    painterResource(Res.drawable.avatar), "teacher",
////                    modifier = Modifier.size(80.dp)
////                )
////                Spacer(modifier = Modifier.height(8.dp))
////                Text("昵称：你的昵称", style = MaterialTheme.typography.bodyMedium)
////                Spacer(modifier = Modifier.height(16.dp))
//                //Text("个人主页")
//                //移动到topbar上,美化头像界面
//                //202411/11
//                Scaffold(
//                    topBar = {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            IconButton(
//                                onClick = { navController.popBackStack() },
//                                modifier = Modifier.padding(start = 8.dp)
//                            ) {
//                                Icon(painterResource(Res.drawable.back), "back")
//                            }
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(
//                                "个人主页",
//                                style = MaterialTheme.typography.titleMedium,
//                                modifier = Modifier.padding(vertical = 8.dp)
//                            )
//                        }
//                    },
//                    bottomBar = {
//                    },
//                    modifier = Modifier.fillMaxHeight().widthIn(max = 700.dp)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(16.dp),
//                        verticalArrangement = Arrangement.Top,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        // 添加头像和昵称
//                        Image(
//                            painterResource(Res.drawable.avatar),
//                            contentDescription = "Avatar",
//                            modifier = Modifier
//                                .size(80.dp)
//                                .shadow(elevation = 4.dp, shape = CircleShape)
//                        )
//                        Spacer(modifier = Modifier.height(8.dp))
//                        Text(
//                            "昵称：你的昵称",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = MaterialTheme.colorScheme.onSurface
//                        )
//                        Spacer(modifier = Modifier.height(16.dp))
//                    }
//                }
//            }
//        }
//    }
//}
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
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
                        label ={ Text("课程") },
                        selected = false,
                        onClick = {navController.navigate("coursePage")}
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(painterResource(Res.drawable.question_bank), "question_bank")
                        },
                        label ={ Text("题库") },
                        selected = false,
                        onClick = { navController.navigate("questionBankPage") }
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(painterResource(Res.drawable.my), "my")
                        },
                        label ={ Text("我的") },
                        selected = true,
                        onClick = { navController.navigate("myInfoPage") }
                    )
                }
            },
            modifier = Modifier.fillMaxHeight().widthIn(max = 700.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 添加头像和昵称
                Image(
                    painterResource(Res.drawable.avatar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(80.dp)
                        .graphicsLayer(
                            shadowElevation = 4f,
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "昵称：你的昵称",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}