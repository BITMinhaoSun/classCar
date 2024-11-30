package org.example.demo.questions

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.questionRouting() {
    routing {
        route("/teacher") {
            get("/questions/{lessonId}") {
                try {
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
                } catch (_: Exception) {

                }
            }
            post("/question/add/{lessonId}") {
                try {
                    val req = call.receive<AddQuestionRequest>()
                    questionDao.addQuestion(
                        req.description,
                        req.options,
                        req.answer,
                        req.lessonId
                    )
                } catch (_: Exception) {

                }
            }
            get("/questionsBank/{courseId}") {
                try {
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
                } catch (_: Exception) {

                }
            }
            get("/questionsBank/all") {
                try {
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
                } catch (_: Exception) {

                }
            }
            post("/question/delete/{id}") {
                try {
                    val qId = call.parameters["id"]!!.toInt()
                    println("================================================")
                    println(qId)
                    println("================================================")
                    questionDao.deleteQuestion(qId)
                } catch (_: Exception) {

                }
            }
            post("/question/release/{questionId}") {
                try {
                    val questionId = call.parameters["questionId"]!!.toInt()
                    questionDao.releaseQuestion(questionId)
                } catch (_: Exception) {

                }
            }
            post("/question/close/{questionId}") {
                try {
                    val questionId = call.parameters["questionId"]!!.toInt()
                    questionDao.closeQuestion(questionId)
                } catch (_: Exception) {

                }
            }
            get("/question/statistics/{questionId}") {
                try {
                    val questionId = call.parameters["questionId"]!!.toInt()
                    val stat = questionDao.getStatisticOfSingleQuestion(questionId)
                    val options = questionDao.getSingleQuestion(questionId).options
                    val res = options.map {
                        QuestionStatisticsResponse(it, stat.getOrDefault(it, 0))
                    }
                    call.respond(res)
                } catch (_: Exception) {

                }
            }
            post("/question/clone/{questionId}/{lessonId}") {
                try {
                    val questionId = call.parameters["questionId"]!!.toInt()
                    val lessonId = call.parameters["lessonId"]!!.toInt()
                    questionDao.cloneQuestion(questionId, lessonId)
                } catch (_: Exception) {

                }
            }
        }
        route("/student") {
            get("/questions/{lessonId}") {
                try {
                    val lessonId = call.parameters["lessonId"]!!.toInt()
                    val questions = questionDao.getReleasedQuestionsOfLesson(lessonId).map {
                        QuestionsResponse(
                            id = it.id,
                            description = it.description,
                            options = it.options,
                            answer = if (!it.closed) "" else it.answer,
                            lessonId = it.lessonId,
                            courseId = it.courseId,
                            released = it.released,
                            closed = it.closed
                        )
                    }
                    call.respond(questions)
                } catch (_: Exception) {

                }
            }
            get("/question/detail/{questionId}/{studentName}") {
                try {
                    val questionId = call.parameters["questionId"]!!.toInt()
                    val studentName = call.parameters["studentName"]!!
                    val question = questionDao.getSingleQuestion(questionId)
                    val answer = questionDao.getStudentAnswer(questionId, studentName)
                    val res = StudentQuestionDetailResponse(
                        id = question.id,
                        description = question.description,
                        options = question.options,
                        standardAnswer = if (!question.closed) "" else question.answer,
                        myAnswer = answer ?: "",
                        lessonId = question.lessonId,
                        courseId = question.courseId,
                        released = question.released,
                        closed = question.closed
                    )
                    call.respond(res)
                } catch (_: Exception) {

                }
            }
            post("/question/answer") {
                try {
                    val req = call.receive<AnswerQuestionRequest>()
                    questionDao.answerQuestion(req.questionId, req.student, req.answer)
                } catch (_: Exception) {

                }
            }
            get("/questionsBank/{courseId}") {
                try {
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
                } catch (_: Exception) {

                }
            }
            get("/questionsBank/all") {
                try {
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
                } catch (_: Exception) {

                }
            }
        }
    }
}