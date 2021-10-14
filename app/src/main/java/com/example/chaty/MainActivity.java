package com.example.chaty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ImageView imgAvatar,imgLogOut,imgPhoneBook,imgFriendSuggestion;
    RecyclerView rcvItemChat;
    ArrayList<ItemChat> itemChats;
    ItemChatAdapter itemChatAdapter;
    TextView txtName;
    String token,profileId,phone,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgAvatar =  findViewById(R.id.imgAvatar);
        imgLogOut = findViewById(R.id.imgLogout);
        txtName = findViewById(R.id.txtName);
        rcvItemChat = findViewById(R.id.rcvItemChat);
        imgPhoneBook = findViewById(R.id.imgPhoneBook);
        imgFriendSuggestion = findViewById(R.id.imgFriendSuggestion);
        //nhận dữ liệu
        token= getIntent().getStringExtra("token");
        profileId = getIntent().getStringExtra("profileId");
        email= getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");



        //call api
        getProfile(profileId);


        itemChats = new ArrayList<>();
        itemChats.add(new ItemChat(R.drawable.blueduck, "Văn Nam", "Xong deadline chưa", "20:00"));
        itemChats.add(new ItemChat(R.drawable.cuteduck, "Văn Tèo", "Mai xong", "21:00"));
        itemChats.add(new ItemChat(R.drawable.pinkduck, "Văn Tí", "Lại hẹn à", "22:00"));
        itemChats.add(new ItemChat(R.drawable.supermanduck, "Văn Teo", "Chắc chắc mai luôn", "23:00"));
        itemChats.add(new ItemChat(R.drawable.spiderduck, "Văn Tẹo", "Alo xong chưa", "8:00"));
        itemChats.add(new ItemChat(R.drawable.brownduck, "Văn Téo", "Mai nữa đi", "10:00"));
        itemChats.add(new ItemChat(R.drawable.blueduck, "Văn Nam", "Xong deadline chưa", "20:00"));
        itemChats.add(new ItemChat(R.drawable.cuteduck, "Văn Tèo", "Mai xong", "21:00"));
        itemChats.add(new ItemChat(R.drawable.pinkduck, "Văn Tí", "Lại hẹn à", "22:00"));
        itemChats.add(new ItemChat(R.drawable.supermanduck, "Văn Teo", "Chắc chắc mai luôn", "23:00"));
        itemChats.add(new ItemChat(R.drawable.spiderduck, "Văn Tẹo", "Alo xong chưa", "8:00"));
        itemChats.add(new ItemChat(R.drawable.brownduck, "Văn Téo", "Mai nữa đi", "10:00"));

        itemChats.add(new ItemChat(R.drawable.blueduck, "Văn Nam", "Xong deadline chưa", "20:00"));
        itemChats.add(new ItemChat(R.drawable.cuteduck, "Văn Tèo", "Mai xong", "21:00"));
        itemChats.add(new ItemChat(R.drawable.pinkduck, "Văn Tí", "Lại hẹn à", "22:00"));
        itemChats.add(new ItemChat(R.drawable.supermanduck, "Văn Teo", "Chắc chắc mai luôn", "23:00"));
        itemChats.add(new ItemChat(R.drawable.spiderduck, "Văn Tẹo", "Alo xong chưa", "8:00"));
        itemChats.add(new ItemChat(R.drawable.brownduck, "Văn Téo", "Mai nữa đi", "10:00"));

        itemChatAdapter = new ItemChatAdapter(itemChats,this);

        rcvItemChat.setAdapter(itemChatAdapter);
        rcvItemChat.setLayoutManager(new GridLayoutManager(MainActivity.this,1));

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Profile.class);
                //gửi dữ liệu
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();
            }
        });
        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Profile.class);
                startActivity(intent);
                finish();
            }
        });
        imgLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
//        imgPhoneBook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, PhoneBook.class);
//                startActivity(intent);
//                finish();
//            }
//        });
        imgFriendSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MakeFriend.class);
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();
            }
        });

    }
    private void getProfile(String profileID) {
        String url ="https://chaty-api.herokuapp.com/profile/"+profileID;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONObject respObj = new JSONObject(response);
                            Log.d("JSON", respObj.toString());
                            JSONObject respObj2 = new JSONObject(respObj.getString("data"));
//                            imgAvatar.setImageResource(Integer.parseInt(respObj2.get("avatar").toString()));
                            txtName.setText(respObj2.get("name").toString());
                            String avatar = respObj2.get("avatar").toString();
                            //load ảnh từ db
                            if(avatar.equalsIgnoreCase("http://chaty-api.herokuapp.com/file/avatar/smile.png"))
                                imgAvatar.setImageResource(R.drawable.smile);
                            else{
                            Glide.with(getApplicationContext())
                                    .load(respObj2.get("avatar"))
                                    .into(imgAvatar);}



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            //token
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("authorization",token );
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
}