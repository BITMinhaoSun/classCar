package org.example.demo.chapter

import org.example.demo.course.*
import org.example.demo.chapter.*
import org.example.demo.lesson.Lesson
import org.example.demo.lesson.LessonEntity
import org.example.demo.lesson.LessonTable
import org.example.demo.utils.dbQuery
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ChapterDao {
    suspend fun createChapter(name: String, description: String, course_id: Int): Int = dbQuery{
        val chapter = ChapterEntity.new {
            this.name = name
            this.description = description
            this.course_id = CourseEntity.findById(course_id)!!.id
        }
        return@dbQuery chapter.id.value
    }
    suspend fun getLessonOfChapter(chapter: String) = dbQuery {
        LessonEntity.find { ChapterTable.name eq chapter }
                .map(LessonEntity::toModel)
    }
}

val quesitonDao = ChapterDao()