package com.example.chaty;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemFriendRequestAdapter extends RecyclerView.Adapter<MyViewHolderItemFriendRequest> {
    private List<ItemFriendRequest> itemFriendRequests;
    private Context context;

    public ItemFriendRequestAdapter(List itemFriendRequests, Context context)
    {
        this.itemFriendRequests = itemFriendRequests;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolderItemFriendRequest onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friendrequest,parent,false);
        return new MyViewHolderItemFriendRequest(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderItemFriendRequest holder, int position) {
        ItemFriendRequest itemFriendRequest = itemFriendRequests.get(position);

        holder.imgAvatarFriendRequest.setImageResource(itemFriendRequest.getImgAvatarFriendRequest());
        holder.imgInfoFriendRequest.setImageResource(itemFriendRequest.getImgInforFriendRequest());
        holder.tvNameFriendRequest.setText(itemFriendRequest.getTvNameFriendRequest());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context,FriendRequestProfile.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemFriendRequests.size();
    }
}
