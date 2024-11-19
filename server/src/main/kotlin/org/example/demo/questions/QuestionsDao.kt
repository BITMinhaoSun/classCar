package org.example.demo.questions

import org.example.demo.lesson.Lesson
import org.example.demo.lesson.LessonEntity
import org.example.demo.lesson.LessonTable
import org.example.demo.student.StudentEntity
import org.example.demo.teacher.TeacherEntity
import org.example.demo.utils.dbQuery
import org.jetbrains.exposed.dao.id.EntityID

class QuestionsDao {
    suspend fun createQuestion(topic: String, answer: String, creator: String, region: Int): Int = dbQuery{
        val question = QuestionEntity.new {
            this.topic = topic
            this.answer = answer
            this.creator = creator
            this.region = LessonEntity.findById(region)!!.id
        }
        return@dbQuery question.id.value
    }
    suspend fun getLessonOfQuestion(question: String) = dbQuery {
        LessonEntity.find { QuestionTable.topic eq question }
            .map(LessonEntity::toModel)
    }
    suspend fun getTeacherOfQuestion(question: String) = dbQuery {
        QuestionEntity.find { QuestionTable.topic eq question }
            .map(QuestionEntity::toModel)
    }
}

val quesitonDao = QuestionsDao()