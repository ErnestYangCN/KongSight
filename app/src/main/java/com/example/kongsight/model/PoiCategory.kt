package com.example.kongsight.model

import android.content.Context

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
            FOOD -> listOf("[美食]", "餐厅", "小吃", "美食", "饭店", "餐馆", "[food]", "restaurant", "cafe")
            TRANSPORT -> listOf("[交通]", "地铁", "巴士", "车站", "交通", "码头", "轮渡", "[transport]", "subway", "bus")
            ENTERTAINMENT -> listOf("[娱乐]", "购物", "影院", "酒吧", "娱乐", "商场", "商店", "[entertainment]", "shopping", "cinema")
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