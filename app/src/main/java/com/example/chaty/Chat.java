package com.example.chaty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Chat extends AppCompatActivity {

    ImageView imgReturnChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        imgReturnChat = findViewById(R.id.imgReturnProfile);
        imgReturnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chat.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}