package com.example.chaty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    Button btnUpdateProfile,btnChangePass;
    TextView txtName,txtDob,txtSex,txtPhone,txtEmail;
    ImageView imgReturnProfile,imgAvatar;
    String token,profileId,phone,email,avatar,name,sex,dob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txtName = findViewById(R.id.tvNameProfile);
        txtDob = findViewById(R.id.tvDateProfile);
        txtSex = findViewById(R.id.tvSexProfile);
        txtEmail = findViewById(R.id.tvEmailProfile);
        txtPhone = findViewById(R.id.tvPhoneNumberProfile);
        btnChangePass = findViewById(R.id.btnChangePass);
        btnUpdateProfile= findViewById(R.id.btnUpdateProfile);
        imgReturnProfile =findViewById(R.id.imgReturnProfile);
        imgAvatar = findViewById(R.id.imgAvatarProfile);
        //nhận data
        token= getIntent().getStringExtra("token");
        profileId = getIntent().getStringExtra("profileId");
        email= getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");

        getProfile(profileId);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, UpdateProfile.class);
                // gửi data
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("name",name);
                intent.putExtra("sex",sex);
                intent.putExtra("avatar",avatar);
                intent.putExtra("dob",dob);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();
            }
        });
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, ChangePassword.class);
                intent.putExtra("avatar",avatar);
                intent.putExtra("name",name);
                intent.putExtra("token",token);
                intent.putExtra("email",email);
                intent.putExtra("profileId",profileId);
                intent.putExtra("phone",phone);
                startActivity(intent);

                finish();
            }
        });
        imgReturnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, MainActivity.class);
                // gửi data
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
                            name= respObj2.get("name").toString();
                            txtName.setText(name);
                            txtEmail.setText(email);
                            txtPhone.setText(phone);
                            dob=respObj2.get("dob").toString();
                            Log.d("dob", dob);
                            txtDob.setText(dob);
                            if(respObj2.get("sex").equals(true)){
                                txtSex.setText("Nam");
                                sex="Nam";
                            }
                            else{
                                txtSex.setText("Nữ");
                                sex="Nữ";}
                                avatar = respObj2.get("avatar").toString();
                            if(avatar.equalsIgnoreCase(BuildConfig.API+"file/avatar/smile.png"))
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