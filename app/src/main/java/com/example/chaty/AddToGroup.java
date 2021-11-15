package com.example.chaty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chaty.Adapter.ItemFriendAddToGroupAdapter;
import com.example.chaty.Item.ItemFriendAddToGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AddToGroup extends AppCompatActivity {
    RecyclerView rcvListFriend;
    ArrayList<ItemFriendAddToGroup> itemFriendAddToGroups;
    ItemFriendAddToGroupAdapter itemFriendAddToGroupAdapter;
    ImageView imgBackAddToGroup;
    String token, profileId, email, phone, frAvatar, frName, size, admin, frID;
    Button btnAdd;
    List member = new ArrayList();
    JSONArray participant = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_group);
        try {
            participant = new JSONArray(getIntent().getStringExtra("member"));
            for (int i = 0; i < participant.length(); i++)
                member.add(participant.getString(i));
            token = getIntent().getStringExtra("token");
            profileId = getIntent().getStringExtra("profileId");
            email = getIntent().getStringExtra("email");
            phone = getIntent().getStringExtra("phone");
            admin = getIntent().getStringExtra("admin");
            size = getIntent().getStringExtra("size");
            frAvatar = getIntent().getStringExtra("frAvatar");
            frName = getIntent().getStringExtra("frName");
            frID = getIntent().getStringExtra("frID");
            rcvListFriend = findViewById(R.id.rcvListFriendRemove);
            imgBackAddToGroup = findViewById(R.id.imgBackAddToGroup);
            btnAdd = findViewById(R.id.btnCreateRoomChat);
            itemFriendAddToGroups = new ArrayList<>();
            itemFriendAddToGroupAdapter = new ItemFriendAddToGroupAdapter(AddToGroup.this, profileId, token, frID, admin, member);
            rcvListFriend.setAdapter(itemFriendAddToGroupAdapter);
            rcvListFriend.setLayoutManager(new GridLayoutManager(AddToGroup.this, 1));
            imgBackAddToGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddToGroup.this, MenuChat.class);
                    intent.putExtra("frID", frID);
                    intent.putExtra("frAvatar", frAvatar);
                    intent.putExtra("frName", frName);
                    intent.putExtra("member", participant.toString());
                    intent.putExtra("token", token);
                    intent.putExtra("profileId", profileId);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("size", size);
                    intent.putExtra("admin", admin);
                    startActivity(intent);
                    finish();
                }
            });
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemFriendAddToGroupAdapter.addMemberToGroup();
                    Intent intent = new Intent(AddToGroup.this, MainActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("profileId", profileId);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                    finish();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}