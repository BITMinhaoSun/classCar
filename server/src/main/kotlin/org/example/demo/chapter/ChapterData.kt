package org.example.demo.chapter

import kotlinx.serialization.Serializable
import org.example.demo.course.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

@Serializable
data class Chapter(
        val id: Int,
        val name: String,
        val description: String,
        val course_id: Int
)

object ChapterTable : IntIdTable() {
    val name = varchar("name", 64)
    val description = varchar("description", 256)
    val course_id = reference("course_id", CourseTable, onDelete = ReferenceOption.CASCADE).index()
}

class ChapterEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ChapterEntity>(ChapterTable)
    var name by ChapterTable.name
    var description by ChapterTable.description
    var course_id by ChapterTable.course_id
    fun toModel() = Chapter(
            id.value, name, description, course_id.value
    )
}


