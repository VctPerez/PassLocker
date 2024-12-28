package es.uma.passlocker.ui.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NameAdapter extends RecyclerView.Adapter<NameAdapter.NameViewHolder> {

    private final List<String> nameList;
    private final OnItemClickListener onItemClickListener;

    // Interfaz para manejar clics
    public interface OnItemClickListener {
        void onItemClick(String name) throws Exception;
    }

    public NameAdapter(List<String> nameList, OnItemClickListener onItemClickListener) {
        this.nameList = nameList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new NameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NameViewHolder holder, int position) {
        String name = nameList.get(position);
        holder.bind(name, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    static class NameViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;

        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String name, OnItemClickListener onItemClickListener) {
            tvName.setText(name);
            itemView.setOnClickListener(v -> {
                try {
                    onItemClickListener.onItemClick(name);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
