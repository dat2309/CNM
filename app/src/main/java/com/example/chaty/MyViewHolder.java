package com.example.chaty;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView txtTime,txtMessage,txtNameChat;
    ImageView imgAvatar;

    public MyViewHolder(@NonNull View view) {
        super(view);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        txtNameChat = view.findViewById(R.id.txtNameChat);
        txtMessage = view.findViewById(R.id.txtMessage);
        txtTime = view.findViewById(R.id.txtTime);
    }
}
