package com.example.chaty;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.bumptech.glide.Glide;


import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class UpdateProfile extends AppCompatActivity {
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    ImageView back,avt;
    Button btnSaveProfile,huy;
    String token,profileId,avatar,name,sex,dob,email,phone;
    EditText edtName;
    TextView editDOB;
    Boolean sex2;
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST =1 ;
    private Bitmap bitmap;
    private String filePath;
    private int mYear, mMonth, mDay;
    private RadioButton radioNu,radioNam;
    private AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        btnSaveProfile= findViewById(R.id.btnSaveProfile);
        back =findViewById(R.id.imgReturnUpdateProfile);
        huy = findViewById(R.id.btnCancelProfileUpdate);
        token= getIntent().getStringExtra("token");
        profileId = getIntent().getStringExtra("profileId");
        avatar= getIntent().getStringExtra("avatar");
        name = getIntent().getStringExtra("name");
        sex = getIntent().getStringExtra("sex");
        dob = getIntent().getStringExtra("dob");
        email= getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        avt = findViewById(R.id.imgAvatarProfile);
        if(avatar.equalsIgnoreCase(BuildConfig.API+"file/avatar/smile.png"))
            avt.setImageResource(R.drawable.smile);
        else{
            Glide.with(getApplicationContext())
                    .load(avatar)
                    .into(avt);}
        edtName = findViewById(R.id.edtHoTenUpdate);
        edtName.setText(name);
        editDOB = findViewById(R.id.edtUpdateDOB);
        editDOB.setText(String.valueOf(dob));
        radioNu = findViewById(R.id.radioNU);
        radioNam = findViewById(R.id.radioNam);
        if(sex.equals("Nam"))
            radioNam.setChecked(true);

        else
            radioNu.setChecked(true);
        avt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(UpdateProfile.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(UpdateProfile.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {

                    } else {
                        ActivityCompat.requestPermissions(UpdateProfile.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    Log.e("Else", "Else");
                    showFileChooser();
                }
            }
        });
        editDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateProfile.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                editDOB.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProfile.this, Profile.class);
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();
            }
        });
        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProfile.this, Profile.class);
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();
            }
        });
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.edtHoTenUpdate, RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    radioSexGroup = (RadioGroup) findViewById(R.id.radioGroup2);
                    int selectedId = radioSexGroup.getCheckedRadioButtonId();
                    radioSexButton = (RadioButton) findViewById(selectedId);
                    if(radioSexButton.getText().toString().equals("Nam")) {
                        sex2 = true;
                    }
                    else{
                        sex2 = false;
                    }
                    if(awesomeValidation.validate())
                        uploadBitmap(bitmap,profileId,edtName.getText().toString(),sex2, editDOB.getText().toString(),token,email,phone);

            }
        });
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
                    avt.setImageBitmap(bitmap);
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
        bitmap.compress(Bitmap.CompressFormat.JPEG,30 , byteArrayOutputStream);
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
    private void uploadBitmap(final Bitmap bitmap, String profileId, String name, boolean sex, String dob , String token,String email,String phone) {
        String url =BuildConfig.API+"profile/"+profileId;
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.PUT, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        Toast.makeText(UpdateProfile.this,"Cập nhật thông tin thành công",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(UpdateProfile.this, Profile.class);
                        intent.putExtra("token",token);
                        intent.putExtra("profileId",profileId);
                        intent.putExtra("email",email);
                        intent.putExtra("phone",phone);
                        startActivity(intent);
                        finish();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name",name);
                params.put("sex",String.valueOf(sex));
                params.put("dob",dob);
                return params;

            }
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
                if(bitmap != null ) {
                    params.put("avatar", new DataPart( "avatar"+".jpeg", getFileDataFromDrawable(bitmap),"image/jpeg"));
                         }
                else
                    params.put("avatar", new DataPart("avatar"+".jpeg",getFileDataFromDrawable2(UpdateProfile.this,avt.getDrawable()), "image/jpeg"));

                return params;
            }

            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("authorization",token );
                return headers;
            }
        };

        //adding the request to volley

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

}