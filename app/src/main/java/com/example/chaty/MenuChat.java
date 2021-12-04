package com.example.chaty;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuChat extends AppCompatActivity {

    ImageView imgBackMenuChat;
    TextView tvAddPerson, tvRemovePerson, txtName, txtLeave, txtChangeName, txtChangeAva,txtSeeMem,txtAdmin;
    String token, profileId, email, phone, frAvatar, frName, frID, size, idRoom, admin,memID;
    ImageView imgAvt;
    EditText edtName;
    Button btnPro,btnhuy;
    public static Bitmap bitmap;
    public static String RomID;
    private String filePath;
    List member = new ArrayList();
    Button btnChange;
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_chat);
        imgBackMenuChat = findViewById(R.id.imgBackMenuChat);
        tvAddPerson = findViewById(R.id.tvAddPerson);
        tvRemovePerson = findViewById(R.id.tvRemovePerson);
        txtLeave = findViewById(R.id.tvLeaveGroup);
        imgAvt = findViewById(R.id.getAvatarMenuChat);
        txtName = findViewById(R.id.txtNameMenuChat);
        txtChangeName = findViewById(R.id.tvChangeName);
        txtChangeAva = findViewById(R.id.tvChangeAvatar);
        edtName = findViewById(R.id.editNameGroup);
        txtSeeMem = findViewById(R.id.txtSeeMem);
        btnPro = findViewById(R.id.btnSeePro);
        txtAdmin = findViewById(R.id.txtAdmin);
        btnhuy = findViewById(R.id.btnhuyU);
        try {
            JSONArray participant = new JSONArray(getIntent().getStringExtra("member"));

            token = getIntent().getStringExtra("token");
            profileId = getIntent().getStringExtra("profileId");
            email = getIntent().getStringExtra("email");
            phone = getIntent().getStringExtra("phone");
            frAvatar = getIntent().getStringExtra("frAvatar");
            frName = getIntent().getStringExtra("frName");
            admin = getIntent().getStringExtra("admin");
            size = getIntent().getStringExtra("size");

            if (!profileId.equals(admin)){
                tvRemovePerson.setVisibility(View.INVISIBLE);
                txtAdmin.setVisibility(View.INVISIBLE);
            }
            btnChange = findViewById(R.id.btnChangeMenu);
            if (size.equals("2")) {
                frID = getIntent().getStringExtra("frID");
                tvRemovePerson.setVisibility(View.INVISIBLE);
                tvAddPerson.setVisibility(View.INVISIBLE);
                txtChangeName.setVisibility(View.INVISIBLE);
                txtChangeAva.setVisibility(View.INVISIBLE);
                txtSeeMem.setVisibility(View.INVISIBLE);
                txtAdmin.setVisibility(View.INVISIBLE);
                btnPro.setVisibility(View.VISIBLE);
                txtLeave.setText("Xóa cuộc trò chuyện ");

                for( int i =0; i<participant.length();i++){
                    if(!profileId.equals(participant.get(i).toString()))
                        memID = participant.get(i).toString();}
            } else {
                idRoom = getIntent().getStringExtra("frID");
            }
            txtName.setText(frName);
            edtName.setText(frName);
            if (bitmap != null && RomID != null && RomID.equals(getIntent().getStringExtra("frID")))
                imgAvt.setImageBitmap(bitmap);

            else if (frAvatar.equalsIgnoreCase(BuildConfig.API + "file/avatar/smile.png"))
                imgAvt.setImageResource(R.drawable.smile);
            else {
                Glide.with(this)
                        .load(frAvatar)
                        .into(imgAvt);
            }
            RomID = new String("");
            imgBackMenuChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MenuChat.this, Chat.class);
                    if (size.equals("2"))
                        intent.putExtra("frID", frID);
                    else
                        intent.putExtra("frID", idRoom);
                    intent.putExtra("frAvatar", frAvatar);
                    intent.putExtra("frName", frName);
                    intent.putExtra("participant", participant.toString());
                    intent.putExtra("token", token);
                    intent.putExtra("profileId", profileId);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("size", size);
                    intent.putExtra("admin", admin);
                    startActivity(intent);
                    finish();

                }
            });
            btnPro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   getProfile(memID);
                }
            });
            txtChangeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    txtName.setVisibility(View.INVISIBLE);
                    edtName.setVisibility(View.VISIBLE);
                    edtName.requestFocus();
                    btnChange.setVisibility(View.VISIBLE);
                    btnhuy.setVisibility(View.VISIBLE);
                    btnChange.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateConName(edtName.getText().toString(), idRoom);
                            txtName.setText(edtName.getText().toString());
                            txtName.setVisibility(View.VISIBLE);
                            edtName.setVisibility(View.INVISIBLE);
                            btnChange.setVisibility(View.INVISIBLE);
                            btnhuy.setVisibility(View.INVISIBLE);
                            frName = edtName.getText().toString();
                            hideKeyboard(v);

                        }
                    });
                    btnhuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            txtName.setText(frName);
                            txtName.setVisibility(View.VISIBLE);
                            edtName.setVisibility(View.INVISIBLE);
                            btnChange.setVisibility(View.INVISIBLE);
                            btnhuy.setVisibility(View.INVISIBLE);
                        }
                    });

                }
            });

            txtChangeAva.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                        if ((ActivityCompat.shouldShowRequestPermissionRationale(MenuChat.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(MenuChat.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE))) {

                        } else {
                            ActivityCompat.requestPermissions(MenuChat.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_PERMISSIONS);
                        }
                    } else {
                        Log.e("Else", "Else");
                        showFileChooser();
                    }
                    btnChange.setVisibility(View.VISIBLE);
                    btnhuy.setVisibility(View.VISIBLE);

                    btnChange.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateConAva(bitmap, idRoom);
                            RomID += idRoom;

                            btnChange.setVisibility(View.INVISIBLE);
                            btnhuy.setVisibility(View.INVISIBLE);
                        }
                    });
                    btnhuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            btnChange.setVisibility(View.INVISIBLE);
                            btnhuy.setVisibility(View.INVISIBLE);
                            if (bitmap != null && RomID != null && RomID.equals(getIntent().getStringExtra("frID")))
                                imgAvt.setImageBitmap(bitmap);

                            else if (frAvatar.equalsIgnoreCase(BuildConfig.API + "file/avatar/smile.png"))
                                imgAvt.setImageResource(R.drawable.smile);
                            else {
                                Glide.with(v)
                                        .load(frAvatar)
                                        .into(imgAvt);
                            }
                        }
                    });

                }
            });
            txtSeeMem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MenuChat.this, MemberGroup.class);
                    intent.putExtra("frID", idRoom);
                    intent.putExtra("frAvatar", frAvatar);
                    intent.putExtra("frName", frName);
                    intent.putExtra("member", participant.toString());
                    intent.putExtra("token", token);
                    intent.putExtra("profileId", profileId);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("size", size);
                    intent.putExtra("admin", admin);
                    startActivity(intent);
                    finish();
                }
            });
            tvAddPerson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MenuChat.this, AddToGroup.class);
                    intent.putExtra("frID", idRoom);
                    intent.putExtra("frAvatar", frAvatar);
                    intent.putExtra("frName", frName);
                    intent.putExtra("member", participant.toString());
                    intent.putExtra("token", token);
                    intent.putExtra("profileId", profileId);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("size", size);
                    intent.putExtra("admin", admin);
                    startActivity(intent);
                    finish();
                }
            });
            txtAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MenuChat.this, ChangeAdmin.class);
                    intent.putExtra("frID", idRoom);
                    intent.putExtra("frAvatar", frAvatar);
                    intent.putExtra("frName", frName);
                    intent.putExtra("member", participant.toString());
                    intent.putExtra("token", token);
                    intent.putExtra("profileId", profileId);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("size", size);
                    intent.putExtra("admin", admin);
                    startActivity(intent);
                    finish();
                }
            });
            tvRemovePerson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MenuChat.this, RemoveFromGroup.class);
                    intent.putExtra("frID", idRoom);
                    intent.putExtra("frAvatar", frAvatar);
                    intent.putExtra("frName", frName);
                    intent.putExtra("member", participant.toString());
                    intent.putExtra("token", token);
                    intent.putExtra("profileId", profileId);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("size", size);
                    intent.putExtra("admin", admin);
                    startActivity(intent);
                    finish();
                }
            });

            txtLeave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!profileId.equals(admin)) {
                        deleteMemberFromGroup(profileId, getIntent().getStringExtra("frID"));
                        Intent intent = new Intent(MenuChat.this, MainActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("profileId", profileId);
                        intent.putExtra("email", email);
                        intent.putExtra("phone", phone);
                        startActivity(intent);
                        finish();
                    } else {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        break;


                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(MenuChat.this);
                        builder.setMessage("Bạn đang là chủ phòng cần đổi chủ phòng trước khi rời phòng ").setPositiveButton("Đồng ý ", dialogClickListener)
                                .show();

                    }

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/jpeg");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && null != data) {
            if (data.getData() != null) {

                final Uri uri = data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                    imgAvt.setImageBitmap(bitmap);
                    long imagename = System.currentTimeMillis();

                } catch (Exception e) {
                    Log.e("log", "File select error", e);
                }
            }
        }
    }

    // chuyển ảnh thành byte nén lại gửi
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();

    }

    public static byte[] getFileDataFromDrawable2(Context context, int id) {
        Drawable drawable = ContextCompat.getDrawable(context, id);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] getFileDataFromDrawable2(Context context, Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void updateConName(String name, String conID) {
        String url = BuildConfig.API + "conversation/" + conID;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject object = new JSONObject();

        try {
            object.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));


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

    private void updateConAva(final Bitmap bitmap, String conID) {
        String url = BuildConfig.API + "conversation/" + conID;
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.PUT, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject respObj = new JSONObject(json);
                            JSONObject respObj3 = new JSONObject(respObj.getString("data"));
                            frAvatar = respObj3.get("avatarRoom").toString();
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        Log.d("ERRor", "Error: " + error

                                + "nStatus Code " + error.networkResponse.statusCode

                                + "nCause " + error.getCause()

                                + "nmessage" + error.getMessage());
                    }
                }) {

            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                if (bitmap != null) {
                    params.put("avatar", new DataPart("avatar" + ".jpeg", getFileDataFromDrawable(bitmap), "image/jpeg"));
                } else
                    params.put("avatar", new DataPart("avatar" + ".jpeg", getFileDataFromDrawable2(MenuChat.this, imgAvt.getDrawable()), "image/jpeg"));

                return params;
            }

            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("authorization", token);
                return headers;
            }
        };

        //adding the request to volley

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    public void deleteMemberFromGroup(String deleteID, String conID) {
        JSONArray description = new JSONArray();

        String url = BuildConfig.API + "conversation/member/" + conID + "?accountId=" + deleteID;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("loi", "loicc");

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
                            JSONObject respObj2 = new JSONObject(respObj.getString("data"));
                            String sex;
                            if(respObj2.get("sex").equals(true)){
                                sex="Nam";
                            }
                            else{
                                sex="Nữ";}
                            Intent intent = new Intent(MenuChat.this,FriendProfile.class);
                            intent.putExtra("frAvatar",respObj2.get("avatar").toString());
                            intent.putExtra("frName",respObj2.get("name").toString());
                            intent.putExtra("frSex",sex);
                            intent.putExtra("frDob",respObj2.get("dob").toString());
                            intent.putExtra("frID",profileID);
                            intent.putExtra("token",token);
                            intent.putExtra("profileId",profileId);
                            intent.putExtra("email",email);
                            intent.putExtra("phone",phone);
                            startActivity(intent);



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
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}