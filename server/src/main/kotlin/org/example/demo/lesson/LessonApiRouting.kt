package org.example.demo.lesson

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.lessonsRouting(){
    routing {
        route("/teacher") {

//            post("/lessons/{num}") {
//                val req = call.receive<LessonsRequest>()
//                val lessons = lessonDao.getLessonsOfTeacher(req.name, call.parameters["num"]!!.toInt(), 10).map {
//                    LessonResponse(
//                        it.id,
//                        it.name,
//                        it.description,
//                        it.course_id
//                    )
//                }
//                call.respond(lessons)
//            }

            post("lesson/create") {
                val req = call.receive<LessonRequest>()
                lessonDao.createLesson(
                    req.name,
                    req.description,
                    req.course_id
                )
            }
        }
        route("/student") {
            post("/lessons/{num}") {
                val req = call.receive<LessonsRequest>()
                val lessons = lessonDao.getLessonsOfStudent(req.name, call.parameters["num"]!!.toInt(), 10).map{
                    LessonResponse(
                        it.id,
                        it.name,
                        it.description,
                        it.course_id
                    )
                }
                call.respond(lessons)
            }
            post("/lesson/join") {
                val req = call.receive<JoinLessonRequest>()
                lessonDao.joinLesson(req.student, req.lesson)
            }
        }
    }
}