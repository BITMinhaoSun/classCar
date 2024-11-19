package org.example.demo.teacher

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Teacher(
    val name: String,
    val password: String
)

object TeacherTable : IntIdTable() {
    val name = varchar("name", 64).uniqueIndex()
    val password = varchar("password", 128)
}

class TeacherEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TeacherEntity>(TeacherTable)
    var name by TeacherTable.name
    var password by TeacherTable.password
    fun toModel() = Teacher(
        name,
        password
    )
}
