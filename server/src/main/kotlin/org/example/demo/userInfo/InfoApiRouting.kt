package org.example.demo.Info

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.demo.userInfo.*


fun Application.InfoRouting() {
    routing {
        ///Info/search
        ///Info/search
        route("/userInfo") {
            post("/search") {
                try {
                    val req = call.receive<SearchInfoRequest>()
                    println("=============================================")
                    println("Info/search")
                    println("=============================================")
                    val name = InfoDao.getInfo(
                        req.name
                    ).map {
                        SearchInfoResponse(
                            name = it.name,
                            school = it.school,
                            e_mail = it.e_mail,
                            phone_number = it.phone_number,
                            avatar = it.avatar,
                        )
                    }
                    call.respond(name)
                } catch (_: Exception) {

                }
            }
        }
        route("/userInfo") {
            post("/update") {
                try {
                    val req = call.receive<UpdateInfoRequest>()
                    println("=============================================")
                    println("Info/update")
                    println("=============================================")
                    InfoDao.updateInfo(
                        name = req.name,
                        school = req.school,
                        phone_number = req.phone_number,
                        e_mail = req.e_mail,
                        avatar = req.avatar,
                    )
                } catch (_: Exception) {

                }
            }
        }
    }
}