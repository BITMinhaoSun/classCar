package org.example.demo.reply


import kotlinx.serialization.Serializable
//这个文件规定了搜索和添加回复的请求格式
@Serializable
data class ReplySearchRequest(
    val name: String,
    val content: String,
    val course_name: String,
)

@Serializable
data class ReplyAddRequest(
    val name: String,
    val content: String,
    val course_name: String,
    val reply_content: String,
    val reply_name: String,
)
@Serializable
data class ReplySearchResponse(
    val name: String,
    val content: String,
    val course_name: String,
    val reply_content: String,
    val reply_name: String
)

@Serializable
data class ReplyDeleteRequest(
    val name: String,
    val content: String,
    val course_name: String,
    val reply_content: String,
    val reply_name: String,
)