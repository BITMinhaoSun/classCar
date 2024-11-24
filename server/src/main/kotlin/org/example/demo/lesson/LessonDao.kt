package org.example.demo.lesson

import org.example.demo.chapter.ChapterEntity
import org.example.demo.chapter.ChapterTable
import org.example.demo.course.*
import org.example.demo.discuss.Discuss
import org.example.demo.discuss.DiscussEntity
import org.example.demo.student.StudentEntity
import org.example.demo.student.StudentTable
import org.example.demo.utils.dbQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.*

class LessonDaoImpl {
    suspend fun addLesson(user: Lesson): Unit = dbQuery {
        println("server adddiscuss course"+user.course_id)
        LessonEntity.new {
            course_id = user.course_id
            name = user.name
            description = user.description
        }
    }
    suspend fun getLessonViaCrouseId(course_id: Int) = dbQuery {
        LessonEntity.find{(LessonTable.course_id eq course_id)}.reversed().map(LessonEntity::toModel)
    }
    suspend fun deleteLesson(lesson_id:Int) = dbQuery {
        println("delete lesson ")
        println(lesson_id)
        LessonEntity.find {
            (LessonTable.id eq lesson_id)
        }.forEach { it.delete() }
    }


    }
//    suspend fun getLessonsOfStudent(student: String, from: Int, num: Int) = dbQuery {
//        StudentEntity.find { StudentTable.name eq student }
//            .firstOrNull()!!
//            .lessons
//            .reversed()
//            .drop(from)
//            .take(num)
//            .map(LessonEntity::toModel)
//    }
//    suspend fun getLessonsOfTeacher(teacher: String, from: Int, num: Int) = dbQuery {
//        CourseEntity.find { CourseTable.creator eq teacher }
//            .reversed()
//            .drop(from)
//            .take(num)
//            .map(CourseEntity::toModel)
//    }
//    suspend fun getStudentsOfLesson(lesson: Int, from: Int, num: Int) = dbQuery {
//        LessonEntity.findById(lesson)!!.students
//            .drop(from)
//            .take(num)
//            .map(StudentEntity::toModel)
//    }
//    suspend fun joinLesson(student: String, lesson: Int) = dbQuery {
//        StudentLessonTable.insert {
//            it[this.student] = StudentEntity.find { StudentTable.name eq student }.first().id.value
//            it[this.lesson] = lesson
//        }
//    }
//}

val lessonDao = LessonDaoImpl()