package org.example.demo.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.student
import demo.composeapp.generated.resources.teacher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun StarterPage(
    navController: NavController,
    onSelect: (String) -> Unit
) {
    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxHeight().widthIn(max = 700.dp)
        ) {
            Text("欢迎使用雪课堂", style = MaterialTheme.typography.headlineLarge)
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {

                OutlinedButton(
                    onClick = { onSelect("teacher") },
                    shape = RoundedCornerShape(5.dp),
                    border = null
                ) {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Box(modifier = Modifier.height(150.dp).clip(RoundedCornerShape(5.dp))) {
                            Image(painterResource(Res.drawable.teacher), "teacher")
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text("我是老师", color = Color.Black)
                    }
                }

                OutlinedButton(
                    onClick = { onSelect("student") },
                    shape = RoundedCornerShape(5.dp),
                    border = null
                ) {
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Box(modifier = Modifier.height(150.dp).clip(RoundedCornerShape(5.dp))) {
                            Image(painterResource(Res.drawable.student), "student")
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text("我是学生", color = Color.Black)
                    }
                }
            }
        }
    }
}