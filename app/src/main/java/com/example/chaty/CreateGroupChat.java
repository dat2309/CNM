package com.example.chaty;

import static com.example.chaty.Adapter.ItemFriendCreateGroupAdapter.itemFriendCreateToGroupLists;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chaty.Adapter.ItemFriendAddToGroupAdapter;
import com.example.chaty.Adapter.ItemFriendCreateGroupAdapter;
import com.example.chaty.Item.ItemFriendAddToGroup;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupChat extends AppCompatActivity {
    RecyclerView rcvListFriend;
    ArrayList<ItemFriendAddToGroup> itemFriendCreateGroups;
    ItemFriendCreateGroupAdapter itemFriendCreateGroupAdapter;
    ImageView imgBackCreateGroup;
    String token, profileId, email, phone, name;
    EditText edtSearch;
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


        initView();
        edtSearch = findViewById(R.id.edtSearchAddToGroup);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0)
                    initView();
                else
                    filter(s.toString());
            }
        });
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
                itemFriendCreateGroupAdapter.createGroupChat();
            }
        });
    }
    public void filter(String text) {
        List<ItemFriendAddToGroup> filteredList = new ArrayList<>();
        for (ItemFriendAddToGroup item : itemFriendCreateToGroupLists) {
            if(item.getTvNameFriendAddToGroup().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        itemFriendCreateGroupAdapter.filterList(filteredList);
        itemFriendCreateGroupAdapter.notifyDataSetChanged();

    }
    public void initView() {

        itemFriendCreateGroupAdapter = new ItemFriendCreateGroupAdapter(CreateGroupChat.this, profileId, token, email, phone, name);
        rcvListFriend.setAdapter(itemFriendCreateGroupAdapter);
        rcvListFriend.setLayoutManager(new GridLayoutManager(CreateGroupChat.this, 1));
    }
}
