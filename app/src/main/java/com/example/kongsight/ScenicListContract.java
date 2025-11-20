package com.example.kongsight;

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


        void loadScenicList();

        /** 删除景点 */
        void deleteScenic(long id);

        /** 修改景点：标题 + 描述 + 图片 */
        void updateScenic(long id, String newTitle, String newDescription, String newImageUrl);

        /** 新增景点：标题 + 描述 + 图片 */
        void addScenic(String title, String description, String imageUrl);
    }
}
