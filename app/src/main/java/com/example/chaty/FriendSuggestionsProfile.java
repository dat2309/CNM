package com.example.chaty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class FriendSuggestionsProfile extends AppCompatActivity {

    TextView txtName,txtSex,txtDob;
    ImageView imgBackFriendSuggestion, imgAvt;
    String token,profileId,phone,email,avatar,phone2,frID;
    EditText description;
    Button btnAdd,btnBlock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_suggestions_profile);
        token= getIntent().getStringExtra("token");
        profileId = getIntent().getStringExtra("profileId");
        email= getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        phone2 = getIntent().getStringExtra("receiver");
        description = findViewById(R.id.edtDescription);
        txtDob = findViewById(R.id.tvDateFriendSuggestionsProfile);
        txtName = findViewById(R.id.tvNameFriendSuggestionsProfile);
        txtSex = findViewById(R.id.tvSexFriendSuggestionsProfile);
        imgAvt = findViewById(R.id.getAvatarFriendSuggestionsProfile);
        btnAdd = findViewById(R.id.btnAddFriend);
        btnBlock = findViewById(R.id.btnBlockFriendSugges);
        imgBackFriendSuggestion = findViewById(R.id.imgBackFriendSuggestionsProfile);
        try {
            JSONObject respObj2 = new JSONObject(getIntent().getStringExtra("respObj2"));
            txtName.setText(respObj2.get("name").toString());
            txtDob.setText(respObj2.get("dob").toString());
            if(respObj2.get("sex").equals(true)){
                txtSex.setText("Nam");
            }
            else{
                txtSex.setText("Nữ");
            }
            avatar = respObj2.get("avatar").toString();
            frID=respObj2.get("_id").toString();
            if(avatar.equalsIgnoreCase(BuildConfig.API+"file/avatar/smile.png"))
                imgAvt.setImageResource(R.drawable.smile);
            else{
                Glide.with(getApplicationContext())
                        .load(respObj2.get("avatar"))
                        .into(imgAvt);}

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendRequestAdd(phone,phone2,description.getText().toString());
                }
            });
            btnBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(btnBlock.getText().toString().equals("Chặn"))
                        blockFriend(profileId,frID);
                    else
                        delblockFriend(profileId,frID);
                }
            });
            imgBackFriendSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendSuggestionsProfile.this, FriendHome.class);
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();
            }
        });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void sendRequestAdd(String sender,String receiver,String description) {
        String url =BuildConfig.API+"request";
        if(description.length()==0)
            description = "Xin chào!";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("sender", sender);
            object.put("receiver",receiver);
            object.put("description",description);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        Intent intent = new Intent(FriendSuggestionsProfile.this, FriendHome.class);
                                        intent.putExtra("token",token);
                                        intent.putExtra("profileId",profileId);
                                        intent.putExtra("email",email);
                                        intent.putExtra("phone",phone);
                                        startActivity(intent);
                                        finish();
                                        break;

                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(FriendSuggestionsProfile.this);
                        builder.setMessage("Gửi lời mời kết bạn thành công").setPositiveButton("oke ", dialogClickListener)
                                .show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }
}