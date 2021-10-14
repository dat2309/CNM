package com.example.chaty;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolderItemPhoneBook extends RecyclerView.ViewHolder {

    ImageView imgAvatarPhoneBook, imgActiveStatus, imgInfoPhoneBook;
    TextView txtNamePhoneBook;

    public MyViewHolderItemPhoneBook(@NonNull View itemView) {
        super(itemView);
        imgAvatarPhoneBook = itemView.findViewById(R.id.imgAvatarPhoneBook);
        imgActiveStatus = itemView.findViewById(R.id.imgActiveStatus);
        imgInfoPhoneBook = itemView.findViewById(R.id.imgInfoPhoneBook);
        txtNamePhoneBook = itemView.findViewById(R.id.txtNamePhoneBook);
    }
}
