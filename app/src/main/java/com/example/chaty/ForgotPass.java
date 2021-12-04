package com.example.chaty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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

public class ForgotPass extends AppCompatActivity {
    EditText edtPhone,edtEmail;
    Button btnXacNhan;
    ImageView imgBack;
    private AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        edtPhone = findViewById(R.id.edtForgorPhone);
        edtEmail = findViewById(R.id.edtForgotEmail);
        btnXacNhan = findViewById(R.id.btnXacNhanForgot);
        imgBack = findViewById(R.id.imgBackForgot);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.edtForgorPhone, "^[0-9]{10}$", R.string.invalid_phone);
        awesomeValidation.addValidation(this, R.id.edtForgotEmail, Patterns.EMAIL_ADDRESS, R.string.invalid_email);
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()) {
                getKeyForgotPass(edtPhone.getText().toString(),edtEmail.getText().toString());}
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPass.this, Login.class);
                startActivity(intent);
            }
        });

    }
    private void getKeyForgotPass(String phone,String email  ) {
        String url =BuildConfig.API+"site/forgot";
        Number status = 0;
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("phone",phone);
            object.put("email",email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Toast.makeText(ForgotPass.this,"yêu cầu thành công",Toast.LENGTH_LONG).show();
                            JSONObject respObj = new JSONObject(String.valueOf(response));
                            String data= respObj.getString("data");

                            if(data.equals("your reset password code has sent")){
                                Intent intent = new Intent(ForgotPass.this, ChangePassForgot.class);
                                // gửi dữ liệu qua acti
                                intent.putExtra("email", edtEmail.getText().toString());
                                intent.putExtra("phone", edtPhone.getText().toString());
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