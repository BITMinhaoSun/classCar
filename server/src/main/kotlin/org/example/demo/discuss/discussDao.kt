package org.example.demo.discuss
import kotlinx.coroutines.flow.Flow
import org.example.demo.utils.dbQuery
//这个文件用于讨论的增删改
import kotlinx.coroutines.flow.flow
import org.example.demo.course.CourseEntity
import org.example.demo.course.CourseTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


class discussDaoImpl{
    suspend fun hasdiscuss(name: String): Boolean = dbQuery {
        !DiscussEntity.find { DiscussTable.name eq name }.empty()
    }

    suspend fun adddiscuss(user: Discuss): Unit = dbQuery {
        DiscussEntity.new {
            name = user.name
            content = user.content
        }
    }

    suspend fun getdiscuss(name: String): Discuss? = dbQuery {
        DiscussEntity.find { DiscussTable.name eq name }.firstOrNull()?.toModel()
    }

    // 新增方法：获取所有讨论的方法
    suspend fun getAllDiscusses( num: Int) = dbQuery {

        DiscussEntity.all()
            .reversed()
            .take(num)
            .map(DiscussEntity::toModel)
    }
}

val discussDao = discussDaoImpl()