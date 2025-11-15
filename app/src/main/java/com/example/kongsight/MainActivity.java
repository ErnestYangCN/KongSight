package com.example.kongsight;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 测试：跳转到页面4（景点详情页）
        testAttractionDetailPage();
    }

    private void testAttractionDetailPage() {
        Intent intent = new Intent(this, AttractionDetailActivity.class);
        intent.putExtra("ATTRACTION_ID", 1L); // 测试景点ID
        startActivity(intent);
        finish();
    }
}