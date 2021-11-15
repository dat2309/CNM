package com.example.chaty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chaty.Adapter.ItemFriendCreateGroupAdapter;
import com.example.chaty.Item.ItemFriendAddToGroup;

import java.util.ArrayList;

public class CreateGroupChat extends AppCompatActivity {
    RecyclerView rcvListFriend;
    ArrayList<ItemFriendAddToGroup> itemFriendCreateGroups;
    ItemFriendCreateGroupAdapter itemFriendCreateGroupAdapter;
    ImageView imgBackCreateGroup;
    String token, profileId, email, phone, name;
    Button btnCreat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_group);
        token = getIntent().getStringExtra("token");
        profileId = getIntent().getStringExtra("profileId");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        name = getIntent().getStringExtra("name");
        rcvListFriend = findViewById(R.id.rcvListFriendRemove);
        imgBackCreateGroup = findViewById(R.id.imgBackAddToGroup);
        btnCreat = findViewById(R.id.btnCreateRoomChat);
        btnCreat.setText("Tạo Nhóm");
        itemFriendCreateGroups = new ArrayList<>();

        itemFriendCreateGroupAdapter = new ItemFriendCreateGroupAdapter(CreateGroupChat.this, profileId, token, email, phone, name);
        rcvListFriend.setAdapter(itemFriendCreateGroupAdapter);
        rcvListFriend.setLayoutManager(new GridLayoutManager(CreateGroupChat.this, 1));
        imgBackCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateGroupChat.this, MainActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("profileId", profileId);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                startActivity(intent);
                finish();
            }
        });
        btnCreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemFriendCreateGroupAdapter.creatConversationGroup();
                Intent intent = new Intent(CreateGroupChat.this, MainActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("profileId", profileId);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                startActivity(intent);
                finish();
            }
        });
    }
}
