package org.example.demo.keypoint


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.Flow
import org.example.demo.course.CourseResponse
import org.example.demo.course.CoursesRequest
import org.example.demo.reply.*

fun Application.KeypointRouting() {
    routing {
        ///Reply/search
        route("/keypoint") {
            post("/search") {
                try {
                    println("------------------------------------------------------------")
                    val req = call.receive<KeypointSearchRequest>()
                    val courses = keypointDao.getkeypoint(
                        name = req.name,
                        content = req.content,
                        course_name = req.course_name,
                    ).map {
                        KeypointSearchResponse(
                            course_name = it.course_name,
                            keypoint_name = it.keypoint_name,
                            name = it.name,
                            content = it.content,
                            keypoint_content = it.keypoint_content
                        )
                    }
                    call.respond(courses)
                } catch (_: Exception) {

                }
            }
            post("/add") {
                try {
                    val req = call.receive<KeypointAddRequest>()
                    println("-----------------------------------")
                    println("post route keypoint name" + req.keypoint_name)
                    keypointDao.addkeypoint(
                        Keypoint(
                            course_name = req.course_name,
                            name = req.name,
                            content = req.content,
                            keypoint_content = req.keypoint_content,
                            keypoint_name = req.keypoint_name
                        )
                    )
                } catch (_: Exception) {

                }
            }
            post("/delete") {
                try {
                    println("keypointApiRouting")
                    println("delete keypoint")
                    val req = call.receive<DeleteKeypointRequest>()
                    keypointDao.deletekeypoint(
                        Keypoint(
                            name = req.name,
                            content = req.content,
                            course_name = req.course_name,
                            keypoint_content = req.keypoint_content,
                            keypoint_name = req.keypoint_name
                        )
                    )
                    println(req.keypoint_content)
                    println(req.keypoint_name)
                } catch (_: Exception) {

                }
            }
        }
    }
}
