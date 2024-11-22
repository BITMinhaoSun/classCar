package org.example.demo.util

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val name: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val name: String,
    val password: String
)

@Serializable
data class CourseResponse(
    val id: Int,
    val name: String,
    val description: String,
    val teacher: String
)

@Serializable
data class CoursesRequest(
    val name: String,
)

@Serializable
data class CourseRequest(
    val name: String,
    val description: String,
    val teacher: String
)

@Serializable
data class JoinCourseRequest(
    val student: String,
    val course: Int
)

@Serializable
data class LessonsResponse(
    val id: Int,
    val name: String,
    val description: String,
    val teacher: String
)

@Serializable
data class LessonsRequest(
    val name: String,
)

@Serializable
data class LQuestionResponse(
    val name: String,
    val img: String,
)

@Serializable
data class LQuestionRequest(
    val name: String,
)

@Serializable
data class QuestionResponse(
    val name: String,
    val img: String,
)

@Serializable
data class QuestionRequest(
    val name: String,
)
@Serializable
data class DiscussResponse(
    val course_name: String,
    val name: String,
    val content: String
)
@Serializable
data class DiscussRequest(
    val course_name: String,
)
@Serializable
data class DiscussAddRequest(
    val course_name: String,
    val name: String,
    val content: String
)

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
