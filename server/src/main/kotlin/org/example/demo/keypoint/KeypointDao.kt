package org.example.demo.keypoint

import org.example.demo.discuss.Discuss
import org.example.demo.discuss.DiscussEntity
import org.example.demo.discuss.DiscussTable
import org.jetbrains.exposed.sql.and
import kotlinx.coroutines.flow.Flow
import org.example.demo.utils.dbQuery
//这个文件用于讨论的增删改
import kotlinx.coroutines.flow.flow
import org.example.demo.course.CourseEntity
import org.example.demo.course.CourseTable
import org.example.demo.reply.Reply
import org.example.demo.reply.ReplyEntity
import org.example.demo.reply.ReplyTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


class keypointDaoImpl{
    suspend fun addkeypoint(user: Keypoint): Unit = dbQuery {
        KeypointEntity.new {
            keypoint_content = user.keypoint_content
            course_name = user.course_name
            name = user.name
            keypoint_name = user.keypoint_name
            content = user.content
        }
    }
    //根据课程名字获取讨论
    suspend fun getkeypoint(name: String,content:String,course_name:String) = dbQuery {
        KeypointEntity.find {
            (KeypointTable.course_name eq course_name) and
                    (KeypointTable.content eq content) and
                    (KeypointTable.name eq name)
        }.reversed().map(KeypointEntity::toModel)
    }
    suspend fun deletekeypoint(user: Keypoint): Unit = dbQuery {
        println("keypointDao")
        println("deletekeypoint")
        KeypointEntity.find {
            ( KeypointTable.name eq user.name) and
            (KeypointTable.content eq user.content) and
            (KeypointTable.course_name eq user.course_name) and
            (KeypointTable.keypoint_content eq user.keypoint_content) and
            (KeypointTable.keypoint_name eq user.keypoint_name)
        }.forEach { it.delete() }
    }

}

val keypointDao = keypointDaoImpl()