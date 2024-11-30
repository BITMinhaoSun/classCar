package org.example.demo.chapter

import org.example.demo.discuss.SearchRequest
import org.example.demo.discuss.AddRequest
import org.example.demo.discuss.Discuss
import org.example.demo.discuss.discussDao

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.Flow
import org.example.demo.course.CourseResponse
import org.example.demo.course.CoursesRequest
import org.example.demo.courseFile.fileDao
import org.example.demo.discuss.discussDao

fun Application.ChapterRouting() {
    routing {
        ///chapter/search
        route("/chapter") {
            post("/search") {
                try {
                    val req = call.receive<ChapterSearchRequest>()
                    val courses = chapterDao.getchapter(
                        req.course_name
                    ).map {
                        ChapterSearchResponse(
                            it.course_name,
                            it.name,
                            it.content,
                        )
                    }
                    call.respond(courses)
                } catch (_: Exception) {

                }
            }
            post("/add") {
                try {
                    val req = call.receive<ChapterAddRequest>()
                    chapterDao.addchapter(
                        Chapter(
                            course_name = req.course_name,
                            name = req.name,
                            content = req.content
                        )
                    )
                } catch (_: Exception) {

                }
            }
            delete("/delete/{chapterName}/{courseName}") {
                try {
                    val courseName = call.parameters["courseName"]
                    val chapterName = call.parameters["chapterName"]
                    println("ChapterApiRouting")
                    println("delete chapter")
                    println(courseName)
                    println(chapterName)
                    if (courseName.isNullOrBlank() || chapterName.isNullOrBlank()) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            "Invalid parameters: courseName and chapterName must be provided"
                        )
                        return@delete
                    }

                    try {
                        // 调用挂起函数删除记录
                        chapterDao.deleteChapter(courseName, chapterName)

                        call.respond(HttpStatusCode.OK, "Chapter deleted successfully")
                    } catch (e: Exception) {
                        // 捕获异常并返回错误信息
                        call.respond(HttpStatusCode.InternalServerError, "Failed to delete chapter: ${e.message}")
                    }
                } catch (_: Exception) {

                }
            }
        }
    }
}
