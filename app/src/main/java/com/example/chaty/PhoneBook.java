package com.example.chaty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class PhoneBook extends AppCompatActivity {
    private ItemPhoneBookAdapter adapter;
    RecyclerView rcvListPhoneBook;
    ArrayList<ItemPhoneBook> itemPhoneBooks;
    ItemPhoneBookAdapter itemPhoneBookAdapter;
    ImageView imgBackPhoneBook;
    String token,profileId,email,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_book);
        token= getIntent().getStringExtra("token");
        profileId = getIntent().getStringExtra("profileId");
        email= getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        rcvListPhoneBook = findViewById(R.id.rcvListPhoneBook);
        imgBackPhoneBook = findViewById(R.id.imgBackPhoneBook);
        adapter = new ItemPhoneBookAdapter(PhoneBook.this,profileId,token,email,phone);
        rcvListPhoneBook.setLayoutManager(new GridLayoutManager(PhoneBook.this, 1));
        rcvListPhoneBook.setAdapter(adapter);

        imgBackPhoneBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneBook.this,MainActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();

            }
        });
    }

}