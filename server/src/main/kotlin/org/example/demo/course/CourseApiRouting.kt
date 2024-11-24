package org.example.demo.course

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.demo.lesson.lessonDao

fun Application.courseRouting() {
    routing {
        route("/teacher") {
            post("/courses/{num}") {
                val req =  call.receive<CoursesRequest>()
                val courses = courseDao.getCoursesOfTeacher(req.name, call.parameters["num"]!!.toInt(), 10).map {
                    CourseResponse(
                        it.id,
                        it.name,
                        it.description,
                        it.creator
                    )
                }
                call.respond(courses)
            }
            post("/course/create") {
                val req = call.receive<CourseRequest>()
                courseDao.createCourse(
                    req.name,
                    req.description,
                    req.teacher
                )
            }
            post("/studentofcourse") {
                val req =  call.receive<StudentofCoursesRequest>()
                val courses = courseDao.getStudentsOfCourse(
                    req.course_id,
                    //call.parameters["num"]!!.toInt(), 10
                    ).map {
                    StudentofCourseResponse(
                        it.name,
                    )
                }
                call.respond(courses)
            }
        }
        route("/student") {
            post("/courses/{num}") {
                val req =  call.receive<CoursesRequest>()
                val courses = courseDao.getCoursesOfStudent(req.name, call.parameters["num"]!!.toInt(), 10).map {
                    CourseResponse(
                        it.id,
                        it.name,
                        it.description,
                        it.creator
                    )
                }
                call.respond(courses)
            }
            post("/course/join") {
                val req = call.receive<JoinCourseRequest>()
                courseDao.joinCourse(req.student, req.course)
            }
            delete("/delete/{courseId}/{studentName}") {
                val course_id = (call.parameters["courseId"])?.toInt()
                val student_name = call.parameters["studentName"]

                println("CourseApiRouting")
                println("delete course")
                println(course_id)
                println(student_name)

                try {
                    // 调用挂起函数删除记录
                    course_id?.let { student_name?.let { it1 -> courseDao.deleteStudent(it, it1) } }

                    call.respond(HttpStatusCode.OK, "Student deleted successfully")
                } catch (e: Exception) {
                    // 捕获异常并返回错误信息
                    call.respond(HttpStatusCode.InternalServerError, "Failed to delete student: ${e.message}")
                }
            }
        }
    }
}