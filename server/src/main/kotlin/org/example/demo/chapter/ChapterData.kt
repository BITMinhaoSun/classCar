package org.example.demo.chapter

import org.example.demo.discuss.DiscussTable.uniqueIndex


import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
//这个文件规定了讨论的格式
@Serializable
data class Chapter(
    val course_name: String,
    val name: String,
    val content: String
)

object ChapterTable : IntIdTable() {
    val course_name = varchar("course_name", 64).default("aaa")
    val name = varchar("name", 64)
    val content = varchar("content", 128).index()
}

class ChapterEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ChapterEntity>(ChapterTable)
    var course_name by ChapterTable.course_name
    var name by ChapterTable.name
    var content by ChapterTable.content
    fun toModel() = Chapter(
        course_name,
        name,
        content
    )
//fun toModel() = Chapter( name, content )
// 这个方法的作用是将 chapterEntity 类型的数据库实体对象转换为业务逻辑层面使用的 Chapter 类型
}
