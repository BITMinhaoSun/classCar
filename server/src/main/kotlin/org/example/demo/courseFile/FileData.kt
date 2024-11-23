package org.example.demo.courseFile

import kotlinx.serialization.Serializable
import org.example.demo.course.CourseEntity
import org.example.demo.course.CourseTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

@Serializable
data class FileDescription(
    val id: Int,
    val baseName: String,
    val extension: String,
    val courseId: Int,
)

object FileDescriptionTable : IntIdTable() {
    val baseName = varchar("baseName", 256)
    val extension = varchar("extension", 64)
    val courseId = reference("courseId", CourseTable, onDelete = ReferenceOption.CASCADE).index()
}

class FileDescriptionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<FileDescriptionEntity>(FileDescriptionTable)
    var baseName by FileDescriptionTable.baseName
    var extension by FileDescriptionTable.extension
    var course by FileDescriptionTable.courseId
    fun toModel() = FileDescription(
        id.value,
        baseName,
        extension,
        course.value
    )
}