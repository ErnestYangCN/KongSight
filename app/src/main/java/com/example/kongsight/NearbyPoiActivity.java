package com.example.kongsight;

import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kongsight.model.AppRepositorySync;
import com.example.kongsight.model.PoiCategory;
import com.example.kongsight.database.ContentEntity;
import java.util.ArrayList;
import java.util.List;

public class NearbyPoiActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PoiAdapter adapter;
    private AppRepositorySync repository;
    private long attractionId;
    private String attractionName;
    private List<ContentEntity> allPois = new ArrayList<>();

    // 分类选项（替换原来的RadioButton）
    private LinearLayout categoryContainer;
    private TextView categoryFood, categoryTransport, categoryEntertainment;
    private TextView currentSelectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_poi);

        // 初始化Repository
        repository = new AppRepositorySync(this);

        // 获取从页面4传递过来的数据
        Intent intent = getIntent();
        attractionId = intent.getLongExtra("ATTRACTION_ID", -1);
        attractionName = intent.getStringExtra("ATTRACTION_NAME");

        // 如果没有传递景点ID，使用默认值并创建测试数据
        if (attractionId == -1) {
            attractionId = 1; // 默认景点ID
            createTestData(); // 创建测试数据
        }

        // 初始化UI组件
        initViews();

        // 加载数据
        loadAllPois();

        // 设置分类切换监听
        setupCategoryListeners();
    }

    // 添加测试数据方法
    private void createTestData() {
        // 创建测试的周边地点数据
        repository.createNearbyPoi(
                attractionId,
                "星光餐厅",
                PoiCategory.FOOD,
                "尖沙咀海滨长廊",
                22.2945,
                114.1713,
                "" // 图片URL留空
        );

        repository.createNearbyPoi(
                attractionId,
                "天星小轮码头",
                PoiCategory.TRANSPORT,
                "尖沙咀天星码头",
                22.2937,
                114.1697,
                ""
        );

        repository.createNearbyPoi(
                attractionId,
                "海港城购物中心",
                PoiCategory.ENTERTAINMENT,
                "尖沙咀广东道3-27号",
                22.2950,
                114.1700,
                ""
        );

        repository.createNearbyPoi(
                attractionId,
                "半岛酒店下午茶",
                PoiCategory.FOOD,
                "尖沙咀梳士巴利道22号",
                22.2940,
                114.1720,
                ""
        );

        repository.createNearbyPoi(
                attractionId,
                "尖沙咀地铁站",
                PoiCategory.TRANSPORT,
                "尖沙咀弥敦道",
                22.2975,
                114.1725,
                ""
        );
    }

    private void initViews() {
        // 设置标题
        TextView titleText = findViewById(R.id.title_text);
        if (attractionName != null) {
            String title = getString(R.string.attraction_surrounding, attractionName);
            titleText.setText(title);
        } else {
            titleText.setText(R.string.nearby_info);
        }

        // 初始化RecyclerView
        recyclerView = findViewById(R.id.poi_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PoiAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        // 初始化分类选择器（使用TextView替代RadioButton）
        categoryContainer = findViewById(R.id.category_container);
        categoryFood = findViewById(R.id.category_food);
        categoryTransport = findViewById(R.id.category_transport);
        categoryEntertainment = findViewById(R.id.category_entertainment);

        // 设置按钮文本（使用多语言）
        categoryFood.setText(PoiCategory.getDisplayName(PoiCategory.FOOD, this));
        categoryTransport.setText(PoiCategory.getDisplayName(PoiCategory.TRANSPORT, this));
        categoryEntertainment.setText(PoiCategory.getDisplayName(PoiCategory.ENTERTAINMENT, this));

        // 设置默认选中项
        currentSelectedCategory = categoryFood;
        updateCategorySelection();

        // 返回按钮
        Button backButton = findViewById(R.id.back_button);
        backButton.setText(R.string.back);
        backButton.setOnClickListener(v -> finish());
    }

    private void loadAllPois() {
        // 获取所有周边地点（不按分类筛选）
        allPois = repository.getNearbyPois(attractionId, null);

        // 如果还是没有数据，创建测试数据
        if (allPois.isEmpty()) {
            createTestData();
            allPois = repository.getNearbyPois(attractionId, null);
        }

        if (allPois.isEmpty()) {
            Toast.makeText(this, R.string.no_surrounding_info, Toast.LENGTH_SHORT).show();
        } else {
            // 显示加载到的数据数量
            Toast.makeText(this, "加载到 " + allPois.size() + " 个周边地点", Toast.LENGTH_SHORT).show();
        }

        // 默认显示美食分类
        filterPoisByCategory(PoiCategory.FOOD);
    }

    private void setupCategoryListeners() {
        categoryFood.setOnClickListener(v -> {
            currentSelectedCategory = categoryFood;
            updateCategorySelection();
            filterPoisByCategory(PoiCategory.FOOD);
        });

        categoryTransport.setOnClickListener(v -> {
            currentSelectedCategory = categoryTransport;
            updateCategorySelection();
            filterPoisByCategory(PoiCategory.TRANSPORT);
        });

        categoryEntertainment.setOnClickListener(v -> {
            currentSelectedCategory = categoryEntertainment;
            updateCategorySelection();
            filterPoisByCategory(PoiCategory.ENTERTAINMENT);
        });
    }

    private void updateCategorySelection() {
        // 重置所有选项的颜色为浅灰色
        categoryFood.setTextColor(Color.parseColor("#CCCCCC"));
        categoryTransport.setTextColor(Color.parseColor("#CCCCCC"));
        categoryEntertainment.setTextColor(Color.parseColor("#CCCCCC"));

        // 设置当前选中项的颜色为深色
        if (currentSelectedCategory != null) {
            currentSelectedCategory.setTextColor(Color.parseColor("#333333"));
        }
    }

    private void filterPoisByCategory(String category) {
        List<ContentEntity> filteredPois = new ArrayList<>();

        for (ContentEntity poi : allPois) {
            if (isPoiInCategory(poi, category)) {
                filteredPois.add(poi);
            }
        }

        adapter.updateData(filteredPois);

        // 显示结果提示
        if (filteredPois.isEmpty()) {
            String categoryName = PoiCategory.getDisplayName(category, this);
            String message = getString(R.string.no_category_info, categoryName);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPoiInCategory(ContentEntity poi, String category) {
        String title = poi.getTitle().toLowerCase();
        List<String> keywords = PoiCategory.getSearchKeywords(category);

        for (String keyword : keywords) {
            if (title.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    // RecyclerView Adapter
    private class PoiAdapter extends RecyclerView.Adapter<PoiAdapter.ViewHolder> {
        private List<ContentEntity> poiList;
        private Context adapterContext;

        public PoiAdapter(List<ContentEntity> poiList, Context context) {
            this.poiList = poiList;
            this.adapterContext = context;
        }

        public void updateData(List<ContentEntity> newList) {
            this.poiList = newList;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            android.view.View view = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_poi, parent, false);
            return new ViewHolder(view, adapterContext);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ContentEntity poi = poiList.get(position);
            holder.bind(poi);
        }

        @Override
        public int getItemCount() {
            return poiList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView nameText, addressText, categoryText;
            private ImageView poiImage;
            private Context holderContext;

            public ViewHolder(android.view.View itemView, Context context) {
                super(itemView);
                holderContext = context;
                nameText = itemView.findViewById(R.id.poi_name);
                addressText = itemView.findViewById(R.id.poi_address);
                categoryText = itemView.findViewById(R.id.poi_category);
                poiImage = itemView.findViewById(R.id.poi_image);
            }

            public void bind(ContentEntity poi) {
                // 设置名称（去掉分类前缀）
                String name = cleanTitle(poi.getTitle());
                nameText.setText(name);

                // 设置地址（去掉[POI]前缀）
                String address = cleanDescription(poi.getDescription());
                addressText.setText(address);

                // 设置分类
                String category = detectCategory(poi.getTitle());
                categoryText.setText(category);

                // 点击item的事件
                itemView.setOnClickListener(v -> {
                    String clickedMessage = getString(R.string.clicked_item, name);
                    Toast.makeText(holderContext, clickedMessage, Toast.LENGTH_SHORT).show();
                });
            }

            private String cleanTitle(String title) {
                if (title == null) return "";

                // 移除所有语言的前缀（包括景点可能有的前缀）
                return title.replace("[美食]", "")
                        .replace("[交通]", "")
                        .replace("[娱乐]", "")
                        .replace("[food]", "")
                        .replace("[transport]", "")
                        .replace("[entertainment]", "")
                        .trim();
            }

            private String cleanDescription(String description) {
                if (description == null) return "";

                // 移除所有语言的POI前缀和地址前缀
                return description.replace("[POI]", "")
                        .replace("地址：", "")
                        .replace("Address:", "")
                        .trim();
            }

            private String detectCategory(String title) {
                if (title.contains("[美食]") || title.contains("[food]")) {
                    return getString(R.string.category_food);
                }
                if (title.contains("[交通]") || title.contains("[transport]")) {
                    return getString(R.string.category_transport);
                }
                if (title.contains("[娱乐]") || title.contains("[entertainment]")) {
                    return getString(R.string.category_entertainment);
                }
                return getString(R.string.other);
            }
        }
    }
}