package org.example.demo.questions

import kotlinx.serialization.Serializable
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
    val topic: String,
    val answer: String,
    val creator: String,
    val region: Int
)

object QuestionTable : IntIdTable() {
    val topic = varchar("topic", 256)
    val answer = varchar("answer", 64)
    val creator = reference("creator", TeacherTable.name, onDelete = ReferenceOption.CASCADE).index()
    val region = reference("region", LessonTable, onDelete = ReferenceOption.CASCADE).index()
}

class QuestionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<QuestionEntity>(QuestionTable)
    var topic by QuestionTable.topic
    var answer by QuestionTable.answer
    var creator by QuestionTable.creator
    var region by QuestionTable.region
    var student by StudentEntity via StudentQuestionTable
    fun toModel() = Question(
        id.value, topic, answer, creator, region.value
    )
}

object StudentQuestionTable : Table() {
    val student = reference("student", StudentTable, onDelete = ReferenceOption.CASCADE)
    val question = reference("question", QuestionTable, onDelete = ReferenceOption.CASCADE)
    val std_answer = varchar("std_answer", 64)    //学生答案
    val std_done = varchar("done", 64)   //是否写了题目，初始化为no，写了是yes
    override val primaryKey = PrimaryKey(student, question)
}


