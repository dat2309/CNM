package com.example.chaty;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderItemFriendRequest extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

    ImageView imgAvatarFriendRequest,imgInfoFriendRequest;
    TextView tvNameFriendRequest;
    private ItemClickListener itemClickListener;

    public MyViewHolderItemFriendRequest(@NonNull View itemView) {
        super(itemView);
        imgAvatarFriendRequest = itemView.findViewById(R.id.imgAvatarFriendRequest);
        imgInfoFriendRequest = itemView.findViewById(R.id.imgInforFriendRequest);
        tvNameFriendRequest = itemView.findViewById(R.id.tvNameFriendRequest);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),true);
        return true;
    }
}
