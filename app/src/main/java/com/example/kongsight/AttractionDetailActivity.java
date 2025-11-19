package com.example.kongsight;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.kongsight.model.AppRepositorySync;
import com.example.kongsight.database.ContentEntity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttractionDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "AttractionDetailActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    // 核心组件
    private AppRepositorySync repository;
    private long attractionId;
    private String attractionName;

    // UI组件
    private ImageView attractionImage;
    private TextView titleText, descriptionText, locationStatus;
    private Button nearbyButton;
    private LinearLayout minimapContainer;

    // 地图组件
    private MapView minimapView;
    private GoogleMap googleMap;
    private boolean isMapReady = false;

    // 坐标数据
    private static final LatLng DEFAULT_HONG_KONG = new LatLng(22.3193, 114.1694);
    private static final float DEFAULT_ZOOM = 14.0f;

    // 香港主要景点坐标映射
    private static final Map<String, LatLng> ATTRACTION_COORDINATES = new HashMap<String, LatLng>() {{
        put("维多利亚港", new LatLng(22.2930, 114.1700));
        put("太平山顶", new LatLng(22.2756, 114.1475));
        put("香港迪士尼乐园", new LatLng(22.3131, 114.0443));
        put("海洋公园", new LatLng(22.2437, 114.1733));
        put("星光大道", new LatLng(22.2935, 114.1710));
        put("天坛大佛", new LatLng(22.2539, 113.9044));
        put("兰桂坊", new LatLng(22.2807, 114.1555));
        put("铜锣湾", new LatLng(22.2800, 114.1830));
        put("旺角", new LatLng(22.3193, 114.1694));
        put("尖沙咀", new LatLng(22.2975, 114.1722));
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_detail);

        Log.d(TAG, "Activity created");

        // 初始化Repository
        repository = new AppRepositorySync(this);

        // 获取Intent数据
        Intent intent = getIntent();
        attractionId = intent.getLongExtra("ATTRACTION_ID", -1);
        attractionName = intent.getStringExtra("ATTRACTION_NAME");

        Log.d(TAG, "Received attraction ID: " + attractionId + ", Name: " + attractionName);

        // 初始化视图
        initViews();

        // 初始化地图
        initMapView(savedInstanceState);

        // 加载景点详情
        loadAttractionDetail();
    }

    private void initViews() {
        attractionImage = findViewById(R.id.attraction_image);
        titleText = findViewById(R.id.title_text);
        descriptionText = findViewById(R.id.description_text);
        nearbyButton = findViewById(R.id.nearby_button);
        minimapContainer = findViewById(R.id.minimap_container);
        locationStatus = findViewById(R.id.location_status);

        // 返回按钮
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            Log.d(TAG, "Back button clicked");
            finish();
        });

        // 周边信息按钮
        nearbyButton.setOnClickListener(v -> {
            Log.d(TAG, "Nearby button clicked for: " + attractionName);
            Intent intent = new Intent(AttractionDetailActivity.this, NearbyPoiActivity.class);
            intent.putExtra("ATTRACTION_ID", attractionId);
            intent.putExtra("ATTRACTION_NAME", attractionName);
            startActivity(intent);
        });
    }

    private void initMapView(Bundle savedInstanceState) {
        Log.d(TAG, "Initializing map view");
        minimapView = findViewById(R.id.minimap_view);

        if (minimapView != null) {
            minimapView.onCreate(savedInstanceState);
            minimapView.getMapAsync(this);
            locationStatus.setText("地图初始化中...");
        } else {
            Log.e(TAG, "MapView not found in layout");
            locationStatus.setText("地图加载失败");
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "GoogleMap is ready");
        this.googleMap = googleMap;
        this.isMapReady = true;

        // 配置地图
        setupGoogleMap();

        // 更新地图位置
        updateMapWithAttractionLocation();
    }

    private void setupGoogleMap() {
        if (googleMap == null) return;

        // 启用基本交互
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);

        // 检查位置权限
        if (hasLocationPermission()) {
            enableMyLocation();
        } else {
            requestLocationPermission();
        }
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void enableMyLocation() {
        if (googleMap != null && hasLocationPermission()) {
            try {
                googleMap.setMyLocationEnabled(true);
                locationStatus.setText("位置服务已启用");
            } catch (SecurityException e) {
                Log.e(TAG, "Location permission not granted", e);
                locationStatus.setText("位置权限不足");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                locationStatus.setText("位置权限被拒绝");
            }
        }
    }

    private void updateMapWithAttractionLocation() {
        if (!isMapReady || googleMap == null) {
            Log.w(TAG, "Map not ready, skipping location update");
            return;
        }

        LatLng location = getAttractionLocation();
        String displayName = attractionName != null ? attractionName : "当前位置";

        // 清除旧标记
        googleMap.clear();

        // 添加新标记
        googleMap.addMarker(new MarkerOptions()
                .position(location)
                .title(displayName)
                .snippet("点击查看详情"));

        // 移动相机
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM));

        // 更新状态
        locationStatus.setText("已显示: " + displayName);
        Log.d(TAG, "Map updated with location: " + location);
    }

    private LatLng getAttractionLocation() {
        // 1. 首先尝试根据景点名称获取坐标
        if (attractionName != null) {
            LatLng coordinates = ATTRACTION_COORDINATES.get(attractionName);
            if (coordinates != null) {
                return coordinates;
            }

            // 尝试模糊匹配
            for (String key : ATTRACTION_COORDINATES.keySet()) {
                if (attractionName.contains(key) || key.contains(attractionName)) {
                    return ATTRACTION_COORDINATES.get(key);
                }
            }
        }

        // 2. 尝试从数据库获取
        if (attractionId != -1) {
            try {
                ContentEntity attraction = repository.getContentById(attractionId);
                if (attraction != null) {
                    // 这里可以扩展从数据库实体获取坐标的逻辑
                    // 暂时返回默认位置
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting attraction from database", e);
            }
        }

        // 3. 返回默认位置
        return DEFAULT_HONG_KONG;
    }

    private void loadAttractionDetail() {
        Log.d(TAG, "Loading attraction detail for ID: " + attractionId);

        if (attractionId == -1) {
            Log.w(TAG, "No attraction ID provided, loading test data");
            Toast.makeText(this, "未传递景点ID，加载测试数据", Toast.LENGTH_SHORT).show();
            loadTestData();
        } else {
            try {
                ContentEntity attraction = repository.getContentById(attractionId);
                if (attraction != null) {
                    displayAttractionDetail(attraction);
                    attractionName = cleanAttractionTitle(attraction.getTitle());
                    Log.d(TAG, "Successfully loaded attraction: " + attractionName);

                    // 更新地图
                    updateMapWithAttractionLocation();
                } else {
                    Log.w(TAG, "Attraction not found in database, loading test data");
                    Toast.makeText(this, "景点信息不存在，使用测试数据", Toast.LENGTH_SHORT).show();
                    loadTestData();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error loading attraction detail", e);
                Toast.makeText(this, "加载失败，使用测试数据", Toast.LENGTH_SHORT).show();
                loadTestData();
            }
        }
    }

    private void loadTestData() {
        ContentEntity testAttraction = getTestAttractionFromDB();
        if (testAttraction != null) {
            displayAttractionDetail(testAttraction);
            attractionId = testAttraction.getId();
            attractionName = cleanAttractionTitle(testAttraction.getTitle());
            Log.d(TAG, "Loaded test data from DB: " + attractionName);
        } else {
            loadHardCodedTestData();
        }

        // 更新地图
        updateMapWithAttractionLocation();
    }

    private ContentEntity getTestAttractionFromDB() {
        try {
            List<ContentEntity> allContents = repository.getAllContents();
            if (allContents == null || allContents.isEmpty()) {
                Log.w(TAG, "Database is empty");
                return null;
            }

            Log.d(TAG, "Found " + allContents.size() + " records in database");

            // 寻找真正的景点（非POI）
            for (ContentEntity content : allContents) {
                if (!isPoiContent(content)) {
                    Log.d(TAG, "Using attraction: " + content.getTitle());
                    return content;
                }
            }

            // 如果没有找到景点，使用第一条记录
            ContentEntity firstContent = allContents.get(0);
            Log.d(TAG, "Using first record: " + firstContent.getTitle());
            return firstContent;

        } catch (Exception e) {
            Log.e(TAG, "Database query error", e);
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
            Log.e(TAG, "Attempted to display null attraction");
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
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(attractionImage);
            Log.d(TAG, "Loaded image: " + imageUrl);
        } else {
            attractionImage.setImageResource(R.mipmap.ic_launcher);
            Log.d(TAG, "Using default image");
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
        Log.d(TAG, "Loading hardcoded test data");

        titleText.setText("维多利亚港");
        descriptionText.setText("维多利亚港是位于香港岛和九龙半岛之间的海港，世界三大天然良港之一。由于港阔水深，为天然良港，香港亦因而有东方之珠、世界三大夜景之美誉。");
        attractionName = "维多利亚港";
        attractionId = 1;

        TextView pageTitle = findViewById(R.id.page_title);
        pageTitle.setText("维多利亚港");
        attractionImage.setImageResource(R.mipmap.ic_launcher);

        Toast.makeText(this, "使用默认测试数据", Toast.LENGTH_SHORT).show();
    }

    // 地图生命周期管理
    @Override
    protected void onResume() {
        super.onResume();
        if (minimapView != null) {
            minimapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (minimapView != null) {
            minimapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (minimapView != null) {
            minimapView.onDestroy();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (minimapView != null) {
            minimapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (minimapView != null) {
            minimapView.onLowMemory();
        }
    }
}