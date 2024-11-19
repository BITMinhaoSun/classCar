package org.example.demo.student

import org.example.demo.utils.dbQuery

interface StudentDao {
    suspend fun hasStudent(name: String): Boolean
    suspend fun addStudent(user: Student): Unit
    suspend fun getStudent(name: String): Student?
}

class StudentDaoImpl: StudentDao {
    override suspend fun hasStudent(name: String): Boolean = dbQuery {
        !StudentEntity.find{ StudentTable.name eq name }.empty()
    }

    override suspend fun addStudent(user: Student): Unit = dbQuery {
        StudentEntity.new {
            name = user.name
            password = user.password
        }
    }

    override suspend fun getStudent(name: String): Student? = dbQuery {
        StudentEntity.find{ StudentTable.name eq name }.firstOrNull()?.toModel()
    }
}

val studentDao = StudentDaoImpl()