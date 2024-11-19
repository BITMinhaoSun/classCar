package org.example.demo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import demo.composeapp.generated.resources.Res
import demo.composeapp.generated.resources.back
import org.jetbrains.compose.resources.painterResource

@Composable
fun AnswerStatisticsPage(navController: NavController) {
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
            bottomBar = { },
            modifier = Modifier.fillMaxHeight().widthIn(max = 700.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text("完整的计算机系统由（　）组成。", fontSize = MaterialTheme.typography.titleMedium.fontSize)
                Spacer(modifier = Modifier.height(8.dp))
                var selectedOption by remember { mutableStateOf("") }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedOption == "A",
                        onClick = { selectedOption = "A" }
                    )
                    Text("A．运算器、控制器、存储器、输入设备和输出设备", modifier = Modifier.padding(start = 8.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedOption == "B",
                        onClick = { selectedOption = "B" }
                    )
                    Text("B．主机和外部设备", modifier = Modifier.padding(start = 8.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedOption == "C",
                        onClick = { selectedOption = "C" }
                    )
                    Text("C．硬件系统和软件系统", modifier = Modifier.padding(start = 8.dp))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedOption == "D",
                        onClick = { selectedOption = "D" }
                    )
                    Text("D．主机箱、显示器、键盘、鼠标、打印机", modifier = Modifier.padding(start = 8.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("答题情况统计图：", fontSize = MaterialTheme.typography.titleMedium.fontSize)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("A: ", modifier = Modifier.padding(start = 8.dp))
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(10.dp)
                            .background(if (selectedOption == "A") Color.Green else Color.Gray)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("B: ", modifier = Modifier.padding(start = 8.dp))
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(10.dp)
                            .background(if (selectedOption == "B") Color.Green else Color.Gray)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("C: ", modifier = Modifier.padding(start = 8.dp))
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(10.dp)
                            .background(if (selectedOption == "C") Color.Green else Color.Gray)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("D: ", modifier = Modifier.padding(start = 8.dp))
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(10.dp)
                            .background(if (selectedOption == "D") Color.Green else Color.Gray)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        // 这里可以处理确认选择的逻辑
                        println("Selected option: $selectedOption")
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("确认")
                }
            }
        }
    }
}