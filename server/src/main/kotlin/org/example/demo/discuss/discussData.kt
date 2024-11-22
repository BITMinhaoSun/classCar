package org.example.demo.discuss

import org.example.demo.discuss.DiscussTable.uniqueIndex


import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
//这个文件规定了讨论的格式
@Serializable
data class Discuss(
    val course_name: String,
    val name: String,
    val content: String
)

object DiscussTable : IntIdTable() {
    val course_name = varchar("course_name", 64).default("aaa")
    val name = varchar("name", 64)
    val content = varchar("content", 128).index()
}

class DiscussEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DiscussEntity>(DiscussTable)
    var course_name by DiscussTable.course_name
    var name by DiscussTable.name
    var content by DiscussTable.content
    fun toModel() = Discuss(
        course_name,
        name,
        content
    )
//fun toModel() = Discuss( name, content )
// 这个方法的作用是将 discussEntity 类型的数据库实体对象转换为业务逻辑层面使用的 Discuss 类型
}
