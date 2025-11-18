package com.example.kongsight.ui.scenic;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kongsight.R;
import com.example.kongsight.database.ContentEntity;
import com.example.kongsight.model.AppRepositorySync;
import com.example.kongsight.test.TestDB;

import java.util.List;

public class ScenicListActivity extends AppCompatActivity
        implements ScenicListContract.View {

    private ScenicListContract.Presenter presenter;
    private ScenicListAdapter adapter;

    private ProgressBar progressBar;
    private RecyclerView rvScenicList;
    private Button btnAddScenic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TestDB.testDB(this);
        
        setContentView(R.layout.activity_scenic_list);

        // 1. 初始化 Presenter（注入 AppRepositorySync）
        presenter = new ScenicListPresenter(new AppRepositorySync(this));
        presenter.attach(this);


        // 2. 找控件
        initViews();

        // 3. 设置 RecyclerView + Adapter
        initRecyclerView();

        // 4. 设置点击事件
        initListeners();

        // 5. 加载景点列表
        presenter.loadScenicList();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        rvScenicList = findViewById(R.id.rvScenicList);
        btnAddScenic = findViewById(R.id.btnAddScenic);
    }

    private void initRecyclerView() {
        adapter = new ScenicListAdapter(new ScenicListAdapter.OnItemActionListener() {
            @Override
            public void onEdit(ContentEntity entity) {
                showEditDialog(entity);
            }

            @Override
            public void onDelete(ContentEntity entity) {
                presenter.deleteScenic(entity.getId());
            }
        });
        rvScenicList.setLayoutManager(new LinearLayoutManager(this));
        rvScenicList.setAdapter(adapter);
    }

    private void initListeners() {
        btnAddScenic.setOnClickListener(v -> showAddDialog());
    }

    /** 编辑景点标题的弹窗 */
    private void showEditDialog(final ContentEntity scenic) {
        final EditText editText = new EditText(this);
        editText.setText(scenic.getTitle());

        new AlertDialog.Builder(this)
                .setTitle("编辑景点标题")
                .setView(editText)
                .setPositiveButton("保存", (dialog, which) -> {
                    String newTitle = editText.getText().toString();
                    presenter.updateScenicTitle(scenic.getId(), newTitle);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /** 新增景点的弹窗 */
    private void showAddDialog() {
        View dialogView = getLayoutInflater()
                .inflate(R.layout.dialog_add_scenic, null);
        final EditText etTitle = dialogView.findViewById(R.id.etTitle);
        final EditText etDescription = dialogView.findViewById(R.id.etDescription);

        new AlertDialog.Builder(this)
                .setTitle("新增景点")
                .setView(dialogView)
                .setPositiveButton("保存", (dialog, which) -> {
                    String title = etTitle.getText().toString();
                    String desc = etDescription.getText().toString();
                    presenter.addScenic(title, desc);
                })
                .setNegativeButton("取消", null)
                .show();
    }

    // --------------- ScenicListContract.View 实现 ---------------

    @Override
    public void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showScenicList(List<ContentEntity> list) {
        adapter.submitList(list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }
}
