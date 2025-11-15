package com.example.kongsight;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.kongsight.R;
import com.example.kongsight.adapter.BannerAdapter;
import com.example.kongsight.adapter.ContentAdapter;
import com.example.kongsight.database.ContentEntity;
import com.example.kongsight.model.AppRepositorySync;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements ContentAdapter.OnItemClickListener {

    private ViewPager2 bannerViewPager;
    private RecyclerView contentRecyclerView;
    private ContentAdapter contentAdapter;
    private BannerAdapter bannerAdapter;
    private AppRepositorySync repository;
    private Button profileButton;

    private Handler autoScrollHandler = new Handler();
    private Runnable autoScrollRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        repository = new AppRepositorySync(this);
        initViews();
        setupBanner();
        setupRecyclerView();
        loadContents();
        setupClickListeners();
    }

    private void initViews() {
        bannerViewPager = findViewById(R.id.bannerViewPager);
        contentRecyclerView = findViewById(R.id.contentRecyclerView);
        profileButton = findViewById(R.id.profileButton);
    }

    private void setupBanner() {
        try {
            // 从数据库获取景点数据作为轮播图
            List<ContentEntity> contents = repository.getAllContents();
            List<String> bannerImages = new ArrayList<>();

            if (contents != null && !contents.isEmpty()) {
                for (ContentEntity content : contents) {
                    if (content.getImageUrl() != null && !content.getImageUrl().isEmpty()) {
                        bannerImages.add(content.getImageUrl());
                    }
                }
            }

            // 如果数据库没有图片，使用默认图片
            if (bannerImages.isEmpty()) {
                bannerImages = Arrays.asList(
                        "https://picsum.photos/400/200?random=101",
                        "https://picsum.photos/400/200?random=102"
                );
            }

            bannerAdapter = new BannerAdapter(bannerImages);
            bannerViewPager.setAdapter(bannerAdapter);

        } catch (Exception e) {
            Log.e("HomePage", "Banner setup error: " + e.getMessage());
        }
    }

    private void setupRecyclerView() {
        contentAdapter = new ContentAdapter();
        contentAdapter.setOnItemClickListener(this);
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contentRecyclerView.setAdapter(contentAdapter);
    }

    private void loadContents() {
        List<ContentEntity> contents = repository.getAllContents();
        if (contents.isEmpty()) {
            Toast.makeText(this, "暂无景点信息", Toast.LENGTH_SHORT).show();
        } else {
            contentAdapter.setContents(contents);
        }
    }

    private void setupClickListeners() {
        profileButton.setOnClickListener(v -> {
            Toast.makeText(this, "跳转到个人页面", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onItemClick(ContentEntity content) {
        try {
            // 创建跳转到详情页面的Intent
            Intent intent = new Intent(HomePageActivity.this, AttractionDetailActivity.class);

            // 使用正确的键名传递景点信息
            intent.putExtra("ATTRACTION_ID", content.getId());
            intent.putExtra("ATTRACTION_NAME", content.getTitle());
            intent.putExtra("content_image_url", content.getImageUrl()); // 这个可以保留，如果需要的话

            // 启动详情页面
            startActivity(intent);

            Toast.makeText(this, "跳转到: " + content.getTitle(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("HomePage", "跳转失败: " + e.getMessage(), e);
            Toast.makeText(this, "跳转失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (autoScrollHandler != null && autoScrollRunnable != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
    }
}