package com.example.chaty;

import static com.example.chaty.Adapter.ItemChatAdapter.itemChats;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.chaty.Adapter.ItemChatAdapter;
import com.example.chaty.Adapter.ItemPhoneBookAdapter;
import com.example.chaty.Item.ItemChat;
import com.example.chaty.Item.ItemPhoneBook;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ImageView imgAvatar,imgLogOut,imgPhoneBook,imgFriendSuggestion,imgAdd,imgCreatePersonGroup;
    RecyclerView rcvItemChat;
    EditText edtSearch;
    ItemChatAdapter itemChatAdapter;
    public static String namePr;
    public static List<String> mobileArray =new ArrayList<>();
    public static final int REQUEST_READ_CONTACTS = 79;
    TextView txtName;
    String token,profileId,phone,email;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            mobileArray = getAllContacts();

            for(int i = 0 ; i < mobileArray.size()-1; i++)
                for( int j = i+1 ; j< mobileArray.size()-1;j++ )
                    if(mobileArray.get(i).equals(mobileArray.get(j)))
                    {
                        mobileArray.remove(j);

                    }
        } else {
            requestPermission();
        }
        imgAvatar =  findViewById(R.id.imgAvatar);
        imgLogOut = findViewById(R.id.imgLogout);
        txtName = findViewById(R.id.txtName);
        rcvItemChat = findViewById(R.id.rcvItemChat);
        imgPhoneBook = findViewById(R.id.imgPhoneBook);
        imgFriendSuggestion = findViewById(R.id.imgFriendSuggestion);
        imgCreatePersonGroup = findViewById(R.id.imgCreatePersonGroup);
        imgAdd = findViewById(R.id.imgAddPerson);
        edtSearch = findViewById(R.id.edtSearch);

        //nhận dữ liệu
        token= getIntent().getStringExtra("token");
        profileId = getIntent().getStringExtra("profileId");
        email= getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");



        //call api
        getProfile(profileId);


        initView();

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FriendHome.class);
                //gửi dữ liệu
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0)
                    initView();
                else
                    filter(s.toString());
            }
        });
        imgCreatePersonGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateGroupChat.class);
                //gửi dữ liệu
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                intent.putExtra("name",namePr);
                startActivity(intent);
                finish();
            }
        });
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Profile.class);
                //gửi dữ liệu
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();
            }
        });
        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Profile.class);
                startActivity(intent);
                finish();
            }
        });
        imgLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                mobileArray.clear();
                                Intent intent = new Intent(MainActivity.this, Login.class);
                                startActivity(intent);
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Xác nhận đăng xuất ").setPositiveButton("Đồng ý ", dialogClickListener)
                        .setNegativeButton("Không", dialogClickListener).show();

            }
        });
        imgPhoneBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhoneBook.class);
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();
                ;
            }
        });
        imgFriendSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FriendHome.class);
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
                            JSONObject respObj2 = new JSONObject(respObj.getString("data"));
//                            imgAvatar.setImageResource(Integer.parseInt(respObj2.get("avatar").toString()));
                            txtName.setText(respObj2.get("name").toString());
                            namePr= respObj2.get("name").toString();
                            String avatar = respObj2.get("avatar").toString();
                            //load ảnh từ db
                            if(avatar.equalsIgnoreCase(BuildConfig.API+"file/avatar/smile.png"))
                                imgAvatar.setImageResource(R.drawable.smile);
                            else{

                                Glide.with(getApplicationContext())
                                        .load(avatar)
                                        .into( imgAvatar);}




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
            //token
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("authorization",token );
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
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
                    for(int i = 0 ; i < mobileArray.size(); i++)
                        for( int j = i+1 ; j< mobileArray.size()-1;j++ )
                            if(mobileArray.get(i).equals(mobileArray.get(j)))
                                mobileArray.remove(j);
                } else {
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
                        Boolean kq = false;
                        for(int i = 0 ; i <nameList.size(); i++)
                            if(nameList.get(i).equals(phoneNo))
                                kq = true;
                        if(!kq)
                            nameList.add(phoneNo);

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
    public void filter(String text) {
        List<ItemChat> filteredList = new ArrayList<>();
        for (ItemChat item : itemChats) {
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        itemChatAdapter.filterList(filteredList);
        itemChatAdapter.notifyDataSetChanged();

    }
    public void initView() {
        itemChatAdapter = new ItemChatAdapter(MainActivity.this,profileId,token,email,phone);
        rcvItemChat.setAdapter(itemChatAdapter);
        rcvItemChat.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
    }
}