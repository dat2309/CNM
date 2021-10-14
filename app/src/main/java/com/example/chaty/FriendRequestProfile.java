package com.example.chaty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class FriendRequestProfile extends AppCompatActivity {

    ImageView imgBackFriendRequestProfile;
    RecyclerView rcvFriendRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request_profile);
        imgBackFriendRequestProfile = findViewById(R.id.imgBackFriendRequest);
        imgBackFriendRequestProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendRequestProfile.this, MakeFriend.class);

                startActivity(intent);
                finish();
            }
        });

    }
}