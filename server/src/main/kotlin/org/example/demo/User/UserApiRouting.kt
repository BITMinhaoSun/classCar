package org.example.demo.User

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.userRouting() {
    routing {
        post("/login") {
            val req = call.receive<LoginRequest>()
            val user = userDao.getUser(req.name)
            if (user == null) {
                call.respondText("用户不存在")
            } else {
                if (user.password == req.password) {
                    call.respondText("登录成功")
                } else {
                    call.respondText("密码错误")
                }
            }
        }
        post("/register") {
            val req = call.receive<RegisterRequest>()
            if (userDao.hasUser(req.name)) {
                call.respondText("用户已存在")
            } else {
                userDao.addUser(User(
                    req.name,
                    req.password
                ))
                call.respondText("注册成功")
            }
        }
    }
}