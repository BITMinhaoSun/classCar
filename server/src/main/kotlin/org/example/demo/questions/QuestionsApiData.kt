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
@Serializable
data class DeleteQuestionRequest(
    val id: Int,
)


@Serializable
data class StudentQuestionDetailResponse(
    val id: Int,
    val description: String,
    val options: List<String>,
    val standardAnswer: String,
    val myAnswer: String,
    val lessonId: Int,
    val courseId: Int,
    val released: Boolean,
    val closed: Boolean,
)

@Serializable
data class AnswerQuestionRequest(
    val student: String,
    val questionId: Int,
    val answer: String,
)

@Serializable
data class QuestionStatisticsResponse(
    val option: String,
    val number: Int
)