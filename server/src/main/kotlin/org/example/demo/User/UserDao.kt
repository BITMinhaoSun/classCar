package org.example.demo.User

import org.example.demo.utils.dbQuery

interface UserDao {
    suspend fun hasUser(name: String): Boolean
    suspend fun addUser(user: User): Unit
    suspend fun getUser(name: String): User?
}

class UserDaoImpl: UserDao {
    override suspend fun hasUser(name: String): Boolean = dbQuery {
        !UserEntity.find{ UserTable.name eq name }.empty()
    }

    override suspend fun addUser(user: User): Unit = dbQuery {
        UserEntity.new {
            name = user.name
            password = user.password
        }
    }

    override suspend fun getUser(name: String): User? = dbQuery {
        UserEntity.find{ UserTable.name eq name }.firstOrNull()?.toModel()
    }
}

val userDao = UserDaoImpl()