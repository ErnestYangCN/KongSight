package com.example.kongsight;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kongsight.ui.EditProfileActivity;
import com.example.kongsight.ui.AdminUserManageActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnEditProfile;
    private Button btnAdminUserManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnAdminUserManage = findViewById(R.id.btnAdminUserManage);

        // 点击跳转到个人信息修改页面
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
            intent.putExtra("user_id", 1); // 测试用 user_id
            startActivity(intent);
        });

        // 点击跳转到管理员用户管理页面
        btnAdminUserManage.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdminUserManageActivity.class);
            startActivity(intent);
        });
    }
}
