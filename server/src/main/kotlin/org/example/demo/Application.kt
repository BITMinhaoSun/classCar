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
import org.example.demo.discuss.DiscussRouting
import org.example.demo.teacher.*
import org.example.demo.chapter.*
import org.example.demo.courseFile.FileDescriptionTable
import org.example.demo.courseFile.fileRouting
import org.example.demo.discuss.DiscussEntity
import org.example.demo.keypoint.*
import org.example.demo.utils.dbQuery
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.example.demo.discuss.DiscussTable
import org.example.demo.reply.ReplyEntity
import org.example.demo.reply.ReplyRouting
import org.example.demo.reply.ReplyTable
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
        SchemaUtils.create(QuestionTable)

        SchemaUtils.drop(DiscussTable)
        SchemaUtils.create(DiscussTable)
        SchemaUtils.drop(ReplyTable)
        SchemaUtils.create(ReplyTable)
//        SchemaUtils.drop(ChapterTable)
        SchemaUtils.create(ChapterTable)
//        SchemaUtils.drop(KeypointTable)
        SchemaUtils.create(KeypointTable)
        SchemaUtils.create(FileDescriptionTable)
//        (1..20).forEach {
//            DiscussEntity.new {
//                course_name="$it"
//                name = "111"
//                content = "评论${it}的描述"
//            }
//        }
//        (1..20).forEach {
//            ReplyEntity.new {
//                course_name="aaa"
//                name = "111"
//                content = "评论${it}的描述"
//                reply_name = "回复$it"
//                reply_content = "回复${it}的描述"
//            }
//        }
//        (1..20).forEach {
//            ChapterEntity.new {
//                course_name="$it"
//                name = "111"
//                content = "评论${it}的描述"
//            }
//        }
//        (1..20).forEach {
//            KeypointEntity.new {
//                course_name="aaa"
//                name = "111"
//                content = "评论${it}的描述"
//                keypoint_name = "回复$it"
//                keypoint_content = "回复${it}的描述"
//            }
//        }
//        (21312..21320).forEach { index ->
//            DiscussTable.insert {
//                it[name] = "评论$index"
//                it[content] = "评论${index}的描述"
//            }
//        }
//        TeacherEntity.new {
//            name = "111"
//            password = "111"
//        }
//        StudentEntity.new {
//            name = "222"
//            password = "222"
//        }
//        (1..20).forEach {
//            CourseEntity.new {
//                name = "课程$it"
//                description = "课程${it}的描述的描述的描述"
//                creator = "111"
//            }
//        }
//        (1..3).forEach { index ->
//            StudentCourseTable.insert {
//                it[this.student] = 1
//                it[this.course] = index
//            }
//        }
    }
    teacherRouting()
    studentRouting()
    courseRouting()
    lessonsRouting()
    DiscussRouting()
    ReplyRouting()
    ChapterRouting()
    KeypointRouting()
    fileRouting()
}