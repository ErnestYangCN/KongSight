package com.example.kongsight;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 直接跳转到页面3（HomePageActivity），不显示MainActivity的布局
        goToHomePage();
    }

    private void goToHomePage() {
        try {
            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish(); // 关闭当前MainActivity，避免按返回键回到这里
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}