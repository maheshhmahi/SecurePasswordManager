package com.example.myapplication.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.List;
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

        private final List<User> userList;
        private final Context context;
        public UserAdapter(List<User> userList,Context context) {
            this.userList = userList;
            this.context = context;
        }

        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
            return new UserViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            User user = userList.get(position);
            Log.d("UserAdapter", "Header visibility for position " + position + ": " + user.isHeaderVisible());

            if (user.isHeaderVisible()) {
                holder.header.setVisibility(View.VISIBLE);
                holder.header.setText(String.valueOf(user.getWebsite().charAt(0)).toUpperCase());
            } else {
                holder.header.setVisibility(View.GONE);
            }

            holder.websiteTextView.setText(user.getWebsite());
            holder.usernameTextView.setText(user.getUsername());

            String faviconUrl = "https://www.google.com/s2/favicons?sz=64&domain=" + user.getUrl();

            Glide.with(context)
                    .load(faviconUrl)
                    .into(holder.websiteIcon);



        }

        @Override
        public int getItemCount() {
            return userList.size();
        }


        static class UserViewHolder extends RecyclerView.ViewHolder {
            TextView header, websiteTextView, usernameTextView;
            ImageView websiteIcon;

            public UserViewHolder(@NonNull View itemView) {
                super(itemView);
                header = itemView.findViewById(R.id.header);
                websiteTextView = itemView.findViewById(R.id.websiteTextView);
                usernameTextView = itemView.findViewById(R.id.usernameTextView);
                websiteIcon = itemView.findViewById(R.id.websiteIcon);
            }
        }
}

