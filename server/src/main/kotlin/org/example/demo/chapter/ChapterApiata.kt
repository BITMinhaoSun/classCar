package org.example.demo.chapter

import kotlinx.serialization.Serializable
//这个文件规定了搜索和添加讨论的请求格式
@Serializable
data class ChapterSearchRequest(
    val course_name: String,
)

@Serializable
data class ChapterAddRequest(
    val course_name: String,
    val name: String,
    val content: String
)
@Serializable
data class ChapterSearchResponse(
    val course_name: String,
    val name: String,
    val content: String,
)