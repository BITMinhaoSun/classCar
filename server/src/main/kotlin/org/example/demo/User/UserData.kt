package org.example.demo.User

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class User(
    val name: String,
    val password: String
)

object UserTable : IntIdTable() {
    val name = varchar("name", 64).uniqueIndex()
    val password = varchar("password", 128)
}

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UserTable)
    var name by UserTable.name
    var password by UserTable.password
    fun toModel() = User(
        name,
        password
    )
}
