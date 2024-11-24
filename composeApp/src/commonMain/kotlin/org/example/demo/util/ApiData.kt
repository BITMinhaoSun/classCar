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

@Serializable
data class ChapterResponse(
    val course_name: String,
    val name: String,
    val content: String
)
@Serializable
data class ChapterRequest(
    val course_name: String,
)
@Serializable
data class ChapterAddRequest(
    val course_name: String,
    val name: String,
    val content: String
)

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

@Serializable
data class UploadFileRequest(
    val baseName: String,
    val extension: String,
    val file: ByteArray
)

@Serializable
data class FileDescriptionResponse(
    val id: Int,
    val baseName: String,
    val extension: String
)

@Serializable
data class DownloadFileResponse(
    val file: ByteArray
)

@Serializable
data class SearchLessonRequest(
    val course_id:Int ,
)

@Serializable
data class AddLessonRequest(
    val course_id: Int ,
    val name: String,
    val description: String
)
@Serializable
data class SearchLessonResponse(
    val course_id: Int ,
    val name: String,
    val description: String,
    val lesson_id: Int,
)

@Serializable
data class StudentofCoursesRequest(
    val course_id: Int,
)

@Serializable
data class StudentofCourseResponse(
    val name:String,
)

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
data class SearchInfoRequest(
    val name: String,
)


@Serializable
data class SearchInfoResponse(
    val name: String,
    val school: String,
    val phone_number: String,
    val e_mail: String,
    val avatar: Int,
)
@Serializable
data class UpdateInfoRequest(
    val name: String,
    val school: String,
    val phone_number: String,
    val e_mail: String,
    val avatar: Int,
)
@Serializable
data class UpdateInfoResponse(
    val name: String,
    val school: String,
    val phone_number: String,
    val e_mail: String,
    val avatar: Int,
)
@Serializable
data class DeleteQuestionRequest(
    val id: Int,
)
