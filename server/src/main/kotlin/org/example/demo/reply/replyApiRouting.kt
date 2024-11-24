package org.example.demo.reply

import org.example.demo.reply.ReplySearchResponse



import org.example.demo.reply.ReplySearchRequest
import org.example.demo.reply.ReplyAddRequest
import org.example.demo.reply.Reply
import org.example.demo.reply.replyDao


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.Flow
import org.example.demo.course.CourseResponse
import org.example.demo.course.CoursesRequest
import org.example.demo.course.courseDao
import org.example.demo.reply.replyDao

fun Application.ReplyRouting() {
    routing {
        ///Reply/search
        route("/reply") {
            post("/search") {
                println("------------------------------------------------------------")
                val req =  call.receive<ReplySearchRequest>()
                val courses = replyDao.getreply(
                    name = req.name,
                    content = req.content,
                    course_name =req.course_name,
                ).map {
                    ReplySearchResponse(
                       course_name =  it.course_name,
                        reply_name = it.reply_name,
                        name = it.name,
                        content = it.content,
                        reply_content=it.reply_content
                    )
                }
                call.respond(courses)
            }
            post("/add") {
                val req = call.receive<ReplyAddRequest>()
                replyDao.addreply(
                    Reply(
                        course_name = req.course_name,
                        name=req.name,
                        content = req.content,
                        reply_content = req.reply_content,
                        reply_name = req.reply_name
                    )
                )
            }
            post("/delete") {


                println("replyApiRouting")
                println("delete reply")
                val req = call.receive<ReplyDeleteRequest>()
                replyDao.deletereply(
                    Reply(
                        course_name = req.course_name,
                        name=req.name,
                        content = req.content,
                        reply_content = req.reply_content,
                        reply_name = req.reply_name
                    )
                )
                println(req.reply_content)
                println(req.reply_name)

            }
        }
    }
}
