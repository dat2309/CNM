package com.example.chaty;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import static com.example.chaty.MenuChat.bitmap;
import static com.example.chaty.MenuChat.RomID;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.chaty.Adapter.ItemMessageAdapter;
import com.example.chaty.Item.ItemMessage;

import org.json.JSONArray;
import org.json.JSONException;


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Chat extends AppCompatActivity {
    String token, profileId, email, phone, frAvatar, frName, frID, size, idRoom, admin, chat,date;
    ImageView imgReturnChat, imgAva, imgMenuChat, imgSend,imgSendImg ;
    EditText edtChat;
    TextView txtName;
    RecyclerView rcvMessage;
    public static Bitmap bitmap2 ;
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST = 1;
    ItemMessageAdapter itemMessageAdapter;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        profileId = getIntent().getStringExtra("profileId");
        {
            try {
                IO.Options options = IO.Options.builder()
                        .setQuery("accountId="+profileId)
                        .build();
                mSocket = IO.socket(BuildConfig.API,options);
            } catch (URISyntaxException e) {}
        }
        mSocket.connect();
        mSocket.on("server-send-mess",onNewMessage);
        setContentView(R.layout.activity_chat);
        String conveID = "";
        imgSend = findViewById(R.id.imgSend);
        edtChat = findViewById(R.id.edtChatInput);
        imgReturnChat = findViewById(R.id.imgReturnProfile);
        imgMenuChat = findViewById(R.id.imgMenuChat);
        rcvMessage = findViewById(R.id.rcvMessage);
        imgSendImg = findViewById(R.id.imageSendImage);



        try {

            JSONArray participant = new JSONArray(getIntent().getStringExtra("participant"));

            token = getIntent().getStringExtra("token");

            email = getIntent().getStringExtra("email");
            phone = getIntent().getStringExtra("phone");
            frAvatar = getIntent().getStringExtra("frAvatar");
            frName = getIntent().getStringExtra("frName");
            admin = getIntent().getStringExtra("admin");
            size = getIntent().getStringExtra("size");
            if (size.equals("2")) {
                frID = getIntent().getStringExtra("frID");
                conveID += frID;

            } else {
                idRoom = getIntent().getStringExtra("frID");
                conveID += idRoom;

            }

            txtName = findViewById(R.id.txtNameChatbox);
            imgAva = findViewById(R.id.imgAvatarChat);
            if (RomID != null && RomID.equals(conveID)) {

                if (bitmap != null) {
                    imgAva.setImageBitmap(bitmap);
                }
            } else if (frAvatar.equalsIgnoreCase(BuildConfig.API + "file/avatar/smile.png"))
                imgAva.setImageResource(R.drawable.smile);
            else {
                Glide.with(this)
                        .load(frAvatar)
                        .into(imgAva);
            }
            if (frName.length() > 20) {
                String nameS = frName;
                String[] namel = nameS.split(",", 10);

                String nameRoom =""+namel[namel.length-1]+","+namel[0]+","+namel[1]+"....";

                txtName.setText(nameRoom);

            }else
                txtName.setText(frName);
            if (size.equals("2")) {
                itemMessageAdapter = new ItemMessageAdapter(this, token, frID,profileId);
            } else
                itemMessageAdapter = new ItemMessageAdapter(this, token, idRoom,profileId);

            rcvMessage.setLayoutManager(new GridLayoutManager(Chat.this, 1));
            rcvMessage.setAdapter(itemMessageAdapter);
            imgSend.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View v) {
                    if(edtChat.getText().toString().length()!=0 ){
                    chat = edtChat.getText().toString();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                     date = df.format(Calendar.getInstance().getTime());
                    JSONObject object = new JSONObject();

                    try {
                        object.put("_id", profileId);
                        object.put("content", chat);
                        object.put("sendAt", date);
                        if (size.equals("2")) {
                            object.put("conversation", frID);

                        } else
                            object.put("conversation", idRoom);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mSocket.emit("client-send-mess", object);
                    itemMessageAdapter.notifyDataSetChanged();
                    edtChat.setText("");
                    edtChat.clearFocus();
                    hideKeyboard(v);
                }}
            });
            imgSendImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                        if ((ActivityCompat.shouldShowRequestPermissionRationale(Chat.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(Chat.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE))) {

                        } else {
                            ActivityCompat.requestPermissions(Chat.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_PERMISSIONS);
                        }
                    } else {
                        Log.e("Else", "Else");
                        showFileChooser();
                    }

                }
            });
            imgMenuChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Chat.this, MenuChat.class);
                    if (size.equals("2"))
                        intent.putExtra("frID", frID);
                    else
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
            imgReturnChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSocket.disconnect();
                    Intent intent = new Intent(Chat.this, MainActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("profileId", profileId);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                    finish();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    try {
                        String cName = date;
                        String cChat = data.get("data").toString();
                        String cSender = data.get("id").toString();
                        String cAvatar = data.get("avatar").toString();

                        itemMessageAdapter.sendnewMess(cAvatar,cChat,cName,cSender);
                        rcvMessage.scrollToPosition(itemMessageAdapter.getItemMessages().size()-1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                    bitmap2 = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                    sendImage(bitmap2 );
                    long imagename = System.currentTimeMillis();

                } catch (Exception e) {

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
    private void sendImage(final Bitmap bitmap) {
        String url = BuildConfig.API + "file/";
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                            date = df.format(Calendar.getInstance().getTime());
                            JSONObject respObj = new JSONObject(json);
                            JSONObject object2 = new JSONObject();

                            try {
                                object2.put("sendAt",date);
                                object2.put("_id", profileId);
                                object2.put("content", respObj.getString("data"));

                                if (size.equals("2")) {
                                    object2.put("conversation", frID);

                                } else
                                    object2.put("conversation", idRoom);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            mSocket.emit("client-send-file", object2);
                            itemMessageAdapter.notifyDataSetChanged();

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

            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("file", new DataPart("send" + ".jpeg", getFileDataFromDrawable(bitmap), "image/jpeg"));
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
}