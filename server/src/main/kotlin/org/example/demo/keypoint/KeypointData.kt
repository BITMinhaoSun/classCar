package org.example.demo.keypoint

import kotlinx.serialization.Serializable
import org.example.demo.chapter.*
import org.example.demo.course.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

@Serializable
data class Keypoint(
        val id: Int,
        val name: String,
        val description: String,
        val chapter_id: Int
)

object KeypointTable : IntIdTable() {
    val name = varchar("name", 64)
    val description = varchar("description", 256)
    val chapter_id = reference("course_id", ChapterTable, onDelete = ReferenceOption.CASCADE).index()
}

class KeypointEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<KeypointEntity>(KeypointTable)
    var name by KeypointTable.name
    var description by KeypointTable.description
    var chapter_id by KeypointTable.chapter_id
    fun toModel() = Keypoint(
            id.value, name, description, chapter_id.value
    )
}