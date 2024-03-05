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

public class ProfileList extends RecyclerView.Adapter<ProfileList.MyViewHolder>{
    private List<Profileget> userItem;
    private static Context context;
    public ProfileList(List<Profileget> userList, Context context) {
        this.userItem = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profilelist,parent,false);
        return new ProfileList.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileList.MyViewHolder holder, int position) {
        Profileget profile = userItem.get(position);
        holder.bind(profile);
        holder.username.setText(profile.getUsername());
        String img = profile.getImage();
        //https://www.namdevvivah.com/namdevuser
        String imageUrl="";
        if(img.equals("NA")){
            imageUrl="https://www.namdevvivah.com/namdevuser/userimg/userphoto.png";
        }else {
            imageUrl="https://www.namdevvivah.com/namdevuser/"+img;
        }
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
        void bind(Profileget profile) {

            usersId.setText(profile.getId());
            username.setText(profile.getUsername());
            userMobile.setText(profile.getMobile());
            // Glide.with(this).load("http://i.imgur.com/DvpvklR.png").into(userImg);
            // Load image from URL into ImageView using Glide

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("userId", profile.getId());
                    intent.putExtra("userName", profile.getUsername());
                    intent.putExtra("userMobile", profile.getMobile());
                    context.startActivity(intent);
                }
            });
        }
    }
}
