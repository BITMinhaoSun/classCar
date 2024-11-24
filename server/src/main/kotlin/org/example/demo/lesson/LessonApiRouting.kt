package org.example.demo.lesson

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.demo.discuss.*


fun Application.lessonsRouting(){
    routing {
//        route("/teacher") {
//
////            post("/lessons/{num}") {
////                val req = call.receive<LessonsRequest>()
////                val lessons = lessonDao.getLessonsOfTeacher(req.name, call.parameters["num"]!!.toInt(), 10).map {
////                    LessonResponse(
////                        it.id,
////                        it.name,
////                        it.description,
////                        it.course_id
////                    )
////                }
////                call.respond(lessons)
////            }
//
//            post("lesson/create") {
//                val req = call.receive<LessonRequest>()
//                lessonDao.createLesson(
//                    req.name,
//                    req.description,
//                    req.course_id
//                )
//            }
//        }
//        route("/student") {
//            post("/lessons/{num}") {
//                val req = call.receive<LessonsRequest>()
//                val lessons = lessonDao.getLessonsOfStudent(req.name, call.parameters["num"]!!.toInt(), 10).map{
//                    LessonResponse(
//                        it.id,
//                        it.name,
//                        it.description,
//                        it.course_id
//                    )
//                }
//                call.respond(lessons)
//            }
////            post("/lesson/join") {
////                val req = call.receive<JoinLessonRequest>()
////                lessonDao.joinLesson(req.student, req.lesson)
////            }
//        }
        routing {
            ///discuss/search
            route("/lesson") {
                post("/search") {
                    println("*-*-*-*-*-*-*")
                    println("code there /lesson/search")
                    println("*-*-*-*-*-*-*")
                    val req =  call.receive<SearchLessonRequest>()
                    val courses = lessonDao.getLessonViaCrouseId(
                        req.course_id
                    ).map {
                        SearchLessonResponse(
                            it.course_id,
                            it.name,
                            it.description,
                            it.id
                        )
                    }
                    call.respond(courses)
                }
                post("/add") {
                    println("*-*-*-*-*-*-*")
                    println("code there /lesson/add")
                    println("*-*-*-*-*-*-*")
                    val req = call.receive<AddLessonRequest>()
                    lessonDao.addLesson(
                        Lesson(
                            course_id = req.course_id,
                            name = req.name,
                            description = req.description,
                            id = 6 //实际这里用不到，因为id是自增的
                        )
                    )
                }
            }
        }
    }
}