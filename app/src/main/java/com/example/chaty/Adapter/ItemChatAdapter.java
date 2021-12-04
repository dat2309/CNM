package com.example.chaty.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.chaty.BuildConfig;
import com.example.chaty.Chat;
import com.example.chaty.Item.ItemChat;
import com.example.chaty.Item.ItemFriendAddToGroup;
import com.example.chaty.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemChatAdapter extends RecyclerView.Adapter<ItemChatAdapter.MyViewHolder> {

    public static List<ItemChat> itemChats;
    private Context context;
    String token, profileId, email, phone;
    View view;

    public ItemChatAdapter(Context context, String profileId, String token, String email, String phone) {
        this.context = context;
        this.profileId = profileId;
        this.token = token;
        this.email = email;
        this.phone = phone;
        getAllChat(profileId);

    }

    @NonNull
    @Override
    public ItemChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.itemchat, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemChat itemChat = itemChats.get(position);
        if (itemChat.getImgAvatar().equalsIgnoreCase(BuildConfig.API + "file/avatar/smile.png"))
            holder.imgAvatar.setImageResource(R.drawable.smile);
        else {
            Glide.with(context)
                    .load(itemChat.getImgAvatar())
                    .into(holder.imgAvatar);
        }
        if (itemChat.getName().length() > 20) {

            String nameS = itemChat.getName();

            String[] namel = nameS.split(",", 10);

            String nameRoom =""+namel[namel.length-1]+","+namel[0]+","+namel[1]+"....";
            holder.txtNameChat.setText(nameRoom);

        } else{
            holder.txtNameChat.setText(itemChat.getName());}
        holder.txtMessage.setText(itemChat.getMessage());
        holder.txtMessage.setVisibility(View.INVISIBLE);
        holder.txtTime.setText(itemChat.getTime());
        holder.imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("frAvatar", itemChat.getImgAvatar());
                intent.putExtra("frName", itemChat.getName());
                intent.putExtra("frID", itemChat.getId());
                intent.putExtra("token", token);
                intent.putExtra("profileId", profileId);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                intent.putExtra("size", String.valueOf(itemChat.getSize()));
                intent.putExtra("admin", itemChat.getAdmin());
                intent.putExtra("participant", itemChat.getMember().toString());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemChats.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtTime, txtMessage, txtNameChat;
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

        itemChats = new ArrayList<>();
        String url = BuildConfig.API + "conversation?id=" + _id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, object,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));

                            JSONArray respObj2 = new JSONArray(respObj.getString("data"));



                            for (int i = respObj2.length() - 1; i >= 0; i--) {
                                JSONObject object = respObj2.getJSONObject(i);

                                String avatar, name;
                                String admin = object.get("admin").toString();
                                List list = new ArrayList();
                                JSONArray arr = ((JSONArray) object.get("participant"));
                                int size = ((JSONArray) object.get("participant")).length();
                                for (int j = 0; j < size; j++) {
                                    list.add(arr.get(j));
                                }
                                if (size != 2) {
                                    avatar = object.get("avatarRoom").toString();
                                    name = object.get("name").toString();

                                } else if (admin.equals(_id)) {
                                    avatar = object.get("anotherAvt").toString();
                                    name = object.get("anotherName").toString();
                                } else {
                                    avatar = object.get("adminAvt").toString();
                                    name = object.get("name").toString();
                                }

                                String idRoom = object.get("_id").toString();

                                LocalDate now = LocalDate.now();
                                itemChats.add(new ItemChat(avatar, name, name, String.valueOf(now), idRoom, admin, size, arr));


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
        }) {

            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("authorization", token);
                return headers;
            }
        };


        requestQueue.add(jsonObjectRequest);

    }


    public void filterList(List<ItemChat> filteredList){
        itemChats = filteredList;
        notifyDataSetChanged();
    }
}
