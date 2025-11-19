/* 定义资讯数据类 */
package com.example.kongsight.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "content")
data class ContentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,                   // 主键：统一ID

    @ColumnInfo(name = "creation_time")
    val creationTime: Long,             // 条目创建时间戳

    @ColumnInfo(name = "content_type")
    val contentTypeIsScene: Boolean,    // 内容类型是否为景点：True 为景点，False 为周边
    @ColumnInfo(name = "parent_attraction_id")
    val surroundingFatherId: Long?,     // 周边的父景点。若为景点，则为 null

    val title: String,                  // 标题

    @ColumnInfo(name = "image_url")
    val imageUrl: String,               // 图片（URL或路径）

    val description: String,            // 描述

    val longitude: Double,              // 坐标：经度
    val latitude: Double,               // 坐标：纬度

    // 补充元素
    @ColumnInfo(name = "update_time")
    val updateTime: Long,               // 条目更新时间戳

    @ColumnInfo(name = "creator_id")
    val creatorId: Long,                // 条目创建者（引用User表的id）
)
