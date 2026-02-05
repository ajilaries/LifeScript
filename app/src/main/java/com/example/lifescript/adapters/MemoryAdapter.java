package com.example.lifescript.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lifescript.R;
import com.example.lifescript.models.Memory;

import java.util.List;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {

    List<Memory> memoryList;

    public MemoryAdapter(List<Memory> memoryList) {
        this.memoryList = memoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_memory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Memory memory = memoryList.get(position);
        holder.tvTitle.setText("Memory #" + memory.getId());
        holder.tvDesc.setText(memory.getPromptText());
        holder.tvDate.setText(memory.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return memoryList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDesc, tvDate;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
