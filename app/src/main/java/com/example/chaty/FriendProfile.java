package com.example.chaty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class FriendProfile extends AppCompatActivity {
    TextView txtName,txtSex,txtDob;
    ImageView  imgAvt;

    ImageView imgBackFriendProfile;
    String token,profileId,phone,email,frAvater,frName,frDob,frSex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        token= getIntent().getStringExtra("token");
        profileId = getIntent().getStringExtra("profileId");
        email= getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        token= getIntent().getStringExtra("token");
        profileId = getIntent().getStringExtra("profileId");
        email= getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        frAvater = getIntent().getStringExtra("frAvater");
        frName = getIntent().getStringExtra("frName");
        frDob = getIntent().getStringExtra("frDob");
        frSex = getIntent().getStringExtra("frSex");
        txtName = findViewById(R.id.tvNameFriendProfile);
        txtDob = findViewById(R.id.tvDateFriendProfile);
        txtSex = findViewById(R.id.tvSexFriendProfile);
        imgAvt = findViewById(R.id.getAvatarFriendProfile);
        imgBackFriendProfile = findViewById(R.id.imgBackFriendProfile);
        txtDob.setText(frDob);
        txtName.setText(frName);
        txtSex.setText(frSex);
            if(frAvater.equalsIgnoreCase("http://chaty-api.herokuapp.com/file/avatar/smile.png"))
                imgAvt.setImageResource(R.drawable.smile);
            else{
                Glide.with(getApplicationContext())
                        .load(frAvater)
                        .into(imgAvt);}
        imgBackFriendProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendProfile.this, PhoneBook.class);

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