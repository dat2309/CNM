package com.example.chaty;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemChatAdapter extends RecyclerView.Adapter<ItemChatAdapter.MyViewHolder> {

    private List<ItemChat> itemChats;
    private Context context;
    String token,profileId,email,phone;
    View view;
    public ItemChatAdapter(Context context,String profileId,String token,String email, String phone)
    {
        this.context=context;
        this.profileId=profileId;
        this.token=token;
        this.email = email;
        this.phone = phone;
        getAllChat(profileId);

    }
    @NonNull
    @Override
    public ItemChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         view =LayoutInflater.from(context).inflate(R.layout.itemchat,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    ItemChat itemChat = itemChats.get(position);
        if(itemChat.getImgAvatar().equalsIgnoreCase(BuildConfig.API+"file/avatar/smile.png"))
            holder.imgAvatar.setImageResource(R.drawable.smile);
        else{
            Glide.with(context)
                    .load(itemChat.getImgAvatar())
                    .into(  holder.imgAvatar);}
    holder.txtNameChat.setText(itemChat.getName());
    holder.txtMessage.setText(itemChat.getMessage());
    holder.txtTime.setText(itemChat.getTime());
    holder.imgAvatar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            deleteMem(itemChat.getId(),itemChat.getMember().get(2).toString());
        }
    });
    view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(itemChat.getSize()==2){
                updateCon1("error",itemChat.getId());
            }
        }
    });
    }
    @Override
    public int getItemCount() {
        return itemChats.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtTime,txtMessage,txtNameChat;
        ImageView imgAvatar;

        public MyViewHolder(@NonNull View view) {
            super(view);
            imgAvatar = view.findViewById(R.id.imgAvatar);
            txtNameChat = view.findViewById(R.id.txtNameChat);
            txtMessage = view.findViewById(R.id.txtMessage);
            txtTime = view.findViewById(R.id.txtTime);
        }
    }
    private void getAllChat(String _id) {
        //gửi

        itemChats =  new ArrayList<>();
        String url =BuildConfig.API+"conversation?id="+_id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,object,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));
                            Log.d("find",respObj.toString());
                            JSONArray respObj2 = new JSONArray(respObj.getString("data"));


//                            Log.d("find",respObj2.toString());}

                                for (int i = respObj2.length() - 1; i >= 0; i--)
                                {
                                    JSONObject object =respObj2.getJSONObject(i);
                                    Log.d("obj["+i+"]",object.toString());
                                    String avatar;
                                    String admin = object.get("admin").toString();
                                    List list = new ArrayList();
                                    JSONArray arr = ((JSONArray) object.get("participant"));
                                    int size = ((JSONArray) object.get("participant")).length();
                                    for(int j = 0 ; j <size ; j++) {
                                        list.add(arr.get(j));
                                    }
                                    if(size!=2)
                                    {
                                        avatar= object.get("avatarRoom").toString();

                                    }
                                    else {

                                        if (admin.equals(_id))
                                            avatar = object.get("anotherAvt").toString();
                                        else
                                            avatar = object.get("adminAvt").toString();
                                    }
                                        String idRoom = object.get("_id").toString();
                                        String name = object.get("name").toString();
                                        LocalDate now = LocalDate.now();
                                        itemChats.add(new ItemChat(avatar, name, "dcm trả lời bố m", String.valueOf(now), idRoom, admin,size,list));
                                        Log.d("chat", itemChats.toString());

                                }
                            notifyDataSetChanged();
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
    private void deleteMem(String conID,String memID)  {
        String url =BuildConfig.API+"conversation/member/"+conID+"?accountId="+memID;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));
                           Log.d("xoa",respObj.toString());

                            notifyDataSetChanged();
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
    private void updateCon1(String name,String conID)  {
        String url =BuildConfig.API+"conversation/"+conID;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject object = new JSONObject();

        try {
            object.put("name",name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));
                            Log.d("update",respObj.toString());

                            notifyDataSetChanged();
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
