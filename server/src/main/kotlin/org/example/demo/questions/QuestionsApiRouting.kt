package org.example.demo.questions

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.demo.course.CourseRequest
import org.example.demo.course.CourseResponse
import org.example.demo.course.CoursesRequest
import org.example.demo.course.JoinCourseRequest
import org.example.demo.course.courseDao

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
        }
    }
}