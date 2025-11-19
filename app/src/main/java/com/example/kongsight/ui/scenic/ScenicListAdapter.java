package com.example.kongsight.ui.scenic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kongsight.R;
import com.example.kongsight.database.ContentEntity;

import java.util.ArrayList;
import java.util.List;

public class ScenicListAdapter extends RecyclerView.Adapter<ScenicListAdapter.ViewHolder> {

    public interface OnItemActionListener {
        void onEdit(ContentEntity entity);
        void onDelete(ContentEntity entity);
    }

    private final List<ContentEntity> items = new ArrayList<>();
    private final OnItemActionListener listener;

    public ScenicListAdapter(OnItemActionListener listener) {
        this.listener = listener;
    }

    public void submitList(List<ContentEntity> list) {
        items.clear();
        if (list != null) {
            items.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scenic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final TextView tvDescription;
        private final Button btnEdit;
        private final Button btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        void bind(final ContentEntity item) {
            tvTitle.setText(item.getTitle());
            tvDescription.setText(item.getDescription());

            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEdit(item);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDelete(item);
                }
            });
        }
    }
}
