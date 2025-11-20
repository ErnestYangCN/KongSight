// InitDB
package com.example.kongsight.util;

import android.content.Context;
import android.util.Log;

import com.example.kongsight.database.ContentEntity;
import com.example.kongsight.database.UserEntity;
import com.example.kongsight.model.AppRepositorySync;

import java.util.List;

/**
 * 初始化类，用于管理 APP 的各种初始化功能。
 * 每个初始化函数独立，便于单独调用或扩展添加更多初始化逻辑。
 */
public class InitDB {
    private static final String TAG = "InitDB"; // 日志标签

    private final Context context;
    private final AppRepositorySync repo;

    /**
     * 方法 0 - 类构造函数
     * 实例化 InitDB 对象后，方可调用类方法
     ***** e.g.
     * InitDb init = new InitDb(this);
     * init.deleteDB();
     *****
     * @param context 传入上下文变量
     */
    public InitDB(Context context) {
        this.context = context;
        this.repo = new AppRepositorySync(context);
    }

    /**
     * 方法 1 - 删除数据库
     */
    public void deleteDB() {
        boolean deleted = context.deleteDatabase("tour_app_database");
        if (deleted) {
            Log.d(TAG, "数据库删除成功");
        } else {
            Log.d(TAG, "没有数据库或删除失败");
        }
    }

    /**
     * 方法 2 - 初始化（创建）管理员账号
     * 用户名: admin, 密码: admin123, 其他字段留空
     * @return 返回创建或已存在的管理员 ID
     */
    public long adminInit() {
        String adminUsername = "admin";
        String adminPassword = "admin123";

        // 检查管理员是否存在
        UserEntity admin = repo.getUserByUsername(adminUsername);
        if (admin == null) {
            // 使用全参数构造函数创建新管理员
            long registrationTime = System.currentTimeMillis();
            UserEntity newAdmin = new UserEntity(
                    1, // id，默认将此管理员设为 1
                    adminUsername,
                    adminPassword,
                    "", // bio
                    "", // contactInfo
                    true, // admin
                    registrationTime
            );

            // 插入用户
            repo.insertUser(newAdmin);

            // 重新获取 ID
            admin = repo.getUserByUsername(adminUsername);
            if (admin == null) {
                throw new IllegalStateException("创建管理员失败");
            }
            Log.d(TAG, "管理员创建成功，ID = " + admin.getId());
        } else {
            Log.d(TAG, "管理员已存在，ID = " + admin.getId());
        }
        return admin.getId();
    }

