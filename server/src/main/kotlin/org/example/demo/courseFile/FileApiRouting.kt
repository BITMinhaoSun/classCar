package org.example.demo.courseFile

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.fileRouting() {
    routing {
        post("/file/upload/{courseId}") {
            val req = call.receive<UploadFileRequest>()
            val fileId = fileDao.uploadFileToCourse(req.baseName, req.extension, call.parameters["courseId"]!!.toInt())
            val file = File("./res/courseFiles/${fileId}")
            file.writeBytes(req.file)
        }
        delete("/file/delete/{fileId}") {
            fileDao.deleteFile(call.parameters["fileId"]!!.toInt())
        }
        get("/file/description/{courseId}/{offset}") {
            val courseId = call.parameters["courseId"]!!.toInt()
            val offset = call.parameters["offset"]!!.toInt()
            val files = fileDao.getFilesOfCourse(courseId, offset, 10).map {
                FileDescriptionResponse(
                    it.id,
                    it.baseName,
                    it.extension
                )
            }
            call.respond(files)
        }
        get("/file/download/{fileId}") {
            val file = File("./res/courseFiles/${call.parameters["fileId"]!!}").readBytes()
            call.respond(DownloadFileResponse(file))
        }
    }
}