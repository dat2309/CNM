package com.example.chaty;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemFriendSuggestionsAdapter extends RecyclerView.Adapter<MyViewHolderItemFriendSuggestions> {

    private List<ItemFriendSuggestions> itemFriendSuggestionss;
    private Context context;

    public ItemFriendSuggestionsAdapter(List itemFriendSuggestions, Context context)
    {
        this.itemFriendSuggestionss = itemFriendSuggestions;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolderItemFriendSuggestions onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friendsuggestions,parent,false);
        return new MyViewHolderItemFriendSuggestions(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderItemFriendSuggestions holder, int position) {
        ItemFriendSuggestions itemFriendSuggestions = itemFriendSuggestionss.get(position);

        holder.imgAvatarFriendSuggestions.setImageResource(itemFriendSuggestions.getImgFriendSuggestions());
        holder.imgInfoFriendSuggestions.setImageResource(itemFriendSuggestions.getImgInforFriendSuggestions());
        holder.tvNameFriendSuggestions.setText(itemFriendSuggestions.getTvNameFriendSuggestions());
        holder.tvPhoneNumberFriendSuggestions.setText(itemFriendSuggestions.getTvPhoneNumberFriendSuggestions());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context,FriendSuggestionsProfile.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemFriendSuggestionss.size();
    }
}
