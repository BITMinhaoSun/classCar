package org.example.demo

import androidx.compose.material.*
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinx.serialization.Serializable
import org.example.demo.ui.*

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        var role = ""
        var name = ""
        NavHost(
            navController = navController,
            startDestination = "starterPage"
        ) {
            composable("starterPage") {
                StarterPage(
                    navController = navController,
                    onSelect = {
                        role = it
                        navController.navigate("loginPage")
                    }
                )
            }
            composable("loginPage") { LoginPage(navController = navController, role = role, onLogin = { name = it }) }
            composable("registerPage") { RegisterPage(navController = navController, role = role) }
            composable("coursePage") { CoursePage(navController, name, role) }
            composable("courseDetailPage/{id}/{name}/{description}/{teacher}") {backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")!!.toInt()
                val courseName = backStackEntry.arguments?.getString("name")!!
                val description = backStackEntry.arguments?.getString("description")!!
                val teacher = backStackEntry.arguments?.getString("teacher")!!
                CourseDetailPage(
                    navController = navController,
                    id = id,
                    courseName = courseName,
                    description = description,
                    teacher = teacher,
                    name = name,
                    role = role
                )
            }
            composable("createCoursePage") { CreateCoursePage(navController, name, role) }
            composable("myInfoPage") { MyInfoPage(navController) }
            composable("questionBankPage") { QuestionBankPage(navController) }
            composable("classPage") { ClassPage(navController,name, role) }
            composable("questionPage") { QuestionPage(navController,name, role) }
            composable("answerstatisticsPage") { AnswerStatisticsPage(navController) }
            composable("createquestionPage") { CreateQuestionPage(navController,name,role) }
            composable("createlessonPage") { CreateLessonPage(navController,name,role) }
            composable("createDiscussPage") {
                CreateDiscussPage(navController,name) // CreateLessonPage 是一个 @Composable 函数，表示创建课程页面内容
            }
        }
    }
}