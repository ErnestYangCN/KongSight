package com.example.kongsight; // 请替换为您的实际包名

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminAccountActivity extends AppCompatActivity {

    // 声明UI组件
    private ImageView userAvatar;
    private TextView userName;
    private Button btnContentEdit;
    private Button btnEditProfile;
    private Button btnUserManagement;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_account); // 请确保XML文件名与此一致

        // 初始化UI组件
        initViews();

        // 设置按钮点击监听器
        setupClickListeners();

        // 可以在这里设置用户信息
        setupUserInfo();
    }

    /**
     * 初始化所有UI组件
     */
    private void initViews() {
        userAvatar = findViewById(R.id.userAvatar);
        userName = findViewById(R.id.userName);
        btnContentEdit = findViewById(R.id.btnContentEdit);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnUserManagement = findViewById(R.id.btnUserManagement);
        btnLogout = findViewById(R.id.btnLogout);
    }

    /**
     * 设置所有按钮的点击监听器
     */
    private void setupClickListeners() {
        // 内容编辑按钮
        btnContentEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到内容编辑界面
                Intent intent = new Intent(getApplicationContext(), ScenicListActivity.class);
                startActivity(intent);
            }
        });

        // 编辑个人资料按钮
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到编辑个人资料界面
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        // 用户管理按钮
        btnUserManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到用户管理界面
                Intent intent = new Intent(getApplicationContext(), AdminUserManageActivity.class);
                startActivity(intent);
            }
        });

        // 退出登录按钮
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 执行退出登录操作
                performLogout();
            }
        });
    }

    /**
     * 设置用户信息（示例）
     */
    private void setupUserInfo() {
        // 这里可以设置用户名和头像
        userName.setText("管理员"); // 设置实际用户名

        // 如果需要设置头像，可以使用以下代码
        // userAvatar.setImageResource(R.drawable.admin_avatar);
    }

    /**
     * 执行退出登录操作
     */
    private void performLogout() {
        // 这里可以添加退出登录的逻辑，例如：
        // 1. 清除用户登录状态
        // 2. 跳转到登录界面

        Intent intent = new Intent(AdminAccountActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * 可选：处理返回按钮点击
     */
    @Override
    public void onBackPressed() {
        // 可以在这里添加返回按钮的特殊处理
        super.onBackPressed();
    }
}