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
     * 封装资讯方法1 - 新增景点信息函数，资讯包含的内容项必需齐全
     * @param title 新建景点信息题目
     * @param imageUrl 新建景点信息图片链接
     * @param description 新建景点信息内容
     * @param longitude 新建景点信息经度
     * @param latitude 新建景点信息纬度
     * @param creatorId 创建者 ID
     */
    fun createContent(
        title: String,
        imageUrl: String,
        description: String,
        longitude: Double,
        latitude: Double,
        creatorId: Long,
    ) {
        runBlocking {
            repo.createContent(title, imageUrl, description, longitude, latitude, creatorId)
        }
    }

    /**
     * 封装资讯方法2 - 新增周边信息函数，周边包含的内容项必需齐全
     * @param title 新建周边信息题目
     * @param imageUrl 新建周边信息图片链接
     * @param description 新建周边信息内容
     * @param longitude 新建周边信息经度
     * @param latitude 新建周边信息纬度
     * @param creatorId 创建者 ID
     * @param fatherId 所属景点 ID
     */
    fun createSurrounding(
        title: String,
        imageUrl: String,
        description: String,
        longitude: Double,
        latitude: Double,
        creatorId: Long,
        fatherId: Long?
    ) {
        runBlocking {
            repo.createSurrounding(title, imageUrl, description, longitude, latitude, creatorId, fatherId)
        }
    }

    /**
     * 封装资讯方法3 - 编辑（更新）资讯信息函数，不需更新的内容可留空（维持原样）；无法在景点和周边之间做更改
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
     * 封装资讯方法4 - 根据 ID 判断是景点还是周边
     * @param contentId 待查寻的信息都 ID
     * @return Boolean 若是景点返回 true 是周边返回 false
     */
    fun checkContentIsSceneOrNot(contentId: Long): Boolean {
        return runBlocking {
            repo.checkContentIsSceneOrNot(contentId)
        }
    }

    /**
     * 封装资讯方法 - 5: 根据父景点 ID 查询所有属于这个景点的周边
     * @param fatherId 父景点（即景点）的 ID
     * @return Flow<List<ContentEntity>> 返回所有周边列表
     */
    fun getSurroundingsByFatherId(fatherId: Long): Flow<List<ContentEntity>> {
        return runBlocking {
            repo.getSurroundingsByFatherId(fatherId)
        }
    }

    /**
     * 封装资讯方法6 - 根据 ID 删除资讯
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

    /* Nearby POI 操作接口 - 基于ContentEntity扩展 */
    /**
     * 获取指定景点的周边地点
     * @param attractionId 景点ID
     * @param category 分类：food-美食, transport-交通, entertainment-娱乐 (可选)
     * @return 周边地点列表
     */
    fun getNearbyPois(attractionId: Long, category: String? = null): List<ContentEntity> {
        return runBlocking {
            val allContents = repo.getAllContents().firstOrNull() ?: emptyList()
            allContents.filter { content ->
                content.description.startsWith("[POI]") && // 是周边地点
                        content.creatorId == attractionId && // 属于指定景点
                        (category == null || when(category) {
                            PoiCategory.FOOD -> content.title.contains("[美食]") || content.title.contains("餐厅") || content.title.contains("小吃") || content.title.contains("美食")
                            PoiCategory.TRANSPORT -> content.title.contains("[交通]") || content.title.contains("地铁") || content.title.contains("巴士") || content.title.contains("车站") || content.title.contains("交通")
                            PoiCategory.ENTERTAINMENT -> content.title.contains("[娱乐]") || content.title.contains("购物") || content.title.contains("影院") || content.title.contains("酒吧") || content.title.contains("娱乐")
                            else -> true
                        })
            }
        }
    }

    /**
     * 获取周边地点分类列表
     * @return 分类列表
     */
    fun getPoiCategories(): List<String> {
        return listOf(PoiCategory.FOOD, PoiCategory.TRANSPORT, PoiCategory.ENTERTAINMENT)
    }

    /**
     * 创建周边地点数据
     * @param attractionId 所属景点ID
     * @param name 地点名称
     * @param category 分类
     * @param address 地址（存储在description中）
     * @param latitude 纬度
     * @param longitude 经度
     * @param imageUrl 图片URL（可选）
     */
    @JvmOverloads
    fun createNearbyPoi(
        attractionId: Long,
        name: String,
        category: String,
        address: String,
        latitude: Double,
        longitude: Double,
        imageUrl: String? = null
    ) {
        val title = when(category) {
            PoiCategory.FOOD -> "[美食] $name"
            PoiCategory.TRANSPORT -> "[交通] $name"
            PoiCategory.ENTERTAINMENT -> "[娱乐] $name"
            else -> name
        }

        val description = "[POI] 地址：$address"
        val finalImageUrl = imageUrl ?: ""

        runBlocking {
            repo.createContent(
                title = title,
                imageUrl = finalImageUrl,
                description = description,
                longitude = longitude,
                latitude = latitude,
                creatorId = attractionId // 用creatorId存储所属景点ID
            )
        }
    }

    /**
     * 初始化示例景点数据（用于测试）
     */
    fun initSampleAttractionData() {
        // 先删除可能存在的旧数据（避免重复）
        val allContents = getAllContents()
        for (content in allContents) {
            deleteContentByID(content.id)
        }

        // 创建真正的景点数据 - ID从1开始
        createContent(
            title = "维多利亚港",
            imageUrl = "",
            description = "维多利亚港是位于香港岛和九龙半岛之间的海港，世界三大天然良港之一。由于港阔水深，为天然良港，香港亦因而有东方之珠、世界三大夜景之美誉。",
            longitude = 114.1713,
            latitude = 22.2945,
            creatorId = 1  // 创建者ID，不是景点ID
        )

        createContent(
            title = "香港迪士尼乐园",
            imageUrl = "",
            description = "香港迪士尼乐园位于大屿山，是亚洲第二座迪士尼乐园。乐园包括七大主题区，提供丰富的娱乐设施和表演。",
            longitude = 114.0410,
            latitude = 22.3080,
            creatorId = 1
        )

        // 然后创建周边地点数据，关联到景点ID 1（维多利亚港）
        createNearbyPoi(
            attractionId = 1,  // 关联到维多利亚港
            name = "星光餐厅",
            category = PoiCategory.FOOD,
            address = "尖沙咀海滨长廊",
            latitude = 22.2945,
            longitude = 114.1713
        )

        createNearbyPoi(
            attractionId = 1,
            name = "天星小轮码头",
            category = PoiCategory.TRANSPORT,
            address = "尖沙咀天星码头",
            latitude = 22.2937,
            longitude = 114.1697
        )
    }
}

