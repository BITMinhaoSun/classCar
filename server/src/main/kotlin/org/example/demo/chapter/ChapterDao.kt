package org.example.demo.chapter
import kotlinx.coroutines.flow.Flow
import org.example.demo.utils.dbQuery
//这个文件用于讨论的增删改
import kotlinx.coroutines.flow.flow
import org.example.demo.course.CourseEntity
import org.example.demo.course.CourseTable
import org.example.demo.courseFile.FileDescriptionEntity
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.*


class chapterDaoImpl{
    suspend fun haschapter(name: String): Boolean = dbQuery {
        !ChapterEntity.find { ChapterTable.name eq name }.empty()
    }

    suspend fun addchapter(user: Chapter): Unit = dbQuery {
        println("server addchapter course"+user.course_name)
        ChapterEntity.new {
            course_name = user.course_name
            name = user.name
            content = user.content
        }
    }
    //根据课程名字获取讨论
    suspend fun getchapter(course_name: String) = dbQuery {
        ChapterEntity.find { ChapterTable.course_name eq course_name}.reversed().map(ChapterEntity::toModel)
    }

//    suspend fun deleteChapter(course_name: String, chapter_name: String) = dbQuery {
//        ChapterTable.deleteWhere {
//            (ChapterTable.course_name eq course_name) and (ChapterTable.name eq chapter_name)
//        }
//    }

    suspend fun deleteChapter(course_name: String, chapter_name: String) = dbQuery {
        // 查找符合条件的所有实体
//        ChapterEntity.find {
//            (ChapterTable.course_name eq course_name) and (ChapterTable.name eq chapter_name)
//        }.forEach { ChapterEntity ->
//            // 删除每个实体
//            ChapterEntity.delete()
//        }
        println("delete chapter ")
        println(course_name)
        println(chapter_name)
        ChapterEntity.find {
            (ChapterTable.course_name eq course_name) and (ChapterTable.name eq chapter_name)
        }.forEach { it.delete() }
//        FileDescriptionEntity.findById(fileId)?.delete()
    }
    // 新增方法：获取所有讨论的方法
    suspend fun getAllChapters( num: Int) = dbQuery {
        ChapterEntity.all()
            .reversed()
            .take(num)
            .map(ChapterEntity::toModel)
    }
}

val chapterDao = chapterDaoImpl()