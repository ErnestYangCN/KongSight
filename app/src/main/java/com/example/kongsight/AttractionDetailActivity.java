package com.example.kongsight;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.kongsight.model.AppRepositorySync;
import com.example.kongsight.database.ContentEntity;
import com.example.kongsight.test.TestDB;
import java.util.List;

public class AttractionDetailActivity extends AppCompatActivity {

    private AppRepositorySync repository;
    private long attractionId;
    private String attractionName;

    private ImageView attractionImage;
    private TextView titleText, descriptionText;
    private Button nearbyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_detail);

        // 首先确保测试数据存在
        Log.d("TestDB", "正在初始化测试数据...");
        //TestDB.testDB(this);
        Log.d("TestDB", "测试数据初始化完成");

        // 初始化Repository
        repository = new AppRepositorySync(this);

        // 获取从页面3传递过来的景点ID
        Intent intent = getIntent();
        attractionId = intent.getLongExtra("ATTRACTION_ID", -1);
        attractionName = intent.getStringExtra("ATTRACTION_NAME");

        // 初始化UI组件
        initViews();

        // 加载景点详情数据
        loadAttractionDetail();
    }

    private void initViews() {
        attractionImage = findViewById(R.id.attraction_image);
        titleText = findViewById(R.id.title_text);
        descriptionText = findViewById(R.id.description_text);
        nearbyButton = findViewById(R.id.nearby_button);

        // 返回按钮
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // 周边信息按钮
        nearbyButton.setOnClickListener(v -> {
            Intent intent = new Intent(AttractionDetailActivity.this, NearbyPoiActivity.class);
            intent.putExtra("ATTRACTION_ID", attractionId);
            intent.putExtra("ATTRACTION_NAME", attractionName);
            startActivity(intent);
        });

    }

    private void loadAttractionDetail() {
        // 如果从页面3没有传递景点ID，使用测试数据
        if (attractionId == -1) {
            Toast.makeText(this, "未传递景点ID，尝试加载测试数据...", Toast.LENGTH_SHORT).show();
            loadTestData();
        } else {
            // 使用传递过来的景点ID从数据库加载数据
            ContentEntity attraction = repository.getContentById(attractionId);
            if (attraction != null) {
                displayAttractionDetail(attraction);
                attractionName = cleanAttractionTitle(attraction.getTitle());
                Toast.makeText(this, "成功加载景点: " + attractionName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "景点信息不存在，使用测试数据", Toast.LENGTH_SHORT).show();
                loadTestData();
            }
        }
    }

    private void loadTestData() {
        // 首先尝试从数据库获取测试数据
        ContentEntity testAttraction = getTestAttractionFromDB();
        if (testAttraction != null) {
            displayAttractionDetail(testAttraction);
            attractionId = testAttraction.getId();
            attractionName = cleanAttractionTitle(testAttraction.getTitle());
            Toast.makeText(this, "已加载数据库测试数据: " + attractionName, Toast.LENGTH_SHORT).show();
        } else {
            // 如果数据库中没有数据，使用硬编码的测试数据
            loadHardCodedTestData();
        }
    }

    private ContentEntity getTestAttractionFromDB() {
        try {
            // 从数据库获取所有内容
            List<ContentEntity> allContents = repository.getAllContents();

            if (allContents == null || allContents.isEmpty()) {
                Toast.makeText(this, "数据库为空，没有测试数据", Toast.LENGTH_SHORT).show();
                return null;
            }

            Log.d("DBDebug", "数据库中找到 " + allContents.size() + " 条记录");

            // 调试：显示所有内容的标题
            for (ContentEntity content : allContents) {
                Log.d("DBDebug", "内容标题: " + content.getTitle() + ", 描述: " + content.getDescription());
            }

            // 优先选择真正的景点（不是POI）
            for (ContentEntity content : allContents) {
                if (!isPoiContent(content)) {
                    Log.d("DBDebug", "找到景点: " + content.getTitle());
                    return content;
                }
            }

            // 如果没有找到真正的景点，返回第一个
            ContentEntity firstContent = allContents.get(0);
            Log.d("DBDebug", "使用第一条记录: " + firstContent.getTitle());
            return firstContent;

        } catch (Exception e) {
            Log.e("DBDebug", "数据库查询错误: " + e.getMessage());
            Toast.makeText(this, "数据库查询错误: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private boolean isPoiContent(ContentEntity content) {
        if (content == null) return false;

        String description = content.getDescription();
        String title = content.getTitle();

        // 如果描述以[POI]开头，则是周边地点
        if (description != null && description.startsWith("[POI]")) {
            return true;
        }

        // 如果标题包含分类标签，则是周边地点
        if (title != null) {
            return title.contains("[美食]") || title.contains("[交通]") ||
                    title.contains("[娱乐]") || title.contains("[food]") ||
                    title.contains("[transport]") || title.contains("[entertainment]");
        }

        return false;
    }

    private void displayAttractionDetail(ContentEntity attraction) {
        if (attraction == null) {
            Toast.makeText(this, "景点数据为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 设置标题
        String cleanTitle = cleanAttractionTitle(attraction.getTitle());
        titleText.setText(cleanTitle);

        // 设置描述
        String cleanDescription = cleanAttractionDescription(attraction.getDescription());
        descriptionText.setText(cleanDescription);

        // 加载图片
        String imageUrl = attraction.getImageUrl();

        if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.equals("null")) {
            // 使用Glide加载网络图片
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(attractionImage);
            Log.d("ImageLoad", "加载图片: " + imageUrl);
        } else {
            attractionImage.setImageResource(R.mipmap.ic_launcher);
            Log.d("ImageLoad", "使用默认图片");
        }

        // 设置页面标题
        TextView pageTitle = findViewById(R.id.page_title);
        pageTitle.setText(cleanTitle);
    }

    private String cleanAttractionTitle(String title) {
        if (title == null) return "未知景点";
        return title.replace("[美食]", "")
                .replace("[交通]", "")
                .replace("[娱乐]", "")
                .replace("[food]", "")
                .replace("[transport]", "")
                .replace("[entertainment]", "")
                .trim();
    }

    private String cleanAttractionDescription(String description) {
        if (description == null) return "暂无描述";
        return description.replace("[POI]", "")
                .replace("地址：", "")
                .replace("Address:", "")
                .trim();
    }

    private void loadHardCodedTestData() {
        // 硬编码测试数据
        titleText.setText("维多利亚港");
        descriptionText.setText("维多利亚港是位于香港岛和九龙半岛之间的海港，世界三大天然良港之一。由于港阔水深，为天然良港，香港亦因而有东方之珠、世界三大夜景之美誉。");
        attractionName = "维多利亚港";
        attractionId = 1;

        TextView pageTitle = findViewById(R.id.page_title);
        pageTitle.setText("维多利亚港");
        attractionImage.setImageResource(R.mipmap.ic_launcher);

        Toast.makeText(this, "使用硬编码测试数据", Toast.LENGTH_SHORT).show();
    }

    private ContentEntity getTestAttraction() {
        // 兼容旧方法
        return getTestAttractionFromDB();
    }
}