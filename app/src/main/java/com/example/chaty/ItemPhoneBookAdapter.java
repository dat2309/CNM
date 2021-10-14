package com.example.chaty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemPhoneBookAdapter extends RecyclerView.Adapter<MyViewHolderItemPhoneBook>{

    private List<ItemPhoneBook> itemPhoneBooks;
    private Context context;

    public ItemPhoneBookAdapter(List itemPhoneBooks,Context context)
    {
        this.itemPhoneBooks =itemPhoneBooks;
        this.context=context;
    }
    @NonNull
    @Override
    public MyViewHolderItemPhoneBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phonebook,parent,false);
        return new MyViewHolderItemPhoneBook(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderItemPhoneBook holder, int position) {
        ItemPhoneBook itemPhoneBook = itemPhoneBooks.get(position);

        holder.imgAvatarPhoneBook.setImageResource(itemPhoneBook.getImgAvatarPhoneBook());
        holder.imgActiveStatus.setImageResource(itemPhoneBook.getImgActiveStatus());
        holder.imgInfoPhoneBook.setImageResource(itemPhoneBook.getImgInfoPhoneBook());
        holder.txtNamePhoneBook.setText(itemPhoneBook.getTxtNamePhoneBook());
//        holder.imgInfoPhoneBook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context,FriendProfile.class);
//                intent.putExtra("avt",itemPhoneBook.getImgAvatarPhoneBook());
//                intent.putExtra("status", itemPhoneBook.getImgActiveStatus());
//                intent.putExtra("info", itemPhoneBook.getImgInfoPhoneBook());
//                intent.putExtra("name",itemPhoneBook.getTxtNamePhoneBook());
//                context.startActivity(intent);
//            }
//      });
    }
    @Override
    public int getItemCount() {
        return itemPhoneBooks.size();
    }
}
