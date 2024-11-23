package org.example.demo.keypoint


import kotlinx.serialization.Serializable
//这个文件规定了搜索和添加回复的请求格式
@Serializable
data class KeypointSearchRequest(
    val name: String,
    val content: String,
    val course_name: String,
)

@Serializable
data class KeypointAddRequest(
    val name: String,
    val content: String,
    val course_name: String,
    val keypoint_content: String,
    val keypoint_name: String,
)
@Serializable
data class KeypointSearchResponse(
    val name: String,
    val content: String,
    val course_name: String,
    val keypoint_content: String,
    val keypoint_name: String
)
