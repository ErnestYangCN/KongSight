package com.example.kongsight.ui;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kongsight.R;
import com.example.kongsight.database.UserEntity;
import com.example.kongsight.model.AppRepositorySync;

public class EditProfileActivity extends AppCompatActivity {

    private AppRepositorySync repo;
    private long userId;

    private TextView tvUsername;
    private EditText etPassword, etBio, etContact;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        repo = new AppRepositorySync(this);

        // 登录界面通过 Intent 传入的用户 ID
        userId = getIntent().getLongExtra("user_id", -1);

        tvUsername = findViewById(R.id.tvUsername);
        etPassword = findViewById(R.id.etPassword);
        etBio = findViewById(R.id.etBio);
        etContact = findViewById(R.id.etContact);
        btnSave = findViewById(R.id.btnSave);

        loadUserData();

        btnSave.setOnClickListener(v -> saveChanges());
    }

    /** 从数据库加载用户信息 */
    private void loadUserData() {
        UserEntity user = repo.getUserById(userId);
        if (user != null) {
            tvUsername.setText(user.getUsername());
            etBio.setText(user.getBio() != null ? user.getBio() : "");
            etContact.setText(user.getContactInfo() != null ? user.getContactInfo() : "");
        }
    }

    /** 保存用户修改 */
    private void saveChanges() {
        String newPassword = etPassword.getText().toString().trim();
        String newBio = etBio.getText().toString().trim();
        String newContact = etContact.getText().toString().trim();

        // 空密码表示不修改，bio/contact为空则不修改
        repo.updateUserProfile(
                userId,
                newBio.isEmpty() ? null : newBio,
                newContact.isEmpty() ? null : newContact,
                newPassword.isEmpty() ? null : newPassword
        );

        Toast.makeText(this, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
