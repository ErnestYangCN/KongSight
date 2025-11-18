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
        // 通过同步仓库接口获取所有景点
        List<ContentEntity> list = repository.getAllContents();
        view.showLoading(false);

        view.showScenicList(list);
    }

    @Override
    public void deleteScenic(long id) {
        if (view == null) return;

        view.showLoading(true);
        repository.deleteContentByID(id);
        view.showLoading(false);

        view.showMessage("已删除景点");
        loadScenicList(); // 刷新列表
    }

    @Override
    public void updateScenicTitle(long id, String newTitle) {
        if (view == null) return;

        if (newTitle == null || newTitle.trim().isEmpty()) {
            view.showError("标题不能为空");
            return;
        }

        view.showLoading(true);
        // 只更新 title，其余传 null 表示不改
        repository.editContent(
                id,
                newTitle,
                null,   // imageUrl
                null,   // description
                null,   // longitude
                null    // latitude
        );
        view.showLoading(false);

        view.showMessage("已更新标题");
        loadScenicList();
    }

    @Override
    public void addScenic(String title, String description) {
        if (view == null) return;

        if (title == null || title.trim().isEmpty()) {
            view.showError("标题不能为空");
            return;
        }

        view.showLoading(true);
        // 用默认值填充图片 / 经纬度 / 创建者
        repository.createContent(
                title,
                "",         // imageUrl
                description != null ? description : "",
                0.0,        // longitude
                0.0,        // latitude
                1L          // creatorId（随便给一个默认值）
        );
        view.showLoading(false);

        view.showMessage("已新增景点");
        loadScenicList();
    }
}
