package com.example.chaty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
import com.example.chaty.Adapter.ItemChatAdapter;
import com.example.chaty.Item.ItemChat;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ImageView imgAvatar,imgLogOut,imgPhoneBook,imgFriendSuggestion,imgAdd,imgCreatePersonGroup;
    RecyclerView rcvItemChat;
    ArrayList<ItemChat> itemChats;
    ItemChatAdapter itemChatAdapter;
    public static String namePr;
    TextView txtName;
    String token,profileId,phone,email;
    @RequiresApi(api = Build.VERSION_CODES.O)
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
        imgCreatePersonGroup = findViewById(R.id.imgCreatePersonGroup);
        imgAdd = findViewById(R.id.imgAddPerson);

        //nhận dữ liệu
        token= getIntent().getStringExtra("token");
        profileId = getIntent().getStringExtra("profileId");
        email= getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");



        //call api
        getProfile(profileId);



        itemChatAdapter = new ItemChatAdapter(MainActivity.this,profileId,token,email,phone);
        rcvItemChat.setAdapter(itemChatAdapter);
        rcvItemChat.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FriendHome.class);
                //gửi dữ liệu
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();
            }
        });
        imgCreatePersonGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateGroupChat.class);
                //gửi dữ liệu
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                intent.putExtra("name",namePr);
                startActivity(intent);
                finish();
            }
        });
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
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent intent = new Intent(MainActivity.this, Login.class);
                                startActivity(intent);
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Xác nhận đăng xuất ").setPositiveButton("Đồng ý ", dialogClickListener)
                        .setNegativeButton("Không", dialogClickListener).show();

            }
        });
        imgPhoneBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhoneBook.class);
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();
                ;
            }
        });
        imgFriendSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FriendHome.class);
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
        String url =BuildConfig.API+"profile/"+profileID;
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
                            namePr= respObj2.get("name").toString();
                            String avatar = respObj2.get("avatar").toString();
                            //load ảnh từ db
                            if(avatar.equalsIgnoreCase(BuildConfig.API+"file/avatar/smile.png"))
                                imgAvatar.setImageResource(R.drawable.smile);
                            else{

                                Glide.with(getApplicationContext())
                                        .load(avatar)
                                        .into( imgAvatar);}




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