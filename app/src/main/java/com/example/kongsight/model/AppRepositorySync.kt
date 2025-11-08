/**
 * 定义仓库接口类：Java 调用友好
 * 可以选择调用原生方法，也可以调用我设计封装的方法
 *      调用原生方法时请注意传参类型和返回值类型
 *      使用封装方法时请参考KDoc注释
 * 数据库库函数使用方法可参考 test/TestDB 文件
 * ！！请不要直接修改 database 包里的任何内容！！
 * ！！此文件继承自 AppRepository 文件，即二者实现功能相同！！
 * 有更多数据操作需求请对我提出
 */
package com.example.kongsight.model

import android.content.Context
import com.example.kongsight.database.ContentEntity
import com.example.kongsight.database.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.firstOrNull

class AppRepositorySync(context: Context) {
    private val repo = AppRepository(context)

    /* Content操作接口 */
    /**
     * 原生资讯方法1 - 插入资讯信息
     * @param content 资讯实体
     */
    fun insertContent(content: ContentEntity) {
        runBlocking { repo.insertContent(content) }
    }

    /**
     * 原生资讯方法2 - 更新资讯信息
     * @param content 资讯实体
     */
    fun updateContent(content: ContentEntity) {
        runBlocking { repo.updateContent(content) }
    }

    /**
     * 原生资讯方法3 - 删除资讯信息
     * @param content 资讯实体
     */
    fun deleteContent(content: ContentEntity) {
        runBlocking { repo.deleteContent(content) }
    }

    /**
     * 原生资讯方法4 - 根据 ID 查找资讯信息
     * @param id 资讯 ID
     * @return 资讯实体或 null
     */
    fun getContentById(id: Long): ContentEntity? {
        return runBlocking { repo.getContentById(id).firstOrNull() }
    }

    /**
     * 原生资讯方法5 - 获取所有资讯信息
     * @return 资讯列表
     */
    fun getAllContents(): List<ContentEntity> {
        return runBlocking { repo.getAllContents().firstOrNull() ?: emptyList() }
    }

    /**
     * 封装资讯方法1 - 新增资讯信息函数，资讯包含的内容项必需齐全
     * @param title 新建资讯信息题目
     * @param imageUrl 新建资讯信息图片链接
     * @param description 新建资讯信息内容
     * @param longitude 新建资讯信息经度
     * @param latitude 新建资讯信息纬度
     * @param creatorId 创建者 ID
     */
    fun createContent(
        title: String,
        imageUrl: String,
        description: String,
        longitude: Double,
        latitude: Double,
        creatorId: Long
    ) {
        runBlocking {
            repo.createContent(title, imageUrl, description, longitude, latitude, creatorId)
        }
    }

    /**
     * 封装资讯方法2 - 编辑（更新）资讯信息函数，不需更新的内容可留空（维持原样）
     * @param id 被编辑的资讯信息 ID，必需
     * @param title 更新资讯信息题目
     * @param imageUrl 更新资讯信息图片链接
     * @param description 更新资讯信息内容
     * @param longitude 更新资讯信息经度
     * @param latitude 更新资讯信息纬度
     */
    @JvmOverloads
    fun editContent(
        id: Long,
        title: String? = null,
        imageUrl: String? = null,
        description: String? = null,
        longitude: Double? = null,
        latitude: Double? = null
    ) {
        runBlocking {
            repo.editContent(id, title, imageUrl, description, longitude, latitude)
        }
    }

    /**
     * 封装资讯方法3 - 根据 ID 删除资讯
     * @param id 待删除资讯条目的 ID
     */
    fun deleteContentByID(id: Long) {
        runBlocking { repo.deleteContentByID(id) }
    }





    /* User操作接口 */
    /**
     * 原生用户方法1 - 插入用户信息
     * @param user 用户实体
     */
    fun insertUser(user: UserEntity) {
        runBlocking { repo.insertUser(user) }
    }

    /**
     * 原生用户方法2 - 更新用户信息
     * @param user 用户实体
     */
    fun updateUser(user: UserEntity) {
        runBlocking { repo.updateUser(user) }
    }

    /**
     * 原生用户方法3 - 删除用户信息
     * @param user 用户实体
     */
    fun deleteUser(user: UserEntity) {
        runBlocking { repo.deleteUser(user) }
    }

    /**
     * 原生用户方法4 - 根据 ID 查找用户信息
     * @param id 用户 ID
     * @return 用户实体或 null
     */
    fun getUserById(id: Long): UserEntity? {
        return runBlocking { repo.getUserById(id).firstOrNull() }
    }

    /**
     * 原生用户方法5 - 根据用户名查找用户信息
     * @param username 用户名
     * @return 用户实体或 null
     */
    fun getUserByUsername(username: String): UserEntity? {
        return runBlocking { repo.getUserByUsername(username).firstOrNull() }
    }

    /**
     * 原生用户方法6 - 获取所有用户信息
     * @return 用户列表
     */
    fun getAllUsers(): List<UserEntity> {
        return runBlocking { repo.getAllUsers().firstOrNull() ?: emptyList() }
    }

    /**
     * 封装用户方法1 - 用户注册（仅填写用户名、密码）
     * @param username 新用户名
     * @param password 设定的密码
     * @return 注册完毕后的新用户 ID 或 null（当已存在时）
     */
    fun registerUser(username: String, password: String): Long? {
        return runBlocking { repo.registerUser(username, password) }
    }

    /**
     * 封装用户方法2 - 用户登录（匹配用户名和密码，密码未加密）
     * @param username
     * @param password
     * @return 登录的用户 ID 或 null（密码错误或用户不存在时）
     */
    fun loginUser(username: String, password: String): Long? {
        return runBlocking { repo.loginUser(username, password) }
    }

    /**
     * 封装用户方法3 - 用户完善个人信息（允许留空）
     * @param id 被编辑的用户 ID，必需
     * @param bio 自我介绍
     * @param contactInfo 联系方式
     * @param password 密码
     */
    @JvmOverloads
    fun updateUserProfile(
        id: Long,
        bio: String? = null,
        contactInfo: String? = null,
        password: String? = null
    ) {
        runBlocking {
            repo.updateUserProfile(id, bio, contactInfo, password)
        }
    }

    /**
     * 封装用户方法4 - 用户权限查询
     * @param id 被查询的用户 ID
     * @return True - 用户是管理员；False - 用户非管理员
     */
    fun getUserAdminStatus(id: Long): Boolean {
        return runBlocking { repo.getUserAdminStatus(id) }
    }
}