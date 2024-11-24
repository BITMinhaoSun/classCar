package org.example.demo.questions

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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
            post("/question/release/{questionId}") {
                val questionId = call.parameters["questionId"]!!.toInt()
                questionDao.releaseQuestion(questionId)
            }
            post("/question/close/{questionId}") {
                val questionId = call.parameters["questionId"]!!.toInt()
                questionDao.closeQuestion(questionId)
            }
            get("/question/statistics/{questionId}") {
                val questionId = call.parameters["questionId"]!!.toInt()
                val stat = questionDao.getStatisticOfSingleQuestion(questionId)
                val options = questionDao.getSingleQuestion(questionId).options
                val res = options.map {
                    QuestionStatisticsResponse(it, stat.getOrDefault(it, 0))
                }
                call.respond(res)
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
                        answer = if (!it.closed) "" else it.answer,
                        lessonId = it.lessonId,
                        courseId = it.courseId,
                        released = it.released,
                        closed = it.closed
                    )
                }
                call.respond(questions)
            }
            get("/question/detail/{questionId}/{studentName}") {
                val questionId = call.parameters["questionId"]!!.toInt()
                val studentName = call.parameters["studentName"]!!
                val question = questionDao.getSingleQuestion(questionId)
                val answer = questionDao.getStudentAnswer(questionId, studentName)
                val res = StudentQuestionDetailResponse(
                    id = question.id,
                    description = question.description,
                    options = question.options,
                    standardAnswer = if (!question.closed) "" else question.answer,
                    myAnswer = answer?:"",
                    lessonId = question.lessonId,
                    courseId = question.courseId,
                    released = question.released,
                    closed = question.closed
                )
                call.respond(res)
            }
            post("/question/answer") {
                val req = call.receive<AnswerQuestionRequest>()
                questionDao.answerQuestion(req.questionId, req.student, req.answer)
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