/**
 * 周边地点分类常量
 */
object PoiCategory {
    const val FOOD = "food"
    const val TRANSPORT = "transport"
    const val ENTERTAINMENT = "entertainment"

    /**
     * 获取分类的显示名称（支持多语言）
     * @param category 分类常量
     * @param context Context用于获取资源
     * @return 显示名称
     */
    @JvmStatic
    fun getDisplayName(category: String, context: Context? = null): String {
        // 如果有Context，尝试从资源获取
        if (context != null) {
            try {
                val resourceId = when(category) {
                    FOOD -> context.resources.getIdentifier("category_food", "string", context.packageName)
                    TRANSPORT -> context.resources.getIdentifier("category_transport", "string", context.packageName)
                    ENTERTAINMENT -> context.resources.getIdentifier("category_entertainment", "string", context.packageName)
                    else -> context.resources.getIdentifier("other", "string", context.packageName)
                }
                if (resourceId != 0) {
                    return context.getString(resourceId)
                }
            } catch (e: Exception) {
                // 如果资源获取失败，使用默认值
            }
        }

        // 默认英文显示
        return when(category) {
            FOOD -> "Food"
            TRANSPORT -> "Transport"
            ENTERTAINMENT -> "Entertainment"
            else -> "Other"
        }
    }

    /**
     * 获取分类的关键词（用于搜索匹配）
     * @param category 分类常量
     * @return 关键词列表
     */
    @JvmStatic
    fun getSearchKeywords(category: String): List<String> {
        return when(category) {
            FOOD -> listOf( "[food]")
            TRANSPORT -> listOf( "[transport]")
            ENTERTAINMENT -> listOf("[entertainment]")
            else -> emptyList()
        }
    }

    /**
     * 获取所有分类列表
     * @return 分类常量列表
     */
    @JvmStatic
    fun getAllCategories(): List<String> {
        return listOf(FOOD, TRANSPORT, ENTERTAINMENT)
    }
}