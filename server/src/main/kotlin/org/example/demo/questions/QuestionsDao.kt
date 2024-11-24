package org.example.demo.questions

import org.example.demo.course.CourseEntity
import org.example.demo.lesson.Lesson
import org.example.demo.lesson.LessonEntity
import org.example.demo.lesson.LessonTable
import org.example.demo.student.Student
import org.example.demo.student.StudentEntity
import org.example.demo.student.StudentTable
import org.example.demo.teacher.TeacherEntity
import org.example.demo.utils.dbQuery
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert

class QuestionsDaoImpl {
    suspend fun addQuestion(description: String, options: List<String>, answer: String, lessonId: Int): Int = dbQuery {
        val question = QuestionEntity.new {
            this.description = description
            this.options = options
            this.answer = answer
            this.lesson = LessonEntity.findById(lessonId)!!.id
            this.course = CourseEntity.findById(LessonEntity.findById(lessonId)!!.course_id)!!.id
            this.released = false
            this.closed = false
        }
        return@dbQuery question.id.value
    }

    suspend fun deleteQuestion(questionId: Int): Unit = dbQuery {
        QuestionEntity.findById(questionId)?.delete()
    }

    suspend fun cloneQuestion() = dbQuery { /* TODO */ }

    suspend fun getAllQuestionsOfLesson(lessonId: Int/*, from: Int, num: Int*/): List<Question> = dbQuery {
        QuestionEntity.find { QuestionTable.lessonId eq lessonId }
            .reversed()
//            .drop(from)
//            .take(num)
            .map(QuestionEntity::toModel)
    }

    suspend fun getReleasedQuestionsOfLesson(lessonId: Int/*, from: Int, num: Int*/): List<Question> = dbQuery {
        QuestionEntity.find { (QuestionTable.lessonId eq lessonId) and (QuestionTable.released eq Op.TRUE) }
            .reversed()
//            .drop(from)
//            .take(num)
            .map(QuestionEntity::toModel)
    }

    suspend fun releaseQuestion(questionId: Int): Unit = dbQuery {
        QuestionEntity.findById(questionId)?.let {
            it.released = true
        }
    }

    suspend fun closeQuestion(questionId: Int): Unit = dbQuery {
        QuestionEntity.findById(questionId)?.let {
            it.closed = true
        }
    }

    suspend fun getSingleQuestion(questionId: Int) = dbQuery {
        QuestionEntity.findById(questionId)!!.toModel()
    }

    suspend fun getStudentAnswer(questionId: Int, student: String) = dbQuery {
        val studentId = StudentEntity.find { StudentTable.name eq student }.first().id.value
        val studentAnswer = StudentQuestionTable.select(StudentQuestionTable.studentAnswer)
            .where { (StudentQuestionTable.question eq questionId) and (StudentQuestionTable.student eq studentId) }
            .map { it[StudentQuestionTable.studentAnswer] }
            .firstOrNull()
        return@dbQuery studentAnswer
    }

    suspend fun answerQuestion(questionId: Int, student: String, answer: String): Boolean = dbQuery {
        val question = QuestionEntity.findById(questionId)
        question?.let { theQuestion ->
            if (theQuestion.released && !theQuestion.closed) {
                if (answer in theQuestion.options) {
                    StudentQuestionTable.insert {
                        it[this.question] = questionId
                        it[this.student] = StudentEntity.find { StudentTable.name eq student }.first().id
                        it[this.studentAnswer] = answer
                    }
                    return@dbQuery true
                }
                return@dbQuery false
            } else {
                return@dbQuery false
            }
        }?:run {
            return@dbQuery false
        }
    }

    suspend fun getStatisticOfSingleQuestion(questionId: Int): Map<String, Int> = dbQuery {
        val statistic = mutableMapOf<String, Int>()
        val students = QuestionEntity.findById(questionId)?.student
        students?.forEach { student ->
            val studentAnswer = StudentQuestionTable.select(StudentQuestionTable.studentAnswer)
                .where { (StudentQuestionTable.question eq questionId) and (StudentQuestionTable.student eq student.id) }
                .map { it[StudentQuestionTable.studentAnswer] }
                .first()
            statistic[studentAnswer] = statistic.getOrDefault(studentAnswer, 0)
        }
        return@dbQuery statistic
    }
    suspend fun getReleasedQuestionsOfCourse(courseId: Int/*, from: Int, num: Int*/): List<Question> = dbQuery {
        QuestionEntity.find { (QuestionTable.courseId eq courseId) and (QuestionTable.released eq Op.TRUE) }
            .reversed()
//            .drop(from)
//            .take(num)
            .map(QuestionEntity::toModel)
    }
    suspend fun getAllQuestionsOfCourse(courseId: Int/*, from: Int, num: Int*/): List<Question> = dbQuery {
        QuestionEntity.find { (QuestionTable.courseId eq courseId) }
            .reversed()
//            .drop(from)
//            .take(num)
            .map(QuestionEntity::toModel)
    }
    suspend fun getAllQuestionsOfAll (): List<Question> = dbQuery {
        QuestionEntity.all()
            .reversed()
//            .drop(from)
//            .take(num)
            .map(QuestionEntity::toModel)
    }
    suspend fun getAllReleasedQuestionsOfAll (): List<Question> = dbQuery {
        QuestionEntity.all()
            .reversed()
//            .drop(from)
//            .take(num)
            .map(QuestionEntity::toModel)
    }


}

val questionDao = QuestionsDaoImpl()