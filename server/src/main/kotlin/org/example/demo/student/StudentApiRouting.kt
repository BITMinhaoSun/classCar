package org.example.demo.student

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.studentRouting() {
    routing {
        route("/student") {
            post("/login") {
                try {
                    val req = call.receive<LoginRequest>()
                    val student = studentDao.getStudent(req.name)
                    if (student == null) {
                        call.respondText("用户不存在", status = HttpStatusCode.Forbidden)
                    } else {
                        if (student.password == req.password) {
                            call.respondText("登录成功")
                        } else {
                            call.respondText("密码错误", status = HttpStatusCode.Forbidden)
                        }
                    }
                } catch (_: Exception) {

                }
            }
            post("/register") {
                try {
                    val req = call.receive<RegisterRequest>()
                    if (studentDao.hasStudent(req.name)) {
                        call.respondText("用户已存在", status = HttpStatusCode.Forbidden)
                    } else {
                        studentDao.addStudent(
                            Student(
                                req.name,
                                req.password
                            )
                        )
                        call.respondText("注册成功")
                    }
                } catch (_: Exception) {

                }
            }
        }
    }
}