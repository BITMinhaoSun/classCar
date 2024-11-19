package org.example.demo.keypoint

import org.example.demo.course.*
import org.example.demo.chapter.*
import org.example.demo.lesson.Lesson
import org.example.demo.lesson.LessonEntity
import org.example.demo.lesson.LessonTable
import org.example.demo.utils.dbQuery
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class KeypointDao {
    suspend fun createKeypoint(name: String, description: String, chapter_id: Int): Int = dbQuery{
        val keypoint = KeypointEntity.new {
            this.name = name
            this.description = description
            this.chapter_id = ChapterEntity.findById(chapter_id)!!.id
        }
        return@dbQuery keypoint.id.value
    }
    suspend fun getChapterOfKeypoint(keypoint: String) = dbQuery {
        ChapterEntity.find { KeypointTable.name eq keypoint }
                .map(ChapterEntity::toModel)
    }
}

val chapterDao = ChapterDao()