    /**
     * 方法 3 - 初始化资讯数据：添加固定景点和周边信息
     * 如果已存在（通过标题检查），则跳过。
     */
    public void contentInit(long adminId) {
        // 获取所有现有资讯，用于检查是否存在
        List<ContentEntity> allContents = repo.getAllContents();

        // ---------- 景点 1：维多利亚港 ----------
        String title1 = "维多利亚港";
        if (!contentExists(allContents, title1)) {
            repo.createContent(
                    title1,
                    "https://www.discoverhongkong.cn/content/dam/dhk/intl/explore/attractions/best-ways-to-marvel-at-iconic-victoria-harbour/best-ways-to-marvel-at-iconic-victoria-harbour-1920x1080.jpg",
                    "维多利亚港（英语：Victoria Harbour），简称维港，是位于香港的天然海港，位处香港岛与九龙半岛之间，两岸均为香港中心商业区。由于水深港阔，香港亦因此有“东方之珠”的美誉。",
                    22.30131389305076, 114.16691979570172, adminId
            );
            Log.d(TAG, "景点添加成功：" + title1);

            // 获取刚刚添加的景点 ID
            allContents = repo.getAllContents();  // 刷新列表
            long fatherId1 = getContentIdByTitle(allContents, title1);

            // 添加周边
            repo.createSurrounding(
                    "[transport]尖沙咀地铁站", "https://thumbs.dreamstime.com/z/%E5%B0%96%E6%B2%99%E5%92%80mtr%E6%A0%87%E5%BF%97%EF%BC%8C%E4%B8%80%E5%9C%B0%E9%93%81%E4%B8%AD%E6%AD%A2%E5%9C%A8%E9%A6%99%E6%B8%AF-58963173.jpg",
                    "前往维港最适合的地铁站点，可搭乘港铁荃湾线前往",
                    22.298070098626205, 114.17231903897684, adminId, fatherId1
            );
            repo.createSurrounding(
                    "[entertainment]香港艺术馆", "https://www.discoverhongkong.com/content/dam/dhk/intl/explore/arts-entertainment/best-museums-in-hong-kong-for-art-and-culture-lovers/top6-museum-header-16-9.jpg",
                    "香港艺术馆，位于维多利亚港旁、香港九龙油尖旺区尖沙咀梳士巴利道10号，是香港展览本地、中国及世界各地的艺术品的主要场地，馆藏逾17,000件、涵盖中国书画、古代文物、外销艺术、画作、现代艺术及香港艺术家的创作成果等。香港艺术馆不时举行各类型的艺术展览。",
                    22.29379910403179, 114.17211349513511, adminId, fatherId1
            );
            repo.createSurrounding(
                    "[food]Vesu Pizza Bar", "https://static7.orstatic.com/userphoto3/photo/2O/24K1/0F4EFQ6C972D2430F813DElx.jpg?og=1",
                    "Vesu Pizza Bar 是位于香港尖沙咀北京道一号（One Peking）30楼的一家意大利餐厅，以手工制作的那不勒斯风格比萨和创意鸡尾酒闻名，灵感来源于维苏威火山脚下。该店提供午餐和晚餐，营业时间为周五、周六上午11:30至凌晨1:00，周日至周四至午夜12:00，并在2024年入选亚太地区50佳比萨店第44位。",
                    22.29617988461857, 114.16977914047489, adminId, fatherId1
            );
            Log.d(TAG, title1 + " 的周边已添加");
        }

        // ---------- 景点 2：香港海洋公园及水上乐园 ----------
        String title2 = "香港海洋公园";
        if (!contentExists(allContents, title2)) {
            repo.createContent(
                    title2,
                    "https://www.discoverhongkong.cn/content/dam/dhk/intl/explore/attractions/fun-filled-attractions-and-activities-to-try/ocean-park-and-water-world-ocean-park-1920x1080.jpg",
                    "香港海洋公园是香港本地独特的主题公园，提供多元化的世界级海洋主题景点、刺激机动游戏和现场表演，近年更积极投放资源于保育及教育，打造全港最大、最佳的大自“研”教室。",
                    22.24677955598568, 114.17571402721137, adminId
            );
            Log.d(TAG, "景点添加成功：" + title2);

            // 获取 ID
            allContents = repo.getAllContents();
            long fatherId2 = getContentIdByTitle(allContents, title2);

            // 添加周边
            repo.createSurrounding(
                    "[transport]海洋公园地铁站", "https://cdn.hk01.com/di/media/images/276916/org/e64f89910afe65ec59459343a3892515.jpg/pUvv5wGoWPXvb4CWw5kMFsA-6A5WKB2eNG3wHDRt8Bw?v=w1280r16_9",
                    "前往香港海洋公园，可搭乘港铁南港岛线到海洋公园地铁站下车",
                    22.248984787963547, 114.17445485277277, adminId, fatherId2
            );
            repo.createSurrounding(
                    "[food]香港海洋公园万豪酒店", "https://media.oceanpark.com.hk/files/s3fs-public/hkgop_swimming_pool_1900x1000.jpg",
                    "位于海洋公园旁的万豪酒店，下有许多特色餐厅，兼顾饮食与住宿",
                    22.247130819074624, 114.17481178671417, adminId, fatherId2
            );
            repo.createSurrounding(
                    "[entertainment]英皇戏院 (THE SOUTHSIDE)", "https://prd-cdn.capital-hk.com/133131_Entrance_1_scaled_e23a092ef5.jpg",
                    "位于海洋公园西侧的英皇戏院，多部新片热映中",
                    22.248121818676164, 114.16876709500823, adminId, fatherId2
            );
            Log.d(TAG, title2 + " 的周边已添加");
        }

        // ---------- 景点 3：中环街市 ----------
        String title3 = "中环街市";
        if (!contentExists(allContents, title3)) {
            repo.createContent(
                    title3,
                    "https://www.18hall.com/wp-content/uploads/2023/01/%E4%B8%AD%E7%92%B0%E8%A1%97%E5%B8%82%E7%BE%8E%E9%A3%9F.jpg",
                    "在中环街市这个大众的游乐场中寻找无限乐趣！这座极具意义的建筑采取“无边界空间”的设计概念，让大家能够共同活动，感受不一样的文化体验。崭新面貌的中环街市不但保育了街市的建筑特色，加上各方面完善配套，将有助促进本地品牌和初创企业的成长。",
                    22.28440164178044, 114.1553854150231, adminId
            );
            Log.d(TAG, "景点添加成功：" + title3);

            // 获取 ID
            allContents = repo.getAllContents();
            long fatherId3 = getContentIdByTitle(allContents, title3);

            // 添加周边
            repo.createSurrounding(
                    "[transport]港铁香港站", "https://upload.wikimedia.org/wikipedia/commons/2/2d/Hong_Kong_Station_Exterior_202008.jpg",
                    "距离中环街市最近的地铁站，可乘坐港铁东涌线或机场快线抵达",
                    22.28482694508624, 114.15816143032991, adminId, fatherId3
            );
            repo.createSurrounding(
                    "[entertainment]香港摩天轮", "https://2bunny.tw/wp-content/uploads/2024/09/2024-0925-hk-observation-wheel.jpg",
                    "位于维港海畔 60 米高的观景摩天轮，设有封闭式空调座舱，可欣赏壮观的海港景色。",
                    22.285445299163676, 114.16172545161233, adminId, fatherId3
            );
            repo.createSurrounding(
                    "[food]珍妮曲奇聪明小熊（上环店）", "https://sillycoupleblog.tw/wp-content/uploads/2024/10/collage-14.jpg",
                    "经典香港本土曲奇品牌的官方直营店",
                    22.285194057211246, 114.15371547879352, adminId, fatherId3
            );
            Log.d(TAG, title3 + " 的周边已添加");
        }
    }

    // 辅助方法：检查标题是否存在
    private boolean contentExists(List<ContentEntity> contents, String title) {
        for (ContentEntity content : contents) {
            if (content.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }

    // 辅助方法：根据标题获取 ID（假设标题唯一）
    private long getContentIdByTitle(List<ContentEntity> contents, String title) {
        for (ContentEntity content : contents) {
            if (content.getTitle().equals(title)) {
                return content.getId();
            }
        }
        throw new IllegalStateException("未找到标题为 " + title + " 的资讯");
    }
}