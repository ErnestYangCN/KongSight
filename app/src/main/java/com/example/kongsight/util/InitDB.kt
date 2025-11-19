package com.example.kongsight.util

import android.content.Context
import com.example.kongsight.database.ContentEntity
import com.example.kongsight.database.UserEntity
import com.example.kongsight.model.AppRepository
import kotlinx.coroutines.flow.firstOrNull

/**
 * 初始化类，用于管理APP的各种初始化功能。
 * 每个初始化函数独立，便于单独调用或扩展添加更多初始化逻辑。
 */
class InitDB(private val context: Context) {
    private val repo = AppRepository(context)

    /**
     * 初始化管理员账号（如果不存在）。
     * 用户名: admin, 密码: kongsightadmin, 其他字段留空。
     * 返回创建或已存在的管理员ID。
     */
    suspend fun adminInit(): Long {
        val adminUsername = "admin"
        val adminPassword = "kongsightadmin"
        var adminId: Long? = repo.getUserByUsername(adminUsername).firstOrNull()?.id
        if (adminId == null) {
            val adminUser = UserEntity(
                username = adminUsername,
                password = adminPassword,
                bio = "",
                contactInfo = "",
                admin = true,
                registrationTime = System.currentTimeMillis()
            )
            repo.insertUser(adminUser)
            // 获取新创建的ID
            adminId = repo.getUserByUsername(adminUsername).firstOrNull()?.id
                ?: throw IllegalStateException("Failed to create admin user")
        }
        return adminId
    }

    /**
     * 初始化起始资讯（如果不存在）。
     * 标题: test, 描述: hello world, 其他字段留空或默认。
     * 需要管理员ID作为creatorId。
     */
    suspend fun contentInit(adminId: Long) {
        val initialTitle = "test"
        val initialDescription = "hello world"
        val existingContent = repo.getAllContents().firstOrNull()?.find { it.title == initialTitle }
        if (existingContent == null) {
            val creationTime = System.currentTimeMillis()
            val initialContent = ContentEntity(
                creationTime = creationTime,
                title = initialTitle,
                imageUrl = "",  // 留空
                description = initialDescription,
                longitude = 0.0,  // 默认值
                latitude = 0.0,   // 默认值
                updateTime = creationTime,
                creatorId = adminId,  // 使用管理员ID
                contentTypeIsScene = true,
                surroundingFatherId = null
            )
            repo.insertContent(initialContent)
        }
    }

    // 可以在这里添加更多初始化函数，例如：
    // suspend fun otherInit() { ... }
}