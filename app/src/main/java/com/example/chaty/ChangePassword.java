package com.example.chaty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {
    String avatar,email,profileId,phone,token,name;
    EditText edtPass1,edtPass2;
    ImageView imgAvt,imgBack;
    TextView txtName;
    Button btnSave,btnCancle;
    CheckBox ckShow;
    private AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        avatar= getIntent().getStringExtra("avatar");
        token= getIntent().getStringExtra("token");
        profileId = getIntent().getStringExtra("profileId");
        email= getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        name = getIntent().getStringExtra("name");
        txtName = findViewById(R.id.txtNameAvtChangePass);
        ckShow = findViewById(R.id.checkBox2);
        txtName.setText(name);
        imgAvt = findViewById(R.id.imgAvatarProfileChangePass);
        if(avatar.equalsIgnoreCase(BuildConfig.API+"file/avatar/smile.png"))
            imgAvt.setImageResource(R.drawable.smile);
        else{
            Glide.with(getApplicationContext())
                    .load(avatar)
                    .into(imgAvt);}
        edtPass1 = findViewById(R.id.edtNewPassChange);
        edtPass2 = findViewById(R.id.edtRenewPassChange);
        btnCancle = findViewById(R.id.btnCancelPasswordChange);
        btnSave = findViewById(R.id.btnSavePasswordChange);
        imgBack = findViewById(R.id.imgBackProfileChange);
        ckShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    edtPass1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edtPass2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else{
                    edtPass1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edtPass2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassword.this, Profile.class);
                //gửi dữ liệu
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassword.this, Profile.class);
                //gửi dữ liệu
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();
            }
        });
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(ChangePassword.this,R.id.edtNewPassChange,"^[A-Za-z0-9]{6,}$",R.string.invalid_password);
        awesomeValidation.addValidation(this, R.id.edtRenewPassChange, R.id.edtNewPassChange, R.string.invalid_confirm_password);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                        updatePass(profileId,edtPass1.getText().toString());
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    Toast.makeText(ChangePassword.this,"đổi mật khẩu không thành công",Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
                    builder.setMessage("Xác nhận đồng ý đổi mật khẩu ").setPositiveButton("Đồng ý ", dialogClickListener)
                            .setNegativeButton("Hủy ", dialogClickListener).show();

                }
            }
        });
    }
    private void updatePass(String profileId,String pass)  {
        String url =BuildConfig.API+"account/"+profileId;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();

        try {
            object.put("password",pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));
                            Toast.makeText(ChangePassword.this,"đổi mật khẩu thành công",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ChangePassword.this, Profile.class);
                            //gửi dữ liệu
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