package com.example.kongsight;

import android.content.Entity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.example.kongsight.database.AppDAO;
import com.example.kongsight.database.UserEntity;
import com.example.kongsight.model.AppRepositorySync;
import com.google.android.material.textfield.TextInputEditText;


import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText etName,etEmail;
    private Button btnLogin,btnRegister;
    private ProgressBar progressBar;
    private TextView tvMessage;
    private AppRepositorySync repo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        repo = new AppRepositorySync(this);
        initViews();
        setupClickListeners();
    }

    private void initViews(){
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        tvMessage = findViewById(R.id.tvMessage);
    }

    private void setupClickListeners(){

        btnLogin.setOnClickListener(v->attempLogin());
        btnRegister.setOnClickListener(v->attempRegister());
    }
    private void attempRegister(){
        String name = Objects.requireNonNull(etName.getText(),"cannot be empty").toString().trim();
        String email = Objects.requireNonNull(etEmail.getText(),"cannot be empty").toString().trim();

        new Thread(()->{
            try{
                if(name!=null&&email!=null){
                    Long userId = repo.registerUser(name, email);
                    Bundle bundle = new Bundle();
                    bundle.putLong("UserId",userId);
                    Intent intent = new Intent(getApplicationContext(), AdminAccountActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){}
        }).start();
    }
    private void attempLogin(){
        String name = Objects.requireNonNull(etName.getText(),"cannot be empty").toString().trim();
        String email = Objects.requireNonNull(etEmail.getText(),"cannot be empty").toString().trim();

        setLoading(true);
        new Thread(()->{
            try {
                new Handler(Looper.getMainLooper()).post(()->{
                    setLoading(false);
                    if(repo.loginUser(name,email) == null){
                        showMessage("用户不存在或者密码错误",false);
                    }
                    else if(repo.loginUser(name,email) == 1){
                        Bundle bundle = new Bundle();
                        bundle.putLong("UserId",repo.loginUser(name,email));
                        showMessage("管理员欢迎回来",true);
                        Intent intent = new Intent(getApplicationContext(), AdminAccountActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else if(repo.loginUser(name,email)!=null){
                        Bundle bundle = new Bundle();
                        bundle.putLong("UserId",repo.loginUser(name,email));
                        showMessage("欢迎回来",true);
                        Intent intent = new Intent(getApplicationContext(), UserAccountActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else {
                        showMessage("错误邮箱",false);
                    }
                });
            }catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    setLoading(false);
                    showMessage("数据库错误：" + e.getMessage(), false);
                    Log.e("LoginActivity", "数据库操作失败", e);
                });
            }
        }

        ).start();
    }
    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!loading);
        btnRegister.setEnabled(!loading);

        if (loading) {
            btnLogin.setText("验证中...");
        } else {
            btnLogin.setText("登录");
        }
    }

    private void showMessage(String message, boolean isSuccess) {
        runOnUiThread(() -> {
            tvMessage.setText(message);

            // ✅ 兼容所有API版本的写法
            int colorRes = isSuccess ?
                    android.R.color.holo_green_dark :
                    android.R.color.holo_red_dark;
            tvMessage.setTextColor(ContextCompat.getColor(this, colorRes));

            tvMessage.setVisibility(View.VISIBLE);
        });
    }
}
