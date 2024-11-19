package org.example.demo.teacher

import org.example.demo.utils.dbQuery

interface TeacherDao {
    suspend fun hasTeacher(name: String): Boolean
    suspend fun addTeacher(user: Teacher): Unit
    suspend fun getTeacher(name: String): Teacher?
}

class TeacherDaoImpl: TeacherDao {
    override suspend fun hasTeacher(name: String): Boolean = dbQuery {
        !TeacherEntity.find{ TeacherTable.name eq name }.empty()
    }

    override suspend fun addTeacher(user: Teacher): Unit = dbQuery {
        TeacherEntity.new {
            name = user.name
            password = user.password
        }
    }

    override suspend fun getTeacher(name: String): Teacher? = dbQuery {
        TeacherEntity.find{ TeacherTable.name eq name }.firstOrNull()?.toModel()
    }
}

val teacherDao = TeacherDaoImpl()