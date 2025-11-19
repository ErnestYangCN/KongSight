package com.example.kongsight.ui.scenic;

import com.example.kongsight.database.ContentEntity;
import com.example.kongsight.model.AppRepositorySync;

import java.util.List;

public class ScenicListPresenter implements ScenicListContract.Presenter {

    private ScenicListContract.View view;
    private final AppRepositorySync repository;

    public ScenicListPresenter(AppRepositorySync repository) {
        this.repository = repository;
    }

    @Override
    public void attach(ScenicListContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public void loadScenicList() {
        if (view == null) return;

        view.showLoading(true);
        List<ContentEntity> list = repository.getAllContents();
        view.showLoading(false);

        view.showScenicList(list);
    }
//
    @Override
    public void deleteScenic(long id) {
        if (view == null) return;

        view.showLoading(true);
        repository.deleteContentByID(id);
        view.showLoading(false);

        view.showMessage("has already delete the scenic");
        loadScenicList();
    }

    @Override
    public void updateScenic(long id,
                             String newTitle,
                             String newDescription,
                             String newImageUrl) {
        if (view == null) return;

        if (newTitle == null || newTitle.trim().isEmpty()) {
            view.showError("the title can‘t be empty");
            return;
        }

        view.showLoading(true);

        // 只改标题 / 描述 / 图片，经纬度保持不变（传 null 表示不更新）
        repository.editContent(
                id,
                newTitle,
                newImageUrl,
                newDescription,
                null,   // longitude
                null    // latitude
        );

        view.showLoading(false);
        view.showMessage("has updated the new information of scenic");
        loadScenicList();
    }

    @Override
    public void addScenic(String title, String description, String imageUrl) {
        if (view == null) return;

        if (title == null || title.trim().isEmpty()) {
            view.showError("the title can‘t be empty");
            return;
        }

        view.showLoading(true);

        repository.createContent(
                title,
                imageUrl != null ? imageUrl : "",
                description != null ? description : "",
                0.0,       // longitude 默认
                0.0,       // latitude 默认
                1L         // creatorId 默认
        );

        view.showLoading(false);
        view.showMessage("has added the scenic");
        loadScenicList();
    }
}
