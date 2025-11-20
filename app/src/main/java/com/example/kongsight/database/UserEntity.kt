/* 定义用户数据类 */
package com.example.kongsight.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,                   // 主键：统一ID

    val username: String,               // 用户名
    val password: String,               // 密码：eval版不设加密存储功能 （可修改）

    val bio: String,                    // 个人简介（可修改）

    @ColumnInfo(name = "contact_info")
    val contactInfo: String,            // 联系方式（可修改）

    val admin: Boolean,                 // 权限：True 为管理员，False 为用户

    @ColumnInfo(name = "registration_time")
    val registrationTime: Long,         // 注册时间
)