package com.example.chaty;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class VetifyOTP extends AppCompatActivity {
    Button btnOTP ;
    TextView txtWrongOTP, txtSendOTP;
    EditText edtOTP;
    private AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vetify_otp);
        btnOTP = findViewById(R.id.btnXacNhan);
        txtWrongOTP = findViewById(R.id.txtWrongOTP);
        edtOTP = findViewById(R.id.edtGetOTP);
        txtSendOTP = findViewById(R.id.txtSendOTP);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.edtGetOTP, "[0-9]{6}$", R.string.invalid_code);
        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()){
                try {
                    JSONObject respObj2 = new JSONObject(getIntent().getStringExtra("respObj2"));
                    try {
                        postOTP(respObj2.get("_id"),edtOTP.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }}
        });

        edtOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtWrongOTP.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                txtWrongOTP.setText("");
            }
        });
        txtSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    JSONObject respObj2 = new JSONObject(getIntent().getStringExtra("respObj2"));
                    try {
                        postIDaccount(respObj2.get("_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



    }
    private void postOTP(Object _id , String otp) {
        String url2 =BuildConfig.API+"site/auth";
        JSONObject object = new JSONObject();
        try {
            object.put("_id", _id);
            object.put("key", otp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Enter the correct url for your api service site
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url2, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String data =new String( response.getString("data"));
                            if(data.equalsIgnoreCase("Failed"))
                                txtWrongOTP.setText("Vui lòng nhập lại OTP");
                            else{
                                Toast.makeText(VetifyOTP.this,"Kích hoạt thành công",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(VetifyOTP.this, Login.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(VetifyOTP.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(VetifyOTP.this,"send",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(VetifyOTP.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }
}