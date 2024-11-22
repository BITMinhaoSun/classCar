package org.example.demo.reply

import org.example.demo.discuss.DiscussTable.default
import org.example.demo.discuss.DiscussTable.index


import org.example.demo.discuss.DiscussTable.uniqueIndex


import kotlinx.serialization.Serializable
import org.example.demo.reply.ReplyTable.reply_name
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
//这个文件规定了讨论的格式
@Serializable
data class Reply(
    val name: String,
    val content: String,
    val course_name: String,
    val reply_content: String,
    val reply_name: String,
)

object ReplyTable : IntIdTable() {
    val reply_content = varchar("replycontent", 64).default("aaa")
    val name = varchar("name", 64).default("111")
    val course_name = varchar("course_name", 64).default("aaa")
    val content = varchar("content", 128).default("111").index()
    val reply_name = varchar("reply_name", 64).default("111")
}

class ReplyEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ReplyEntity>(ReplyTable)
    var reply_content by ReplyTable.reply_content
    var name by ReplyTable.name
    var course_name by ReplyTable.course_name
    var content by ReplyTable.content
    var reply_name by ReplyTable.reply_name
    fun toModel() = Reply(
        reply_content = reply_content,
        course_name = course_name,
        name=name,
        content = content,
        reply_name = reply_name
    )
//fun toModel() = Discuss( name, content )
// 这个方法的作用是将 discussEntity 类型的数据库实体对象转换为业务逻辑层面使用的 Discuss 类型
}
