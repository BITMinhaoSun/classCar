package org.example.demo.discuss
import kotlinx.coroutines.flow.Flow
import org.example.demo.utils.dbQuery
//这个文件用于讨论的增删改
import kotlinx.coroutines.flow.flow
import org.example.demo.chapter.ChapterEntity
import org.example.demo.chapter.ChapterTable
import org.example.demo.course.CourseEntity
import org.example.demo.course.CourseTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and


class discussDaoImpl{
    suspend fun hasdiscuss(name: String): Boolean = dbQuery {
        !DiscussEntity.find { DiscussTable.name eq name }.empty()
    }

    suspend fun adddiscuss(user: Discuss): Unit = dbQuery {
        println("server adddiscuss course"+user.course_name)
        DiscussEntity.new {
            course_name = user.course_name
            name = user.name
            content = user.content
        }
    }
    //根据课程名字获取讨论
    suspend fun getdiscuss(course_name: String) = dbQuery {
        DiscussEntity.find { DiscussTable.course_name eq course_name}.reversed().map(DiscussEntity::toModel)
    }

    // 新增方法：获取所有讨论的方法
    suspend fun getAllDiscusses( num: Int) = dbQuery {
        DiscussEntity.all()
            .reversed()
            .take(num)
            .map(DiscussEntity::toModel)
    }
    suspend fun deleteDiscuss(course_name: String, discuss_name: String,discussContent:String) = dbQuery {
        println("delete discuss ")
        println(course_name)
        println(discuss_name)
        println(discussContent)
        DiscussEntity.find {
            (DiscussTable.course_name eq course_name) and (DiscussTable.name eq discuss_name) and (DiscussTable.content eq discussContent)
        }.forEach { it.delete() }
    }
}

val discussDao = discussDaoImpl()