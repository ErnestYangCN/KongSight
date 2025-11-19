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
        setContentView(R.layout.activity_scenic_list);

        presenter = new ScenicListPresenter(new AppRepositorySync(this));
        presenter.attach(this);

        initViews();
        initRecyclerView();
        initListeners();

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


    private void showEditDialog(final ContentEntity scenic) {
        View dialogView = getLayoutInflater()
                .inflate(R.layout.dialog_add_scenic, null);
        final EditText etTitle = dialogView.findViewById(R.id.etTitle);
        final EditText etDescription = dialogView.findViewById(R.id.etDescription);
        final EditText etImageUrl = dialogView.findViewById(R.id.etImageUrl);

        // 预填旧数据
        etTitle.setText(scenic.getTitle());
        etDescription.setText(scenic.getDescription());
        etImageUrl.setText(scenic.getImageUrl());

        new AlertDialog.Builder(this)
                .setTitle("Edit Scenic")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newTitle = etTitle.getText().toString();
                    String newDesc = etDescription.getText().toString();
                    String newImageUrl = etImageUrl.getText().toString();

                    presenter.updateScenic(
                            scenic.getId(),
                            newTitle,
                            newDesc,
                            newImageUrl
                    );
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    private void showAddDialog() {
        View dialogView = getLayoutInflater()
                .inflate(R.layout.dialog_add_scenic, null);
        final EditText etTitle = dialogView.findViewById(R.id.etTitle);
        final EditText etDescription = dialogView.findViewById(R.id.etDescription);
        final EditText etImageUrl = dialogView.findViewById(R.id.etImageUrl);

        new AlertDialog.Builder(this)
                .setTitle("Add new scenic")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String title = etTitle.getText().toString();
                    String desc = etDescription.getText().toString();
                    String imageUrl = etImageUrl.getText().toString();

                    presenter.addScenic(title, desc, imageUrl);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ------------ ScenicListContract.View 实现 ------------

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
