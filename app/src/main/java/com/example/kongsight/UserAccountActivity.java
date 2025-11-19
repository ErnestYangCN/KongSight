package com.example.kongsight;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserAccountActivity extends AppCompatActivity {

    // 声明UI组件
    private ImageView userAvatar;
    private TextView userName;
    private Button btnEditProfile;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account); // 请确保XML文件名与此一致
        Bundle bundle =getIntent().getExtras();
        long userid = bundle.getLong("UserId");

        // 初始化UI组件
        initViews();

        // 设置按钮点击监听器
        setupClickListeners(userid);

        // 设置用户信息
        setupUserInfo();
    }

    /**
     * 初始化所有UI组件
     */
    private void initViews() {
        userAvatar = findViewById(R.id.userAvatar);
        userName = findViewById(R.id.userName);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);
    }

    /**
     * 设置按钮点击监听器
     */
    private void setupClickListeners(long userid) {
        // 编辑个人资料按钮
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到编辑个人资料界面
                Bundle bundle = new Bundle();
                bundle.putLong("UserId",userid);
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                intent.putExtras(bundle);
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
     * 设置用户信息
     */
    private void setupUserInfo() {
        // 这里可以从SharedPreferences、数据库或Intent获取用户信息
        // 示例：设置用户名
        String name = getIntent().getStringExtra("USER_NAME");
        if (name != null) {
            userName.setText(name);
        } else {
            userName.setText("用户"); // 默认用户名
        }

        // 如果需要设置头像，可以使用以下代码
        // userAvatar.setImageResource(R.drawable.user_avatar);
    }

    /**
     * 执行退出登录操作
     */
    private void performLogout() {
        // 这里可以添加退出登录的逻辑，例如：

        // 1. 清除用户登录状态（SharedPreferences）
        // SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        // SharedPreferences.Editor editor = preferences.edit();
        // editor.remove("is_logged_in");
        // editor.remove("user_id");
        // editor.apply();

        // 2. 跳转到登录界面
        Intent intent = new Intent(UserAccountActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

        // 可选：显示退出登录提示
        // Toast.makeText(this, "已退出登录", Toast.LENGTH_SHORT).show();
    }

    /**
     * 可选：在onResume中刷新用户信息
     */
    @Override
    protected void onResume() {
        super.onResume();
        // 当从编辑资料页面返回时，可以在这里更新显示的信息
        refreshUserInfo();
    }

    /**
     * 刷新用户信息
     */
    private void refreshUserInfo() {
        // 从数据源重新加载并更新用户信息
        // 例如：userName.setText(loadUserNameFromPrefs());
    }

    /**
     * 处理返回按钮点击
     */
    @Override
    public void onBackPressed() {
        // 可以返回到上一个界面，或者直接退出
        super.onBackPressed();

        // 或者返回到主界面
        // Intent intent = new Intent(this, MainActivity.class);
        // startActivity(intent);
        // finish();
    }
}