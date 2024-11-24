package org.example.demo.questions

import kotlinx.serialization.Serializable

@Serializable
data class QuestionsResponse(
    val id: Int,
    val description: String,
    val options: List<String>,
    val answer: String,
    val lessonId: Int,
    val courseId: Int,
    val released: Boolean,
    val closed: Boolean,
)

@Serializable
data class AddQuestionRequest(
    val description: String,
    val options: List<String>,
    val answer: String,
    val lessonId: Int,
)