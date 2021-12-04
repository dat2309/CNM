package com.example.chaty;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassForgot extends AppCompatActivity {
    EditText edtPass1,edtPass2,edtCode;
    Button btnXacNhan;
    ImageView imgBack;
    String email,phone;
    CheckBox ckShow;
    private AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_forgot);
        imgBack = findViewById(R.id.imgBackMKForgot);
        edtPass1 = findViewById(R.id.edtMKForgot);
        edtPass2 = findViewById(R.id.edtMKForgot2);
        edtCode = findViewById(R.id.edtMKCode);
        btnXacNhan = findViewById(R.id.btnXacNhanTaoMK);
        ckShow = findViewById(R.id.checkBox3);
        email= getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(ChangePassForgot.this,R.id.edtMKForgot,"^[A-Za-z0-9]{6,}$",R.string.invalid_password);
        awesomeValidation.addValidation(this, R.id.edtMKForgot2, R.id.edtMKForgot, R.string.invalid_confirm_password);
        awesomeValidation.addValidation(this, R.id.edtMKCode, "[0-9]{6}$", R.string.invalid_code);
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
                Intent intent = new Intent(ChangePassForgot.this, ForgotPass.class);
                startActivity(intent);
            }
        });
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()) {
                    changePassForgot(phone,email,edtCode.getText().toString(),edtPass1.getText().toString());
            }}
        });
    }
    private void changePassForgot(String phone,String email , String key,String newPass) {
        String url =BuildConfig.API+"site/recovery";
        Number status = 0;
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("email",email);
            object.put("phone",phone);
            object.put("resetPasswordKey",key);
            object.put("newPassword",newPass);
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
                            if(data.equals("Your reset code expired")){
                                edtCode.setError("Mã xác nhận không đúng");
                                edtCode.setText("");
                                edtCode.requestFocus();
                            }
                            else{
                                Toast.makeText(ChangePassForgot.this,"yêu cầu thành công",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ChangePassForgot.this, Login.class);
                                startActivity(intent);}
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }
}