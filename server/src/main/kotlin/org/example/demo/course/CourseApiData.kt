package org.example.demo.course

import kotlinx.serialization.Serializable

@Serializable
data class CourseResponse(
    val id: Int,
    val name: String,
    val description: String,
    val teacher: String
)

@Serializable
data class CourseRequest(
    val name: String,
    val description: String,
    val teacher: String
)

@Serializable
data class CoursesRequest(
    val name: String,
)

@Serializable
data class JoinCourseRequest(
    val student: String,
    val course: Int
)

@Serializable
data class StudentofCoursesRequest(
    val course_id: Int,
)

@Serializable
data class StudentofCourseResponse(
    val name:String,
)