package org.example.demo.student

import kotlinx.serialization.Serializable
import org.example.demo.course.CourseEntity
import org.example.demo.course.StudentCourseTable
import org.example.demo.lesson.LessonEntity
import org.example.demo.lesson.StudentLessonTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Student(
    val name: String,
    val password: String
)

object StudentTable : IntIdTable() {
    val name = varchar("name", 64).uniqueIndex()
    val password = varchar("password", 128)
}

class StudentEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StudentEntity>(StudentTable)
    var name by StudentTable.name
    var password by StudentTable.password
    var courses by CourseEntity via StudentCourseTable
    var lessons by LessonEntity via StudentLessonTable
    fun toModel() = Student(
        name,
        password
    )
}