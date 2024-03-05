package com.namdev.namdevvivah;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class Massage extends RecyclerView.Adapter<Massage.MyViewHolder>{
    private List<Mass> userItem;
    private String imageUrl;
    private AdapterView.OnItemClickListener onItemClickListener;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    public Massage(List<Mass> userList, Context context) {
        this.userItem = userList;
        Massage.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.massage_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Mass mass = userItem.get(position);
        holder.bind(mass);
        String rightMsg = mass.getSender();
        String type = mass.getType();
        if(type.equals("1")){
            if(rightMsg.equals("R")){
                String img = mass.getImage();
                String image = "https://namdevvivah.com/"+img;
                Glide.with(holder.itemView.getContext())
                        .load(image)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)) // Cache image
                        .placeholder(R.drawable.ic_user) // Placeholder image while loading
                        .error(R.drawable.ic_error) // Error image if loading fails
                        .into(holder.leftImg);
                holder.leftImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(image);
                    }
                });
            }else{
                String img = mass.getImage();
                String image = "https://namdevvivah.com/"+img;
                Glide.with(holder.itemView.getContext())
                        .load(image)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)) // Cache image
                        .placeholder(R.drawable.ic_user) // Placeholder image while loading
                        .error(R.drawable.ic_error) // Error image if loading fails
                        .into(holder.rightImg);
                holder.rightImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(image);
                    }
                });
            }

        }



    }

    @Override
    public int getItemCount() {
        return userItem.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView left,leftTextImg;
        TextView right,rightTextImg;
        LinearLayout leftChat,leftImgChat;
        LinearLayout rightChat, rightImgChat;
        ImageView leftImg,rightImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            left=itemView.findViewById(R.id.left_chat_textview);
            right=itemView.findViewById(R.id.right_chat_textview);
            leftChat=itemView.findViewById(R.id.left_chat_layout);
            rightChat=itemView.findViewById(R.id.right_chat_layout);

            leftImgChat=itemView.findViewById(R.id.left_chat_imgLayout);
            rightImgChat=itemView.findViewById(R.id.right_chat_imgLayout);
            leftImg=itemView.findViewById(R.id.left_chat_image);
            rightImg=itemView.findViewById(R.id.right_chat_image);
            leftTextImg=itemView.findViewById(R.id.left_chat_imgTextview);
            rightTextImg=itemView.findViewById(R.id.right_chat_imgTextview);
        }
        void bind(Mass masse) {

            String rightMsg = masse.getSender();
            //String leftMsg = masse.getRecive();
            //String img = masse.getImage();
            String type = masse.getType();
            if(type.equals("1")){
                if(rightMsg.equals("R")){
                    rightImgChat.setVisibility(View.GONE);
                    leftImgChat.setVisibility(View.VISIBLE);
                }
                else{
                    leftImgChat.setVisibility(View.GONE);
                    rightImgChat.setVisibility(View.VISIBLE);
                }
                rightChat.setVisibility(View.GONE);
                leftChat.setVisibility(View.GONE);
                rightTextImg.setText(masse.getSender());
                leftTextImg.setText(masse.getRecive());
            }
            else {
                if(rightMsg.equals("R")){
                    leftChat.setVisibility(View.VISIBLE);
                    rightChat.setVisibility(View.GONE);
                }
                else{
                    rightChat.setVisibility(View.VISIBLE);
                    leftChat.setVisibility(View.GONE);
                }
                rightImgChat.setVisibility(View.GONE);
                leftImgChat.setVisibility(View.GONE);
                left.setText(masse.getId());
                right.setText(masse.getSender());
                left.setText(masse.getRecive());
            }
        }
    }
    private void showDialog(String imageUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_big_image, null);
        ImageView imageViewDialog = dialogView.findViewById(R.id.bigImageView);
        Glide.with(context)
                .load(imageUrl)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)) // Cache image
                .placeholder(R.drawable.ic_user) // Placeholder image while loading
                .error(R.drawable.ic_error)
                .into(imageViewDialog);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true); // Dismiss the dialog when touched outside

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                // Reset dialog state if needed
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
