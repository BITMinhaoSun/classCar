package org.example.demo.reply

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
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


class replyDaoImpl{
    suspend fun addreply(user: Reply): Unit = dbQuery {
        ReplyEntity.new {
            reply_content = user.reply_content
            course_name = user.course_name
            name = user.name
            content = user.content
        }
    }
    //根据课程名字获取讨论
    suspend fun getreply(name: String,content:String,course_name:String) = dbQuery {
        ReplyEntity.find {
            (ReplyTable.course_name eq course_name) and
                    (ReplyTable.content eq content) and
                    (ReplyTable.name eq name)
        }.reversed().map(ReplyEntity::toModel)
    }

}

val replyDao = replyDaoImpl()