package es.uma.passlocker.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uma.passlocker.R;
import es.uma.passlocker.db.entities.PasswordInfoEntity;

public class AuthKeyAdapter extends RecyclerView.Adapter<AuthKeyAdapter.AuthKeyViewHolder>{

    private List<PasswordInfoEntity> authKeyList;

    public AuthKeyAdapter(List<PasswordInfoEntity> authKeyList) {
        this.authKeyList = authKeyList;
    }

    public static class AuthKeyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAuthKey, tvUsername, tvSite;

        public AuthKeyViewHolder(View itemView) {
            super(itemView);
            tvAuthKey = itemView.findViewById(R.id.auth_key);
            tvUsername = itemView.findViewById(R.id.username);
            tvSite = itemView.findViewById(R.id.site);
        }
    }

    @NonNull
    @Override
    public AuthKeyAdapter.AuthKeyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new AuthKeyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AuthKeyAdapter.AuthKeyViewHolder holder, int position) {
        PasswordInfoEntity currentItem = authKeyList.get(position);
        holder.tvAuthKey.setText(currentItem.getAuthKey());
        holder.tvUsername.setText(currentItem.getUser().getUsername());
        holder.tvSite.setText(currentItem.getSite().getName());
    }

    @Override
    public int getItemCount() {
        return authKeyList.size();
    }
}
