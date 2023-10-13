package com.example.localhostconnect.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localhostconnect.R;
import com.example.localhostconnect.model.UserData;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<UserData> userList;

    public UserAdapter(List<UserData> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserData userData = userList.get(position);

        holder.idTextView.setText("Id: " + userData.getId());
        holder.nameTextView.setText("Name: " + userData.getName());
        holder.lastnameTextView.setText("Lastname: " + userData.getLastname());
        holder.birthDateTextView.setText("Birth Date: " + userData.getBirth_date());
        holder.ageTextView.setText("Age: " + userData.getAge());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView;
        TextView nameTextView;
        TextView lastnameTextView;
        TextView birthDateTextView;
        TextView ageTextView;

        UserViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            lastnameTextView = itemView.findViewById(R.id.lastNameTextView);
            birthDateTextView = itemView.findViewById(R.id.birthDateTextView);
            ageTextView = itemView.findViewById(R.id.ageTextView);
        }
    }
}
