package com.example.kongsight;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.kongsight.test.*;
import com.example.kongsight.util.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
       /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

        /**
         * 测试
         */
        // TestKt.updateTextAfterDelay(this); // 这行用来测试 Java + Kt 的兼容
        // TestDB.testDB(this); // 这行用来测试 Java 下的数据库接口是否可用，测试样例来自 test/TestDB，可供接口调用参考

        /**
         * 初始化数据库：位于 util/initDb.java
         */
        InitDb init = new InitDb(this); // 实例化
        init.deleteDB(); // 删库
        init.adminInit(); // 内置管理员账号 - ID 为 0
        init.contentInit(1); // 初始化资讯消息



        // 测试：跳转到页面4（景点详情页）
        // testAttractionDetailPage();
    }

    private void testAttractionDetailPage() {
        // 跳转到首页（HomePageActivity - 页面3）
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

        // 可选：关闭当前登录页面
        // finish();
    }

}