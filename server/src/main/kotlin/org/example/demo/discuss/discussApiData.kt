package org.example.demo.discuss

import kotlinx.serialization.Serializable
//这个文件规定了搜索和添加讨论的请求格式
@Serializable
data class SearchRequest(
    val num:Int
)

@Serializable
data class AddRequest(
    val name: String,
    val content: String
)
@Serializable
data class SearchResponse(
    val name: String,
    val content: String,
)