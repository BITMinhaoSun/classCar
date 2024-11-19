package org.example.demo

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.example.demo.course.CourseEntity
import org.example.demo.course.CourseTable
import org.example.demo.course.StudentCourseTable
import org.example.demo.course.courseRouting
import org.example.demo.lesson.LessonTable
import org.example.demo.lesson.StudentLessonTable
import org.example.demo.lesson.lessonsRouting
import org.example.demo.questions.QuestionTable
import org.example.demo.questions.StudentQuestionTable
import org.example.demo.student.StudentEntity
import org.example.demo.student.StudentTable
import org.example.demo.student.studentRouting
import org.example.demo.teacher.*
import org.example.demo.chapter.*
import org.example.demo.keypoint.*
import org.example.demo.utils.dbQuery
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    embeddedServer(
            Netty,
            port = SERVER_PORT,
            host = "0.0.0.0",
            module = Application::module
    ).start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) { json() }
    Database.connect("jdbc:h2:file:./my_test_db", driver = "org.h2.Driver")
    transaction {
        SchemaUtils.create(TeacherTable)
        SchemaUtils.create(StudentTable)
        SchemaUtils.create(CourseTable)
        SchemaUtils.create(StudentCourseTable)
        SchemaUtils.create(LessonTable)
        SchemaUtils.create(StudentLessonTable)
        SchemaUtils.create(ChapterTable)
        SchemaUtils.create(KeypointTable)
        SchemaUtils.create(QuestionTable)
        TeacherEntity.new {
            name = "111"
            password = "111"
        }
        StudentEntity.new {
            name = "222"
            password = "222"
        }
        (1..20).forEach {
            CourseEntity.new {
                name = "课程$it"
                description = "课程${it}的描述的描述的描述"
                creator = "111"
            }
        }
        (1..3).forEach { index ->
            StudentCourseTable.insert {
                it[this.student] = 1
                it[this.course] = index
            }
        }
    }
    teacherRouting()
    studentRouting()
    courseRouting()
    lessonsRouting()
    
}