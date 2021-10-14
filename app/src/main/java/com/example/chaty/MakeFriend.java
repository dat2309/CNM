package com.example.chaty;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MakeFriend extends AppCompatActivity {

    ImageView imgBackMakeFriend,imgAddFriend;
    RecyclerView rcvFriendRequest,rcvFriendSuggestion;
    ArrayList<ItemFriendRequest> itemFriendRequests;
    ItemFriendRequestAdapter itemFriendRequestAdapter;
    ArrayList<ItemFriendSuggestions> itemFriendSuggestions;
    ItemFriendSuggestionsAdapter itemFriendSuggestionsAdapter;
    String token,profileId,email;
    String phone;
    EditText edtFind;
    private AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_friend);
        imgBackMakeFriend = findViewById(R.id.imgBackMakeFriend);
        imgAddFriend = findViewById(R.id.imgAddFriend);
        rcvFriendRequest = findViewById(R.id.rcvFriendRequest);
        rcvFriendSuggestion = findViewById(R.id.rcvFriendSuggestions);
        edtFind = findViewById(R.id.edtSearchFriend);

        token= getIntent().getStringExtra("token");
        profileId = getIntent().getStringExtra("profileId");
        email= getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        Log.d("phone", phone);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.edtSearchFriend, "^[0-9]{10}$", R.string.invalid_phone);
        itemFriendRequests = new ArrayList<>();
        getReqSender(phone);



        itemFriendRequests.add(new ItemFriendRequest(R.drawable.brownduck, R.drawable.ic_info,"Ngô Quang Long"));
        itemFriendRequests.add(new ItemFriendRequest(R.drawable.spiderduck, R.drawable.ic_info,"Lê Tuấn Tú"));
        itemFriendRequests.add(new ItemFriendRequest(R.drawable.supermanduck, R.drawable.ic_info,"Nguyễn Thế Đạt"));
        itemFriendRequests.add(new ItemFriendRequest(R.drawable.pinkduck, R.drawable.ic_info,"Lê Tuấn Tú"));
        itemFriendRequests.add(new ItemFriendRequest(R.drawable.cuteduck, R.drawable.ic_info,"Ngô Quang Long"));

        itemFriendRequestAdapter = new ItemFriendRequestAdapter(itemFriendRequests,this);
        rcvFriendRequest.setAdapter(itemFriendRequestAdapter);
        rcvFriendRequest.setLayoutManager(new GridLayoutManager(MakeFriend.this,
                1));

        itemFriendSuggestions = new ArrayList<>();
        itemFriendSuggestions.add(new ItemFriendSuggestions(R.drawable.blueduck, R.drawable.ic_info,"Nguyễn Thế Đạt","0123456789"));
        itemFriendSuggestions.add(new ItemFriendSuggestions(R.drawable.brownduck, R.drawable.ic_info,"Ngô Quang Long","0111122222"));
        itemFriendSuggestions.add(new ItemFriendSuggestions(R.drawable.spiderduck, R.drawable.ic_info,"Lê Tuấn Tú","0987654321"));
        itemFriendSuggestions.add(new ItemFriendSuggestions(R.drawable.supermanduck, R.drawable.ic_info,"Nguyễn Thế Đạt","0123459876"));
        itemFriendSuggestions.add(new ItemFriendSuggestions(R.drawable.pinkduck, R.drawable.ic_info,"Lê Tuấn Tú","0123456798"));
        itemFriendSuggestions.add(new ItemFriendSuggestions(R.drawable.cuteduck, R.drawable.ic_info,"Ngô Quang Long","0135798642"));

        itemFriendSuggestionsAdapter = new ItemFriendSuggestionsAdapter(itemFriendSuggestions,this);
        rcvFriendSuggestion.setAdapter(itemFriendSuggestionsAdapter);
        rcvFriendSuggestion.setLayoutManager(new GridLayoutManager(MakeFriend.this,
                1));
        imgBackMakeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeFriend.this,MainActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();
            }
        });
        imgAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtFind.getText().toString().equals(phone))
                {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    edtFind.setText("");
                                    edtFind.requestFocus();
                                    break;

                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(MakeFriend.this);
                    builder.setMessage("Không thể tìm số điện thoại của bạn nhé ahihi đồ ngốc").setPositiveButton("oke ", dialogClickListener)
                            .show();
                }
                if(awesomeValidation.validate()) {
                findProfile(edtFind.getText().toString());}

            }
        });

        rcvFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeFriend.this,FriendRequestProfile.class);
                startActivity(intent);
                finish();
            }
        });
        rcvFriendSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeFriend.this,FriendSuggestionsProfile.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void findProfile(String receiver) {
        String url ="https://chaty-api.herokuapp.com/profile?phone="+receiver;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));
                            JSONObject respObj2 = new JSONObject(respObj.get("data").toString());
                            Log.d("find",response.toString());
                            Log.d("find",respObj2.toString());
                            Intent intent = new Intent(MakeFriend.this,FriendSuggestionsProfile.class);
                            intent.putExtra("respObj2",respObj2.toString());
                            intent.putExtra("token",token);
                            intent.putExtra("profileId",profileId);
                            intent.putExtra("email",email);
                            intent.putExtra("phone",phone);

                            intent.putExtra("receiver",receiver);


                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error.networkResponse.statusCode == 400)
                {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                        edtFind.setText("");
                                        edtFind.requestFocus();
                                    break;

                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(MakeFriend.this);
                    builder.setMessage("Không tìm thấy").setPositiveButton("oke ", dialogClickListener)
                            .show();
                }
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
    private void getReqSender(String phone) {
        String url ="https://chaty-api.herokuapp.com/request/sender/"+phone;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));
                            JSONArray respObj2 = new JSONArray(respObj.getString("data"));

                            Log.d("find",respObj2.getJSONObject(0).toString());
                            for (int i = respObj2.length() - 1; i >= 0; i--)
                            {
                                JSONObject object =respObj2.getJSONObject(i);

                            }
//                            Log.d("find",respObj2.toString());
//                            Intent intent = new Intent(MakeFriend.this,FriendSuggestionsProfile.class);
//                            intent.putExtra("respObj2",respObj2.toString());
//                            intent.putExtra("token",token);
//                            intent.putExtra("profileId",profileId);
//                            intent.putExtra("email",email);
//                            intent.putExtra("phone",phone);
//                            intent.putExtra("phone2",edtFind.getText().toString());
//                            startActivity(intent);
//                            finish();
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