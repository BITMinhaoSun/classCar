package org.example.demo.questions

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import org.example.demo.course.CourseRequest
import org.example.demo.course.CourseResponse
import org.example.demo.course.CoursesRequest
import org.example.demo.course.JoinCourseRequest
import org.example.demo.course.courseDao

fun Application.questionRouting() {
    routing {
        route("/teacher") {
            get("/questions/{lessonId}") {
                val lessonId = call.parameters["lessonId"]!!.toInt()
                val questions = questionDao.getAllQuestionsOfLesson(lessonId).map {
                    QuestionsResponse(
                        id = it.id,
                        description = it.description,
                        options = it.options,
                        answer = it.answer,
                        lessonId = it.lessonId,
                        courseId = it.courseId,
                        released = it.released,
                        closed = it.closed
                    )
                }
                call.respond(questions)
            }
            post("/question/add/{lessonId}") {
                val req = call.receive<AddQuestionRequest>()
                questionDao.addQuestion(
                    req.description,
                    req.options,
                    req.answer,
                    req.lessonId
                )
            }
            get("/questionsBank/{courseId}") {
                val courseId = call.parameters["courseId"]!!.toInt()
                val questions = questionDao.getAllQuestionsOfCourse(courseId).map {
                    QuestionsResponse(
                        id = it.id,
                        description = it.description,
                        options = it.options,
                        answer = "",
                        lessonId = it.lessonId,
                        courseId = it.courseId,
                        released = it.released,
                        closed = it.closed
                    )
                }
                call.respond(questions)
            }
            get("/questionsBank/all") {
                val questions = questionDao.getAllQuestionsOfAll().map {
                    QuestionsResponse(
                        id = it.id,
                        description = it.description,
                        options = it.options,
                        answer = "",
                        lessonId = it.lessonId,
                        courseId = it.courseId,
                        released = it.released,
                        closed = it.closed
                    )
                }
                call.respond(questions)
            }
            post("/question/delete/{id}") {
                val qId = call.parameters["id"]!!.toInt()
                println("================================================")
                println(qId )
                println("================================================")
                questionDao.deleteQuestion(qId)
            }

        }
        route("/student") {
            get("/questions/{lessonId}") {
                val lessonId = call.parameters["lessonId"]!!.toInt()
                val questions = questionDao.getReleasedQuestionsOfLesson(lessonId).map {
                    QuestionsResponse(
                        id = it.id,
                        description = it.description,
                        options = it.options,
                        answer = "",
                        lessonId = it.lessonId,
                        courseId = it.courseId,
                        released = it.released,
                        closed = it.closed
                    )
                }
                call.respond(questions)
            }
            get("/questionsBank/{courseId}") {
                val courseId = call.parameters["courseId"]!!.toInt()
                val questions = questionDao.getReleasedQuestionsOfCourse(courseId).map {
                    QuestionsResponse(
                        id = it.id,
                        description = it.description,
                        options = it.options,
                        answer = "",
                        lessonId = it.lessonId,
                        courseId = it.courseId,
                        released = it.released,
                        closed = it.closed
                    )
                }
                call.respond(questions)
            }
            get("/questionsBank/all") {
                val questions = questionDao.getAllReleasedQuestionsOfAll().map {
                    QuestionsResponse(
                        id = it.id,
                        description = it.description,
                        options = it.options,
                        answer = "",
                        lessonId = it.lessonId,
                        courseId = it.courseId,
                        released = it.released,
                        closed = it.closed
                    )
                }
                call.respond(questions)
            }


        }
    }
}