package org.example.demo.questions

import kotlinx.serialization.Serializable

@Serializable
data class QuestionResponse(
    val id: Int,
    val name: String,
    val description: String,
    val teacher: String
)

@Serializable
data class QuestionRequest(
    val name: String,
    val description: String,
    val teacher: String
)

@Serializable
data class QuestionsRequest(
    val name: String,
)