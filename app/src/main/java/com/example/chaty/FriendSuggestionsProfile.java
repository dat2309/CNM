package com.example.chaty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    String token,profileId,phone,email,avatar,phone2;
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
        txtDob = findViewById(R.id.tvDateFriendSuggestionsProfile);
        txtName = findViewById(R.id.tvNameFriendSuggestionsProfile);
        txtSex = findViewById(R.id.tvSexFriendSuggestionsProfile);
        imgAvt = findViewById(R.id.getAvatarFriendSuggestionsProfile);
        btnAdd = findViewById(R.id.btnAddFriend);
        btnBlock = findViewById(R.id.btnBlockFriend);
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
            if(avatar.equalsIgnoreCase("http://chaty-api.herokuapp.com/file/avatar/smile.png"))
                imgAvt.setImageResource(R.drawable.smile);
            else{
                Glide.with(getApplicationContext())
                        .load(respObj2.get("avatar"))
                        .into(imgAvt);}

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendRequestAdd(phone,phone2);
                }
            });
            imgBackFriendSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendSuggestionsProfile.this, MakeFriend.class);
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
    private void sendRequestAdd(String sender,String receiver ) {
        String url ="https://chaty-api.herokuapp.com/request";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("sender", sender);
            object.put("receiver",receiver);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       Log.d("data",response.toString());
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        Intent intent = new Intent(FriendSuggestionsProfile.this, MakeFriend.class);
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

}