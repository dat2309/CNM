package com.example.chaty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chaty.Adapter.MemberGroupAdapter;
import com.example.chaty.Item.ItemFriendAddToGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MemberGroup extends AppCompatActivity {
    RecyclerView rcvListFriend;
    ArrayList<ItemFriendAddToGroup> itemMemberGroups;
    MemberGroupAdapter itemMemberAdapter;
    ImageView imgBackMemberGroup;
    String token, profileId, email, phone, frAvatar, frName, size, admin, frID;
    Button btnRemove;
    List member = new ArrayList();
    JSONArray participant = null;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_group);
        txt = findViewById(R.id.textView19);
        txt.setText("Danh sách thành viên");
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
            imgBackMemberGroup = findViewById(R.id.imgBackAddToGroup);
            btnRemove = findViewById(R.id.btnCreateRoomChat);
            btnRemove.setVisibility(View.INVISIBLE);
            itemMemberGroups = new ArrayList<>();
            itemMemberAdapter = new MemberGroupAdapter(MemberGroup.this, profileId, token, frID, admin, member);
            rcvListFriend.setAdapter(itemMemberAdapter);
            rcvListFriend.setLayoutManager(new GridLayoutManager(MemberGroup.this, 1));
            imgBackMemberGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MemberGroup.this, MenuChat.class);
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    }
