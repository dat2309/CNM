package com.example.chaty;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    Button btnUpdateProfile, btnChangePass;
    TextView txtName, txtDob, txtSex, txtPhone, txtEmail, txtXTK;
    ImageView imgReturnProfile, imgAvatar;
    String token, profileId, phone, email, avatar, name, sex, dob;
    private Button btnDel;

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
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        imgReturnProfile = findViewById(R.id.imgReturnProfile);
        imgAvatar = findViewById(R.id.imgAvatarProfile);
        txtXTK = findViewById(R.id.txtXTK);
        //nhận data
        token = getIntent().getStringExtra("token");
        profileId = getIntent().getStringExtra("profileId");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");

        getProfile(profileId);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, UpdateProfile.class);
                // gửi data
                intent.putExtra("token", token);
                intent.putExtra("profileId", profileId);
                intent.putExtra("name", name);
                intent.putExtra("sex", sex);
                intent.putExtra("avatar", avatar);
                intent.putExtra("dob", dob);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                startActivity(intent);
                finish();
            }
        });
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, ChangePassword.class);
                intent.putExtra("avatar", avatar);
                intent.putExtra("name", name);
                intent.putExtra("token", token);
                intent.putExtra("email", email);
                intent.putExtra("profileId", profileId);
                intent.putExtra("phone", phone);
                startActivity(intent);

                finish();
            }
        });
        imgReturnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, MainActivity.class);
                // gửi data
                intent.putExtra("token", token);
                intent.putExtra("profileId", profileId);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                startActivity(intent);
                finish();
            }
        });
        txtXTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteDialog(Gravity.CENTER);

            }
        });


    }

    private void openDeleteDialog(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_delete_account);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(false);
        EditText edtMK = dialog.findViewById(R.id.edtMKDelete);

        Button btnCancle = dialog.findViewById(R.id.btnHuyDelete);
        Button btnDel = dialog.findViewById(R.id.btnDeleteAc);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtMK.getText().length() == 0)
                    edtMK.setError("vui lòng nhập mật khẩu");
                else
                {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    deleteAccount(profileId, edtMK.getText().toString(),edtMK);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(dialog.getContext());
                    builder.setMessage("Xác nhận xóa tài khoản ").setPositiveButton("Đồng ý ", dialogClickListener)
                            .setNegativeButton("Không", dialogClickListener).show();
                   }
            }
        });
        dialog.show();
    }

    private void getProfile(String profileID) {
        String url = BuildConfig.API + "profile/" + profileID;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONObject respObj = new JSONObject(response);
                            JSONObject respObj2 = new JSONObject(respObj.getString("data"));
                            name = respObj2.get("name").toString();
                            txtName.setText(name);
                            txtEmail.setText(email);
                            txtPhone.setText(phone);
                            dob = respObj2.get("dob").toString();
                            txtDob.setText(dob);
                            if (respObj2.get("sex").equals(true)) {
                                txtSex.setText("Nam");
                                sex = "Nam";
                            } else {
                                txtSex.setText("Nữ");
                                sex = "Nữ";
                            }
                            avatar = respObj2.get("avatar").toString();
                            if (avatar.equalsIgnoreCase(BuildConfig.API + "file/avatar/smile.png"))
                                imgAvatar.setImageResource(R.drawable.smile);
                            else {
                                Glide.with(getApplicationContext())
                                        .load(respObj2.get("avatar"))
                                        .into(imgAvatar);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("authorization", token);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private void deleteAccount(String profileId, String pass, EditText  edtMK) {
        String url = BuildConfig.API + "account/" + profileId + "?password=" + pass;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));
                            String data = new String(respObj.getString("data"));
                            if(data.equalsIgnoreCase("Wrong password")){
                                edtMK.setError("Mật khẩu sai, vui lòng nhập mật khẩu");
                                Toast.makeText(Profile.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(Profile.this, "Xóa tài khoản thành công ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Profile.this, Login.class);
                            startActivity(intent);
                            finish();}


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("authorization", token);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }
}