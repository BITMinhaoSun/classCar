package org.example.demo.course

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.demo.lesson.lessonDao

fun Application.courseRouting() {
    routing {
        route("/course"){
            post("/delete_course/{id}") {
                try {
                    val course_id = (call.parameters["id"])?.toInt()


                    println("CourseApiRouting")
                    println("delete course")
                    println(course_id)

                    try {
                        // 调用挂起函数删除记录
                        course_id?.let { courseDao.deleteCourse(it) }

                        call.respond(HttpStatusCode.OK, "Course deleted successfully")
                    } catch (e: Exception) {
                        // 捕获异常并返回错误信息
                        call.respond(HttpStatusCode.InternalServerError, "Failed to delete course: ${e.message}")
                    }
                } catch (_: Exception) {

                }
            }
        }
        route("/teacher") {
            post("/courses/{num}") {
                try {
                    val req = call.receive<CoursesRequest>()
                    val courses = courseDao.getCoursesOfTeacher(req.name, call.parameters["num"]!!.toInt(), 10).map {
                        CourseResponse(
                            it.id,
                            it.name,
                            it.description,
                            it.creator
                        )
                    }
                    call.respond(courses)
                } catch (_: Exception) {

                }
            }
            post("/course/create") {
                try {
                    val req = call.receive<CourseRequest>()
                    courseDao.createCourse(
                        req.name,
                        req.description,
                        req.teacher
                    )
                } catch (_: Exception) {

                }
            }
            post("/course/change") {
                try {
                    val req = call.receive<CourseChange>()
                    courseDao.changeCourse(
                        req.course_name,
                        req.courseDescription,
                        req.id
                    )
                } catch (_: Exception) {

                }
            }
            post("/studentofcourse") {
                try {
                    val req = call.receive<StudentofCoursesRequest>()
                    val courses = courseDao.getStudentsOfCourse(
                        req.course_id,
                        //call.parameters["num"]!!.toInt(), 10
                    ).map {
                        StudentofCourseResponse(
                            it.name,
                        )
                    }
                    call.respond(courses)
                } catch (_: Exception) {

                }
            }
        }
        route("/student") {
            post("/courses/{num}") {
                try {
                    val req = call.receive<CoursesRequest>()
                    val courses = courseDao.getCoursesOfStudent(req.name, call.parameters["num"]!!.toInt(), 10).map {
                        CourseResponse(
                            it.id,
                            it.name,
                            it.description,
                            it.creator
                        )
                    }
                    call.respond(courses)
                } catch (_: Exception) {

                }
            }
            post("/course/join") {
                try {
                    val req = call.receive<JoinCourseRequest>()
                    courseDao.joinCourse(req.student, req.course)
                } catch (_: Exception) {

                }
            }
            delete("/delete/{courseId}/{studentName}") {
                try {
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
                } catch (_: Exception) {

                }
            }

        }
    }
}