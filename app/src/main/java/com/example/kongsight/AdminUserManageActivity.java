package com.example.kongsight;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kongsight.database.UserEntity;
import com.example.kongsight.model.AppRepositorySync;

import java.util.List;

public class AdminUserManageActivity extends AppCompatActivity {

    private AppRepositorySync repo;
    private ListView listUsers;
    private List<UserEntity> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_manage);

        repo = new AppRepositorySync(this);
        listUsers = findViewById(R.id.listUsers);

        loadUserList();
    }

    /** 加载所有用户并显示在 ListView */
    private void loadUserList() {
        users = repo.getAllUsers();

        // 构建显示列表：ID + Username
        String[] userDisplay = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            UserEntity u = users.get(i);
            userDisplay[i] = "ID: " + u.getId() + ", Username: " + u.getUsername();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                userDisplay
        );

        listUsers.setAdapter(adapter);

        listUsers.setOnItemClickListener((parent, view, position, id) -> {
            UserEntity selected = users.get(position);
            showDeleteDialog(selected);
        });
    }

    /** 弹出删除用户确认框 */
    private void showDeleteDialog(UserEntity user) {
        new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete user: " + user.getUsername() + " ?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    repo.deleteUser(user);
                    Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show();
                    loadUserList(); // 刷新列表
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
