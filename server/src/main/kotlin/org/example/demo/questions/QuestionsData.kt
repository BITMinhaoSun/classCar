package org.example.demo.questions

import kotlinx.serialization.Serializable
import org.example.demo.course.CourseTable
import org.example.demo.lesson.Lesson
import org.example.demo.lesson.LessonEntity
import org.example.demo.lesson.LessonTable
import org.example.demo.student.StudentEntity
import org.example.demo.student.StudentTable
import org.example.demo.teacher.TeacherTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.UIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

@Serializable
data class Question(
    val id: Int,
    val description: String,
    val options: List<String>,
    val answer: String,
    val lessonId: Int,
    val courseId: Int,
    val released: Boolean,
    val closed: Boolean,
)

object QuestionTable : IntIdTable() {
    val description = varchar("description", 1024)
    val options = array<String>("options", 32)
    val answer = varchar("answer", 16)
    val lessonId = reference("lessonId", LessonTable, onDelete = ReferenceOption.CASCADE).index()
    val courseId = reference("courseId", CourseTable, onDelete = ReferenceOption.CASCADE).index()
    val released = bool("released")
    val closed = bool("closed")
}

class QuestionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<QuestionEntity>(QuestionTable)
    var description by QuestionTable.description
    var options by QuestionTable.options
    var answer by QuestionTable.answer
    var lesson by QuestionTable.lessonId
    var course by QuestionTable.courseId
    var released by QuestionTable.released
    var closed by QuestionTable.closed
    var student by StudentEntity via StudentQuestionTable
    fun toModel() = Question(
        id.value, description, options, answer, lesson.value, course.value, released, closed
    )
}

object StudentQuestionTable : Table() {
    val student = reference("student", StudentTable, onDelete = ReferenceOption.CASCADE)
    val question = reference("question", QuestionTable, onDelete = ReferenceOption.CASCADE)
    val studentAnswer = varchar("studentAnswer", 16)
    override val primaryKey = PrimaryKey(student, question)
}


