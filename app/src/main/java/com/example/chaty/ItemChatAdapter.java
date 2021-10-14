package com.example.chaty;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemChatAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<ItemChat> itemChats;
    private Context context;

    public ItemChatAdapter(List itemChats,Context context)
    {
        this.itemChats =itemChats;
        this.context=context;
       // itemChats = new ArrayList<>();

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.itemchat,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    ItemChat itemChat = itemChats.get(position);

    holder.imgAvatar.setImageResource(itemChat.getImgAvatar());
    holder.txtNameChat.setText( itemChat.getName());
    holder.txtMessage.setText(itemChat.getMessage());
    holder.txtTime.setText(itemChat.getTime());
    holder.imgAvatar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context,Chat.class);
            intent.putExtra("name",itemChat.getName());
            intent.putExtra("msg", itemChat.getMessage());
            intent.putExtra("time", itemChat.getTime());
            intent.putExtra("img",itemChat.getImgAvatar());
            context.startActivity(intent);
        }
    });
    }
    @Override
    public int getItemCount() {
        return itemChats.size();
    }

}
