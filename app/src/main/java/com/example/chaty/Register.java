package com.example.chaty;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class Register extends AppCompatActivity {
    EditText edtPhoneNumber,edtRePass,edtPass,edtEmail;
    TextView txtCoTK;
    private AwesomeValidation awesomeValidation;
    private Button btnRegister;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumberLogin);
        edtPass = findViewById(R.id.edtPassLogin);
        edtRePass = findViewById(R.id.edtRepass);
        btnRegister = findViewById(R.id.btnDangKy);
        txtCoTK = findViewById(R.id.txtCoTK);
        edtEmail = findViewById(R.id.edtEmail);
        checkBox = findViewById(R.id.checkBoxRegis);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.edtPhoneNumberLogin, "^[0-9]{10}$", R.string.invalid_phone);
        awesomeValidation.addValidation(this, R.id.edtEmail, Patterns.EMAIL_ADDRESS, R.string.invalid_email);
        awesomeValidation.addValidation(this,R.id.edtPassLogin,"^[A-Za-z0-9]{6,}$",R.string.invalid_password);
        awesomeValidation.addValidation(this, R.id.edtRepass, R.id.edtPassLogin, R.string.invalid_confirm_password);
        txtCoTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()) {
                    postDataUsingVolley(edtPhoneNumber.getText().toString(),edtEmail.getText().toString(),edtPass.getText().toString());


                }
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edtRePass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else{
                    edtPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edtRePass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }
    private void postDataUsingVolley(String phone,String email ,String password ) {
        String url =BuildConfig.API+"site/signup";
        Number status = 0;
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("phone",phone);
            object.put("email",email);
            object.put("password",password);
//            object.put("status",status);

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

                            if(data.equals("Account was exists")){
                                Toast.makeText(Register.this,"Account đã tồn tại",Toast.LENGTH_LONG).show();
                                edtPhoneNumber.setText("");
                                edtPhoneNumber.setError("Số điện thoại đã được đăng ký ");
                                edtPhoneNumber.requestFocus();
        }
                      else if(data.equalsIgnoreCase("Email was exists")){
                                Toast.makeText(Register.this,"Email khoản đã tồn tại",Toast.LENGTH_LONG).show();
                                edtEmail.setText("");
                                edtPhoneNumber.setError("Email đã được đăng ký");
                                edtEmail.requestFocus();
                            }
                            else{
                                JSONObject respObj2 = new JSONObject(respObj.get("data").toString());

                                postIDaccount(respObj2.get("_id"));
                                Toast.makeText(Register.this,"Đăng ký thành công",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Register.this, VetifyOTP.class);
                                intent.putExtra("respObj2",respObj2.toString());
                                intent.putExtra("email",email);
                                intent.putExtra("phone",phone);
                                startActivity(intent);}
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Register.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }

    private void postIDaccount(Object _id ) {
        String url2 =BuildConfig.API+"site/active";
        JSONObject object = new JSONObject();
        try {
            object.put("_id", _id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url2, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Register.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }


}