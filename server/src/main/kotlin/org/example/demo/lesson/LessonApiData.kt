package org.example.demo.lesson

import kotlinx.serialization.Serializable
//
//@Serializable
//data class LessonResponse(
//    val id: Int,
//    val name: String,
//    val description: String,
//    val course_id: Int
//)
//
//@Serializable
//data class LessonRequest(
//    val name: String,
//    val description: String,
//    val course_id: Int
//)
//
//@Serializable
//data class LessonsRequest(
//    val name: String,
//)
//
//@Serializable
//data class JoinLessonRequest(
//    val student: String,
//    val lesson: Int
//)

//这个文件规定了搜索和添加课程的请求格式
@Serializable
data class SearchLessonRequest(
    val course_id:Int ,
)

@Serializable
data class AddLessonRequest(
    val course_id: Int ,
    val name: String,
    val description: String
)
@Serializable
data class SearchLessonResponse(
    val course_id: Int ,
    val name: String,
    val description: String,
    val lesson_id: Int,
)