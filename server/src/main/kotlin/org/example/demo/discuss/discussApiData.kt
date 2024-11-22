package org.example.demo.discuss

import kotlinx.serialization.Serializable
//这个文件规定了搜索和添加讨论的请求格式
@Serializable
data class SearchRequest(
    val course_name: String,
)

@Serializable
data class AddRequest(
    val course_name: String,
    val name: String,
    val content: String
)
@Serializable
data class SearchResponse(
    val course_name: String,
    val name: String,
    val content: String,
)