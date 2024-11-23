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
            composable("createDiscussPage/{course_name}") {
                    backStackEntry ->
                val course_name = backStackEntry.arguments?.getString("course_name")!!
                CreateDiscussPage(course_name=course_name,navController=navController,name=name) // CreateLessonPage 是一个 @Composable 函数，表示创建课程页面内容
            }
            composable("discussDetail/{course_name}/{name}/{content}/{reply_name}") {
                    backStackEntry ->
                val course_name = backStackEntry.arguments?.getString("course_name")!!
                val reply_name = backStackEntry.arguments?.getString("reply_name")!!
                val name = backStackEntry.arguments?.getString("name")!!
                val content = backStackEntry.arguments?.getString("content")!!
                DiscussDetailPage(reply_name=reply_name,course_name=course_name,name=name,navController=navController,content=content) // CreateLessonPage 是一个 @Composable 函数，表示创建课程页面内容
            }
            composable("createReplyPage/{course_name}/{name}/{content}/{reply_name}") {
                    backStackEntry ->
                val course_name = backStackEntry.arguments?.getString("course_name")!!
                val reply_name = backStackEntry.arguments?.getString("reply_name")!!
                val name = backStackEntry.arguments?.getString("name")!!
                val content = backStackEntry.arguments?.getString("content")!!
                CreateReplyPage(
                    course_name = course_name,
                    navController = navController,
                    name = name,
                    reply_name = reply_name,
                    content = content,
                ) // CreateLessonPage 是一个 @Composable 函数，表示创建课程页面内容
            }
            composable("createchapterPage/{course_name}") {
                    backStackEntry ->
                val course_name = backStackEntry.arguments?.getString("course_name")!!
                CreateChapterPage(course_name=course_name,navController=navController,name=name) // CreateLessonPage 是一个 @Composable 函数，表示创建课程页面内容
            }
            composable("chapterDetail/{course_name}/{name}/{content}/{keypoint_name}") {
                    backStackEntry ->
                val course_name = backStackEntry.arguments?.getString("course_name")!!
                val keypoint_name = backStackEntry.arguments?.getString("keypoint_name")!!
                val name = backStackEntry.arguments?.getString("name")!!
                val content = backStackEntry.arguments?.getString("content")!!
                ChapterDetailPage(keypoint_name=keypoint_name,course_name=course_name,name=name,navController=navController,content=content) // CreateLessonPage 是一个 @Composable 函数，表示创建课程页面内容
            }
            composable("createkeypointPage/{course_name}/{name}/{content}/{keypoint_name}") {
                    backStackEntry ->
                val course_name = backStackEntry.arguments?.getString("course_name")!!
                val keypoint_name = backStackEntry.arguments?.getString("keypoint_name")!!
                val name = backStackEntry.arguments?.getString("name")!!
                val content = backStackEntry.arguments?.getString("content")!!
                CreateKeypointPage(
                    course_name = course_name,
                    navController = navController,
                    name = name,
                    keypoint_name = keypoint_name,
                    content = content,
                ) // CreateLessonPage 是一个 @Composable 函数，表示创建课程页面内容
            }
//            composable("createDiscussPage/{id}/{name}/{description}/{teacher}") {backStackEntry ->
//                val id = backStackEntry.arguments?.getString("id")!!.toInt()
//                val courseName = backStackEntry.arguments?.getString("name")!!
//                val description = backStackEntry.arguments?.getString("description")!!
//                val teacher = backStackEntry.arguments?.getString("teacher")!!
//                DiscussDetailPage(
//                    navController = navController,
//                    id = id,
//                    courseName = courseName,
//                    description = description,
//                    teacher = teacher,
//                    name = name,
//                    role = role
//                )
//            }
        }
    }
}