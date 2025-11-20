package com.example.kongsight.test;

import android.content.Context;
import android.util.Log;

import com.example.kongsight.database.ContentEntity;
import com.example.kongsight.database.UserEntity;
import com.example.kongsight.model.AppRepositorySync;

import java.util.List;

public class TestDBNew {

    private static final String TAG = "DbTest"; // 在 Logcat 中搜索 DbTest 查看日志

    public static void testDB(Context context) {
        // 这里使用同步版本，方便在 Java 中直接调用
        AppRepositorySync repo = new AppRepositorySync(context);

        try {
            // 1. 删除旧数据库（每次运行都从干净的状态开始）
            boolean deleted = context.deleteDatabase("tour_app_database"); // 注意：你的数据库文件名要和 AppDatabase 中一致
            if (deleted) {
                Log.d(TAG, "旧数据库删除成功");
            } else {
                Log.d(TAG, "没有旧数据库或删除失败");
            }

            // ====================== 用户相关测试 ======================
            Long userId;

            // 注册新用户
            userId = repo.registerUser("testuser", "password123");
            if (userId == null) {
                // 如果已经存在就直接登录
                userId = repo.loginUser("testuser", "password123");
                Log.d(TAG, "用户已存在，直接登录成功，ID = " + userId);
            } else {
                Log.d(TAG, "新用户注册成功，ID = " + userId);
            }

            // 更新个人资料（可选）
            repo.updateUserProfile(userId, "我是测试用户", "test@example.com", null);
            Log.d(TAG, "用户资料已更新");

            // ====================== 添加景点和周边数据（供前端测试） ======================

            // ---------- 景点 1：埃菲尔铁塔 ----------
            repo.createContent(
                    "维多利亚港",
                    "https://www.discoverhongkong.cn/content/dam/dhk/intl/explore/attractions/best-ways-to-marvel-at-iconic-victoria-harbour/best-ways-to-marvel-at-iconic-victoria-harbour-1920x1080.jpg",
                    "维多利亚港（英语：Victoria Harbour），简称维港，是位于香港的天然海港，位处香港岛与九龙半岛之间，两岸均为香港中心商业区。由于水深港阔，香港亦因此有“东方之珠”的美誉。",
                    22.283, 114.150, userId
            );
            Log.d(TAG, "景点添加成功：埃菲尔铁塔");

            // 获取刚刚添加的景点 ID（因为后面周边需要父 ID）
            List<ContentEntity> all = repo.getAllContents();
            long eiffelId = all.get(all.size() - 1).getId(); // 最新一条就是埃菲尔铁塔

            // 为埃菲尔铁塔添加两个周边
            repo.createSurrounding(
                    "[transport]塞纳河游船", "https://example.com/seine_cruise.jpg",
                    "在塞纳河上乘坐游船，夜晚灯光特别美。",
                    2.2950, 48.8600, userId, eiffelId
            );
            repo.createSurrounding(
                    "[food]铁塔餐厅 Le Jules Verne", "https://example.com/jules_verne.jpg",
                    "位于埃菲尔铁塔二层的米其林餐厅。",
                    2.2945, 48.8584, userId, eiffelId
            );
            repo.createSurrounding(
                    "[food]慕田峪缆车", "https://example.com/mutianyu_cable.jpg",
                    "[food]乘坐缆车上长城，省力又能看风景。",
                    116.5698, 40.4379, userId, eiffelId
            );
            Log.d(TAG, "埃菲尔铁塔的两个周边已添加");

            // ---------- 景点 2：长城 ----------
            repo.createContent(
                    "中国长城",
                    "https://example.com/great_wall.jpg",
                    "世界七大奇迹之一，古老的军事防御工程。",
                    116.570374, 40.431633, userId
            );
            Log.d(TAG, "景点添加成功：中国长城");

            all = repo.getAllContents();
            long greatWallId = all.get(all.size() - 1).getId();

            repo.createSurrounding(
                    "[food]慕田峪缆车", "https://example.com/mutianyu_cable.jpg",
                    "[food]乘坐缆车上长城，省力又能看风景。",
                    116.5698, 40.4379, userId, greatWallId
            );
            repo.createSurrounding(
                    "[food]八达岭野生动物园", "https://example.com/badaling_zoo.jpg",
                    "[food]距离八达岭长城很近的野生动物园。",
                    116.0069, 40.3632, userId, greatWallId
            );
            Log.d(TAG, "长城的两个周边已添加");

            // ---------- 景点 3：自由女神像 ----------
            repo.createContent(
                    "自由女神像",
                    "https://example.com/statue_of_liberty.jpg",
                    "美国纽约的象征，法国赠送的礼物。",
                    -74.044500, 40.689200, userId
            );
            Log.d(TAG, "景点添加成功：自由女神像");

            all = repo.getAllContents();
            long libertyId = all.get(all.size() - 1).getId();

            repo.createSurrounding(
                    "[food]埃利斯岛移民博物馆", "https://example.com/ellis_island.jpg",
                    "[food]紧邻自由女神像，记录美国移民历史。",
                    -74.0396, 40.6995, userId, libertyId
            );
            repo.createSurrounding(
                    "[food]自由女神渡轮", "https://example.com/liberty_ferry.jpg",
                    "[food]前往自由女神像必须乘坐的官方渡轮。",
                    -74.0467, 40.7022, userId, libertyId
            );
            Log.d(TAG, "自由女神像的两个周边已添加");

        } catch (Exception e) {
            Log.e(TAG, "测试过程中出现异常：" + e.getMessage(), e);
        }
    }
}