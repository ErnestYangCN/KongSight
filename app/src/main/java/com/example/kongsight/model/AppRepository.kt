/**
 * Kotlin 调用友好
 * 此文件的函数 Java 无法直接调用，Java 请移步同文件夹下 AppRepositorySync 文件
 */
package com.example.kongsight.model

import android.content.Context
import com.example.kongsight.database.AppDatabase
import com.example.kongsight.database.ContentEntity
import com.example.kongsight.database.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class AppRepository(context: Context) {
    private val dao = AppDatabase.getDatabase(context).appDao()

    /* Content操作接口 */
    /**
     * 定义在 DAO 中的原生方法（5个）
     * 插入、更新、删除、根据 ID 查找、获取所有信息
     */
    suspend fun insertContent(content: ContentEntity) = dao.insertContent(content)
    suspend fun updateContent(content: ContentEntity) = dao.updateContent(content)
    suspend fun deleteContent(content: ContentEntity) = dao.deleteContent(content)
    fun getContentById(id: Long): Flow<ContentEntity?> = dao.getContentById(id)
    fun getAllContents(): Flow<List<ContentEntity>> = dao.getAllContents()

    /**
     * Content Operation 1: 新增资讯信息函数，资讯包含的内容项必须齐全
     * @param title 新建资讯信息题目
     * @param imageUrl 新建资讯信息图片链接
     * @param description 新建资讯信息内容
     * @param longitude 新建资讯信息经度
     * @param latitude 新建资讯信息纬度
     * @param creatorId 创建者 ID
     */
    suspend fun createContent(
        title: String,
        imageUrl: String,
        description: String,
        longitude: Double,
        latitude: Double,
        creatorId: Long
    ) {
        val currentTime = System.currentTimeMillis()
        val newContent = ContentEntity(
            creationTime = currentTime,
            title = title,
            imageUrl = imageUrl,
            description = description,
            longitude = longitude,
            latitude = latitude,
            updateTime = currentTime,
            creatorId = creatorId,
            contentTypeIsScene = true,
            surroundingFatherId = null
        )
        dao.insertContent(newContent)
    }

    /**
     * Content Operation 2: 新增周边信息函数，周边包含的内容项必须齐全
     * @param title 新建周边信息题目
     * @param imageUrl 新建周边信息图片链接
     * @param description 新建周边信息内容
     * @param longitude 新建周边信息经度
     * @param latitude 新建周边信息纬度
     * @param creatorId 创建者 ID
     */
    suspend fun createSurrounding(
        title: String,
        imageUrl: String,
        description: String,
        longitude: Double,
        latitude: Double,
        creatorId: Long,
        fatherID: Long?
    ) {
        val currentTime = System.currentTimeMillis()
        val newContent = ContentEntity(
            creationTime = currentTime,
            title = title,
            imageUrl = imageUrl,
            description = description,
            longitude = longitude,
            latitude = latitude,
            updateTime = currentTime,
            creatorId = creatorId,
            contentTypeIsScene = false,
            surroundingFatherId = fatherID
        )
        dao.insertContent(newContent)
    }

    /**
     * Content Operation 3: 编辑（更新）资讯信息函数，更新内容可留空（维持原样）；无法在景点和周边之间做更改
     * @param id 被编辑的资讯信息 ID，必需
     * @param title 更新资讯信息题目
     * @param imageUrl 更新资讯信息图片链接
     * @param description 更新资讯信息内容
     * @param longitude 更新资讯信息经度
     * @param latitude 更新资讯信息纬度
     */
    suspend fun editContent(
        id: Long,
        title: String? = null,
        imageUrl: String? = null,
        description: String? = null,
        longitude: Double? = null,
        latitude: Double? = null
    ) {
        val existing = getContentById(id).firstOrNull() ?: throw IllegalArgumentException("Content not found")
        val updated = existing.copy(
            title = title ?: existing.title,
            imageUrl = imageUrl ?: existing.imageUrl,
            description = description ?: existing.description,
            longitude = longitude ?: existing.longitude,
            latitude = latitude ?: existing.latitude,
            updateTime = System.currentTimeMillis()
        )
        dao.updateContent(updated)
    }

    /**
     * Content Operation 4: 根据 ID 判断是景点还是周边
     * @param contentId 待查寻的信息都 ID
     * @return Boolean 若是景点返回 true 是周边返回 false
     */
    fun checkContentIsSceneOrNot(contentId: Long): Boolean {
        return dao.checkContentIsSceneOrNot(contentId)
    }

    /**
     * Content Operation 5: 根据父景点 ID 查询所有属于这个景点的周边
     * @param fatherId 父景点（即景点）的 ID
     * @return Flow<List<ContentEntity>> 返回所有周边列表
     */
    fun getSurroundingsByFatherId(fatherId: Long): Flow<List<ContentEntity>> {
        return dao.getSurroundingsByFatherId(fatherId)
    }

    /**
     * Content Operation 6: 根据 ID 删除条目
     * @param id 待删除资讯条目的 ID
     */
    suspend fun deleteContentByID(id: Long) {
        val existing = getContentById(id).firstOrNull() ?: throw IllegalArgumentException("Content not found")
        deleteContent(existing)
    }

    /* User操作接口 */
    /**
     * 定义在 DAO 中的原生方法
     * 插入、更新、删除、根据 ID 查找、根据用户名查找、获取所有用户
     */
    suspend fun insertUser(user: UserEntity) = dao.insertUser(user)
    suspend fun updateUser(user: UserEntity) = dao.updateUser(user)
    suspend fun deleteUser(user: UserEntity) = dao.deleteUser(user)
    fun getUserById(id: Long): Flow<UserEntity?> = dao.getUserById(id)
    fun getUserByUsername(username: String): Flow<UserEntity?> = dao.getUserByUsername(username)
    fun getAllUsers(): Flow<List<UserEntity>> = dao.getAllUsers()

    /**
     * User Operation 1: 用户注册（仅填写用户名、密码）
     * @param username 新用户名
     * @param password 设定的密码
     * @return 注册完毕后的新用户 ID 或 null（当已存在时）
     * todo 登录页面代码应设计针对 null 的错误提示：用户已存在
     */
    suspend fun registerUser(username: String, password: String): Long? {
        if (dao.existsByUsername(username)) {
            return null
        }
        val user = UserEntity(
            username = username,
            password = password,
            bio = "",  // Default empty
            contactInfo = "",  // Default empty
            admin = false,  // Default non-admin
            registrationTime = System.currentTimeMillis()
        )
        dao.insertUser(user)
        // Retrieve the auto-generated ID
        return dao.getUserByUsername(username).firstOrNull()?.id
    }

    /**
     * User Operation 2: 用户登录（匹配用户名和密码，密码未加密）
     * @param username
     * @param password
     * @return 登录的用户 ID 或 null（密码错误或用户不存在时）
     * todo 登录页面代码应设计针对 null 的错误提示：用户不存在或密码错误
     */
    suspend fun loginUser(username: String, password: String): Long? {
        val user = dao.getUserByUsername(username).firstOrNull() ?: return null
        return if (user.password == password) user.id else null
    }

    /**
     * User Operation 3: 用户完善个人信息（允许留空）
     * @param id 被编辑的用户 ID，必需
     * @param bio 自我介绍
     * @param contactInfo 联系方式
     * @param password 密码
     */
    suspend fun updateUserProfile(
        id: Long,
        bio: String? = null,
        contactInfo: String? = null,
        password: String? = null  // Allow password change if needed
    ) {
        val existing = getUserById(id).firstOrNull() ?: throw IllegalArgumentException("User not found")
        val updated = existing.copy(
            bio = bio ?: existing.bio,
            contactInfo = contactInfo ?: existing.contactInfo,
            password = password ?: existing.password
        )
        dao.updateUser(updated)
    }

    /**
     * User Operation 4: 用户权限查询
     * @param id 被查询的用户 ID
     * @return True - 用户是管理员；False - 用户非管理员
     * 使用情形例：判断当前登录用户是否是管理员
     */
    suspend fun getUserAdminStatus(id: Long): Boolean {
        return dao.isUserAdmin(id) ?: false
    }
}