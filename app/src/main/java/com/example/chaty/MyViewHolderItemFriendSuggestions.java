package com.example.chaty;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderItemFriendSuggestions extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

    ImageView imgAvatarFriendSuggestions,imgInfoFriendSuggestions;
    TextView tvNameFriendSuggestions,tvPhoneNumberFriendSuggestions;
    private ItemClickListener itemClickListener;

    public MyViewHolderItemFriendSuggestions(@NonNull View itemView) {
        super(itemView);
        imgAvatarFriendSuggestions = itemView.findViewById(R.id.imgAvatarFriendSuggestions);
        imgInfoFriendSuggestions = itemView.findViewById(R.id.imgInforFriendSuggestions);
        tvNameFriendSuggestions = itemView.findViewById(R.id.tvNameFriendSuggestions);
        tvPhoneNumberFriendSuggestions = itemView.findViewById(R.id.tvPhoneNumberFriendSuggestions);
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
