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
                val courses = discussDao.getAllDiscusses(10).map {
                   SearchResponse(
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
                        req.name,
                        req.content
                    )
                    )
                }
            }
        }
    }
