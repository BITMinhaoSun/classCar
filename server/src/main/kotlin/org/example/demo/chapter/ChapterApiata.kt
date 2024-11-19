package org.example.demo.chapter

import kotlinx.serialization.Serializable

@Serializable
data class ChapterResponse(
    val id: Int,
    val name: String,
    val description: String,
    val teacher: String
)

@Serializable
data class ChapterRequest(
    val name: String,
    val description: String,
    val teacher: String
)

@Serializable
data class ChaptersRequest(
    val name: String,
)