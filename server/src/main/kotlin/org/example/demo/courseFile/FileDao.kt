package org.example.demo.courseFile

import org.example.demo.course.CourseEntity
import org.example.demo.utils.dbQuery

class FileDaoImpl {
    suspend fun uploadFileToCourse(baseName: String, extension: String, courseId: Int): Int = dbQuery {
        val file = FileDescriptionEntity.new {
            this.baseName = baseName
            this.extension = extension
            this.course = CourseEntity.findById(courseId)!!.id
        }
        return@dbQuery file.id.value
    }

    suspend fun deleteFile(fileId: Int) = dbQuery {
        FileDescriptionEntity.findById(fileId)?.delete()
    }

    suspend fun getFilesOfCourse(courseId: Int, from: Int, num: Int): List<FileDescription> = dbQuery {
        FileDescriptionEntity.find { FileDescriptionTable.courseId eq courseId }
            .reversed()
            .drop(from)
            .take(num)
            .map(FileDescriptionEntity::toModel)
    }
}

val fileDao = FileDaoImpl()