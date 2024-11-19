package org.example.demo.course

import kotlinx.serialization.Serializable
import org.example.demo.student.StudentEntity
import org.example.demo.student.StudentTable
import org.example.demo.teacher.TeacherTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

@Serializable
data class Course(
    val id: Int,
    val name: String,
    val description: String,
    val creator: String
)

object CourseTable : IntIdTable() {
    val name = varchar("name", 64)
    val description = varchar("description", 256)
    val creator = reference("creator", TeacherTable.name, onDelete = ReferenceOption.CASCADE).index()
}

class CourseEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CourseEntity>(CourseTable)
    var name by CourseTable.name
    var description by CourseTable.description
    var creator by CourseTable.creator
    var students by StudentEntity via StudentCourseTable
    fun toModel() = Course(
        id.value, name, description, creator
    )
}


object StudentCourseTable : Table() {
    val student = reference("student", StudentTable, onDelete = ReferenceOption.CASCADE)
    val course = reference("course", CourseTable, onDelete = ReferenceOption.CASCADE)
    override val primaryKey = PrimaryKey(student, course)
}

