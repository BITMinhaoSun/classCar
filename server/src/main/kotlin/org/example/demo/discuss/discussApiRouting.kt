package org.example.demo.discuss

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
import org.example.demo.discuss.discussDao

fun Application.DiscussRouting() {
    routing {
        ///discuss/search
        route("/discuss") {
            post("/search") {
                val req =  call.receive<SearchRequest>()
                val courses = discussDao.getdiscuss(
                    req.course_name
                ).map {
                   SearchResponse(
                       it.course_name,
                        it.name,
                        it.content,
                    )
                }
                call.respond(courses)
            }
            post("/add") {
                    val req = call.receive<AddRequest>()
                    discussDao.adddiscuss(
                        Discuss(
                        course_name = req.course_name,
                        name = req.name,
                        content = req.content
                    )
                    )
                }
            }
        }
    }
