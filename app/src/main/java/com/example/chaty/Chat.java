package com.example.chaty;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chaty.Adapter.ItemMessageAdapter;
import com.example.chaty.Item.ItemMessage;

import org.json.JSONArray;
import org.json.JSONException;


import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Chat extends AppCompatActivity {
    String token, profileId, email, phone, frAvatar, frName, frID, size, idRoom, admin, chat;
    ImageView imgReturnChat, imgAva, imgMenuChat, imgSend;
    EditText edtChat;
    TextView txtName;
    RecyclerView rcvMessage;
    List<ItemMessage> itemMessages;
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



        try {

            JSONArray participant = new JSONArray(getIntent().getStringExtra("participant"));
            Log.d("arrr", participant.toString());
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
                Log.d("frId", frID);
            } else {
                idRoom = getIntent().getStringExtra("frID");
                conveID += idRoom;
                Log.d("roomId", idRoom);
            }
            Log.d("con", conveID);
            txtName = findViewById(R.id.txtNameChatbox);
            imgAva = findViewById(R.id.imgAvatarChat);
            if (RomID != null && RomID.equals(conveID)) {
                Log.d("rommm", RomID);
                if (bitmap != null) {
                    Log.d("bitmap", bitmap.toString());
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
                Log.d("nameT",namel[0]);
                Log.d("nameT",namel[1]);
                Log.d("nameT",namel[2]);
                Log.d("nameT", String.valueOf(namel.length));
                String nameRoom =""+namel[namel.length-1]+","+namel[0]+","+namel[1]+"....";
                Log.d("roomname",nameRoom);
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
                    String date = df.format(Calendar.getInstance().getTime());
                    JSONObject object = new JSONObject();

                    try {
                        object.put("_id", profileId);
                        object.put("content", chat);
                        object.put("sendAt", date);
                        if (size.equals("2")) {
                            object.put("conversation", frID);
                            Log.d("frID",frID);
                        } else
                            object.put("conversation", idRoom);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("chat", object.toString());
                    mSocket.emit("client-send-mess", object);
                    edtChat.setText("");
                    edtChat.clearFocus();
                    hideKeyboard(v);
                }}
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
                    Log.d("datadd",data.toString());
                    try {
                        String cName = data.getString("name");
                        String cChat = data.get("data").toString();
                        String cSender = data.get("id").toString();
                        String cAvatar = data.get("avatar").toString();
                        itemMessageAdapter.sendnewMess(cAvatar,cChat,cName,cSender);
                        rcvMessage.scrollToPosition(itemMessageAdapter.getItemMessages().size()-1);
                        Log.d("halo",cAvatar);
                        Log.d("halo",cChat);
                        Log.d("halo",cName);
                        Log.d("halo",cSender);

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


}