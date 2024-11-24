package org.example.demo.userInfo

import org.example.demo.utils.dbQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

//这个文件用于讨论的增删改


class InfoDaoImpl{

    //根据名字获取信息
    suspend fun getInfo(name: String) = dbQuery {
        InfoEntity.find { InfoTable.name eq name}.reversed().map(InfoEntity::toModel)
    }
    suspend fun updateInfo(
        name: String,
        school: String,
        phone_number: String,
        e_mail: String,
        avatar: Int
    ) = dbQuery {
        val entity = InfoEntity.find { InfoTable.name eq name }.firstOrNull()
        entity?.apply {
            this.school = school
            this.phone_number = phone_number
            this.e_mail = e_mail
            this.avatar = avatar
        } ?: throw Exception("Info with name $name not found")
    }


}

val InfoDao = InfoDaoImpl()