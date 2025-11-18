package com.example.kongsight.ui.scenic;

import com.example.kongsight.database.ContentEntity;

import java.util.List;

public interface ScenicListContract {

    interface View {
        void showLoading(boolean show);

        void showError(String message);

        void showMessage(String message);

        void showScenicList(List<ContentEntity> list);
    }

    interface Presenter {
        void attach(View view);

        void detach();

        /** 查询所有景点 */
        void loadScenicList();

        /** 删除景点 */
        void deleteScenic(long id);

        /** 更新景点标题（示例只改标题） */
        void updateScenicTitle(long id, String newTitle);

        /** 新增景点（示例只用标题 + 描述，其它字段给默认值） */
        void addScenic(String title, String description);
    }
}
