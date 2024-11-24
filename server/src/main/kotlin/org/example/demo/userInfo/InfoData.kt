package org.example.demo.userInfo

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


@Serializable
data class Info(
    val name: String,
    val school: String,
    val phone_number: String,
    val e_mail: String,
    val avatar: Int,
)

object InfoTable : IntIdTable() {
    val name = varchar("name", 64).default("aaa").uniqueIndex()
    val school = varchar("school", 64).default("bit")
    val phone_number = varchar("phone_number", 64).default("aaa")
    val e_mail = varchar("e_mail", 128).default("111").index()
    val avatar = integer("avator").default(0)
}

class InfoEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<InfoEntity>(InfoTable)

    var school by InfoTable.school
    var name by InfoTable.name
    var phone_number by InfoTable.phone_number
    var e_mail by InfoTable.e_mail
    var avatar by InfoTable.avatar
    fun toModel() = Info(
        school = school,
        name = name,
        phone_number = phone_number,
        e_mail = e_mail,
        avatar = avatar
    )
}