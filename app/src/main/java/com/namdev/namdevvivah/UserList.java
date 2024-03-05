package com.namdev.namdevvivah;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class UserList extends RecyclerView.Adapter<UserList.MyViewHolder> {
    private List<User> userItem;
    private static Context context;
    public UserList(List<User> userList, Context context) {
        this.userItem = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlist,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = userItem.get(position);
        holder.bind(user);
        holder.username.setText(user.getUsername());
        //https://www.namdevvivah.com/namdevuser
        String imageUrl="https://gpsggc.com/assets/img/user.png";
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)) // Cache image
                .placeholder(R.drawable.ic_user) // Placeholder image while loading
                .error(R.drawable.ic_error) // Error image if loading fails
                .into(holder.userImg);
    }
    @Override
    public int getItemCount() {
        return userItem.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView usersId;
        TextView username;
        TextView userMobile;
        ImageView userImg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            usersId=itemView.findViewById(R.id.usersId);
            username=itemView.findViewById(R.id.username);
            userMobile=itemView.findViewById(R.id.userMobile);
            userImg=itemView.findViewById(R.id.userImg);
        }
        void bind(User user) {

            usersId.setText(user.getId());
            username.setText(user.getUsername());
            userMobile.setText(user.getMobile());
           // Glide.with(this).load("http://i.imgur.com/DvpvklR.png").into(userImg);
            // Load image from URL into ImageView using Glide

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("userId", user.getId());
                    intent.putExtra("userName", user.getUsername());
                    intent.putExtra("userMobile", user.getMobile());
                    context.startActivity(intent);
                }
            });
        }
    }
}