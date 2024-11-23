package org.example.demo.keypoint

import org.example.demo.discuss.DiscussTable.default
import org.example.demo.discuss.DiscussTable.index


import org.example.demo.discuss.DiscussTable.uniqueIndex


import kotlinx.serialization.Serializable
import org.example.demo.keypoint.KeypointTable.keypoint_content
import org.example.demo.keypoint.KeypointTable.keypoint_name
import org.example.demo.reply.ReplyTable.reply_name
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
//这个文件规定了讨论的格式
@Serializable
data class Keypoint(
    val name: String,
    val content: String,
    val course_name: String,
    val keypoint_content: String,
    val keypoint_name: String,
)

object KeypointTable : IntIdTable() {
    val keypoint_content = varchar("keypointcontent", 64).default("aaa")
    val name = varchar("name", 64).default("111")
    val course_name = varchar("course_name", 64).default("aaa")
    val content = varchar("content", 128).default("111").index()
    val keypoint_name = varchar("keypoint_name", 64).default("555")
}

class KeypointEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<KeypointEntity>(KeypointTable)
    var keypoint_content by KeypointTable.keypoint_content
    var name by KeypointTable.name
    var course_name by KeypointTable.course_name
    var content by KeypointTable.content
    var keypoint_name by KeypointTable.keypoint_name
    fun toModel() = Keypoint(
        keypoint_content = keypoint_content,
        course_name = course_name,
        name=name,
        content = content,
        keypoint_name = keypoint_name
    )
//fun toModel() = Keypoint( name, content )
// 这个方法的作用是将 keypointEntity 类型的数据库实体对象转换为业务逻辑层面使用的 Keypoint 类型
}
