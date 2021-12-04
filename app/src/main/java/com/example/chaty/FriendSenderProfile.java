package com.example.chaty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FriendSenderProfile extends AppCompatActivity {
    TextView txtName,txtSex,txtDob;
    ImageView imgAvt;
    ImageView imgBackFriendRequestProfile;
    RecyclerView rcvFriendRequest;
    String token,profileId,phone,email,frPhone,reqID,avatar;
    Button btnAccept, btnDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_sender_proflie);
        token= getIntent().getStringExtra("token");
        profileId = getIntent().getStringExtra("profileId");
        email= getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        reqID = getIntent().getStringExtra("reqID");
        frPhone = getIntent().getStringExtra("frPhone");

        txtName = findViewById(R.id.tvNameFriendSenderProfile);
        txtSex = findViewById(R.id.tvSexFriendSenderProfile);
        txtDob = findViewById(R.id.tvDateFriendSenderProfile);
        imgAvt = findViewById(R.id.imgAvatarFriendSenderProfile);
        findProfile(frPhone);
        btnDelete = findViewById(R.id.btnDeleteSender);
        imgBackFriendRequestProfile = findViewById(R.id.imgBackFriendSender);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteReq(reqID);

            }
        });
        imgBackFriendRequestProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendSenderProfile.this, FriendHome.class);
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();
            }
        });

    }
    private void findProfile(String receiver) {
        String url =BuildConfig.API+"profile?phone="+receiver;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));
                            JSONObject respObj2 = new JSONObject(respObj.get("data").toString());

                            txtName.setText(respObj2.get("name").toString());
                            txtDob.setText(respObj2.get("dob").toString());
                            if(respObj2.get("sex").equals(true)){
                                txtSex.setText("Nam");
                            }
                            else{
                                txtSex.setText("Nữ");
                            }
                            avatar = respObj2.get("avatar").toString();
                            if(avatar.equalsIgnoreCase(BuildConfig.API+"file/avatar/smile.png"))
                                imgAvt.setImageResource(R.drawable.smile);
                            else{
                                Glide.with(getApplicationContext())
                                        .load(respObj2.get("avatar"))
                                        .into(imgAvt);}

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        }){

            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("authorization",token );
                return headers;
            }
        };


        requestQueue.add(jsonObjectRequest);

    }
    private void deleteReq(String requestId) {
        String url =BuildConfig.API+"request/"+requestId;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));

                            Intent intent = new Intent(FriendSenderProfile.this,FriendHome.class);

                            intent.putExtra("token",token);
                            intent.putExtra("profileId",profileId);
                            intent.putExtra("email",email);
                            intent.putExtra("phone",phone);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        }){

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
