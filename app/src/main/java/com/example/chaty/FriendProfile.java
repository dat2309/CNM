package com.example.chaty;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FriendProfile extends AppCompatActivity {
    TextView txtName,txtSex,txtDob;
    ImageView  imgAvt;
    Button btnBlock,btnHuy;
    ImageView imgBackFriendProfile;
    String token,profileId,phone,email,frAvater,frName,frDob,frSex,frID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        token= getIntent().getStringExtra("token");
        profileId = getIntent().getStringExtra("profileId");
        email= getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        frAvater = getIntent().getStringExtra("frAvater");
        frName = getIntent().getStringExtra("frName");
        frDob = getIntent().getStringExtra("frDob");
        frSex = getIntent().getStringExtra("frSex");
        frID = getIntent().getStringExtra("frID");
        Log.d("frID",frID);
        txtName = findViewById(R.id.tvNameFriendProfile);
        txtDob = findViewById(R.id.tvDateFriendProfile);
        txtSex = findViewById(R.id.tvSexFriendProfile);
        imgAvt = findViewById(R.id.getAvatarFriendProfile);
        btnBlock = findViewById(R.id.btnBlockFriend);
        btnHuy = findViewById(R.id.btnDelFriend);
        imgBackFriendProfile = findViewById(R.id.imgBackFriendProfile);
        txtDob.setText(frDob);
        txtName.setText(frName);
        txtSex.setText(frSex);
        if(frAvater.equalsIgnoreCase(BuildConfig.API+"file/avatar/smile.png"))
            imgAvt.setImageResource(R.drawable.smile);
        else{
            Glide.with(getApplicationContext())
                    .load(frAvater)
                    .into(imgAvt);}
        btnHuy.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                deleteFriend(profileId,frID);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(FriendProfile.this);
                builder.setMessage("Xác nhận hủy kết bạn ").setPositiveButton("Đồng ý ", dialogClickListener)
                        .setNegativeButton("Không", dialogClickListener).show();


            }
        });
        btnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ahihi","ahihi");
                if(btnBlock.getText().toString().equals("Chặn"))
                    blockFriend(profileId,frID);
                else
                    delblockFriend(profileId,frID);

            }
        });
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
    private void blockFriend(String profile_id,String frId ) {
        String url =BuildConfig.API+"account/friend/block/"+profile_id;
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("blockId",frId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONObject respObj = new JSONObject(String.valueOf(response));
                            String data= respObj.getString("data");
                            Log.d("chawnj",data);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }

    private void delblockFriend(String profile_id,String frID ) {
        String url =BuildConfig.API+"account/friend/block/"+profile_id+"?blockId="+frID;
        JSONObject object = new JSONObject();
        // Enter the correct url for your api service site
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONObject respObj = new JSONObject(String.valueOf(response));
                            String data= respObj.getString("data");
                            Log.d("chawnj",data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Log.d("erorr","loi");
            }
        }){
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("authorization",token );
                return headers;
            }
        };
        Log.d("frId",profile_id);
        Log.d("put",object.toString());
        Log.d("",token);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }
    private void deleteFriend(String profileId,String frID)  {
        String url =BuildConfig.API+"account/friend/"+profileId+"?friendId="+frID;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));
                            Intent intent = new Intent(FriendProfile.this, PhoneBook.class);

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