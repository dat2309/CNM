package com.example.chaty;


import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.common.collect.Range;
public class Login extends AppCompatActivity {

    EditText edtPhoneNumberLogin, edtPassLogin;
    Button btnDangNhap;
    TextView txtTaoTK, txtWrong,txtForgot;
    private AwesomeValidation awesomeValidation;
    public static final int REQUEST_READ_CONTACTS = 79;
    public static List<String> mobileArray;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            mobileArray = getAllContacts();
        } else {
            requestPermission();
        }
        edtPhoneNumberLogin = findViewById(R.id.edtPhoneNumberLogin);
        edtPassLogin = findViewById(R.id.edtPassLogin);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        txtTaoTK = findViewById(R.id.textView5);
        txtWrong = findViewById(R.id.txtWrong);
        txtForgot = findViewById(R.id.txtForgotPass);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        //kiểm tra
        awesomeValidation.addValidation(this, R.id.edtPhoneNumberLogin, "^[0-9]{10}$", R.string.invalid_phone);
        awesomeValidation.addValidation(this,R.id.edtPassLogin,"^[A-Za-z0-9]{6,}$",R.string.invalid_password);

        txtTaoTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()){
                    //call api
                    postDataLogin(edtPhoneNumberLogin.getText().toString(), edtPassLogin.getText().toString());
                }}
        });
        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPass.class);
                startActivity(intent);
                finish();
            }
        });

    }
    public void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS},
                    REQUEST_READ_CONTACTS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mobileArray = getAllContacts();
                } else {
                    // permission denied,Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
    public List<String> getAllContacts() {
        List<String> nameList = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        nameList.add(phoneNo);
                        Log.d("ahihi",nameList.toString());
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
        return nameList;
    }
    private void postDataLogin(String phone, String password) {

        String url = BuildConfig.API+"site/signin";

        RequestQueue queue = Volley.newRequestQueue(Login.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject respObj = new JSONObject(response);
                    Log.d("json", response);
                    String data =new String( respObj.getString("data"));

                    if(data.equalsIgnoreCase("Phone or password was wrong"))
                        txtWrong.setText("Bạn nhập sai số điện thoại hoặc mật khẩu vui lòng nhập lại");
                    else{
                    JSONObject respObj3 = new JSONObject(respObj.getString("data"));
                    String status = new String(respObj3.getString("status"));
                     if(status.equalsIgnoreCase("0")){
                        Toast.makeText(Login.this, "Bạn chưa active vui lòng active", Toast.LENGTH_SHORT).show();
                        //call api
                        postIDaccount(respObj3.get("_id"));
                        Intent intent = new Intent(Login.this, VetifyOTP.class);
                         // gửi dữ liệu qua acti
                        intent.putExtra("respObj2",respObj3.toString());
                        startActivity(intent);
                    }
                    else {
                         txtWrong.setText("");
                         JSONObject respObj2 = new JSONObject(respObj.getString("data"));
                         Log.d("json", String.valueOf(respObj2));
//                        String token =new String( respObj2.getString("token"));
//                        Log.d("string", token);
                         String profileId = new String(respObj2.getString("profileId"));
                         Log.d("string", profileId);
                         Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                         if (profileId.equals("null")) {
                             Intent intent = new Intent(Login.this, CreateProfile.class);
                             // gửi dữ liệu qua acti
                             intent.putExtra("respObj2", respObj2.toString());
                             intent.putExtra("email", respObj2.getString("email"));
                             intent.putExtra("phone", respObj2.getString("phone"));
                             startActivity(intent);
                         } else {
                             String token = respObj2.get("token").toString();
                             Intent intent = new Intent(Login.this, MainActivity.class);
                             // gửi dữ liệu qua acti
                             intent.putExtra("respObj2", respObj2.toString());
                             intent.putExtra("token", token);
                             Log.d("token", token);
                             intent.putExtra("profileId", respObj2.get("profileId").toString());
                             intent.putExtra("email", respObj2.getString("email"));
                             intent.putExtra("phone", respObj2.getString("phone"));
                             startActivity(intent);
                         }

                     }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error.networkResponse.statusCode == 400)
                    txtWrong.setText("Bạn nhập sai số điện thoại hoặc mật khẩu vui lòng nhập lại");


            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("phone", phone);
                params.put("password", password);

                return params;
            }
        };

        queue.add(request);
    }
    private void postIDaccount(Object _id ) {
        String url2 =BuildConfig.API+"site/active";
        JSONObject object = new JSONObject();
        try {
            object.put("_id", _id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url2, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(Login.this,"send OTP",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Login.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

}
