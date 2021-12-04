package com.example.chaty.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.chaty.BuildConfig;
import com.example.chaty.FriendSenderProfile;
import com.example.chaty.Item.ItemFriendAddToGroup;
import com.example.chaty.Item.ItemFriendRequest;
import com.example.chaty.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemFriendSenderAdapter extends RecyclerView.Adapter<ItemFriendSenderAdapter.MyViewHolderItemFriendSender> {
    public static List<ItemFriendRequest> itemFriendSenders;
    private Context context;
    String token,profileId,email,phone;
    View view;
    String reqID;
    public ItemFriendSenderAdapter(Context context,String profileId,String token,String email, String phone)
    {
        this.context=context;
        this.profileId=profileId;
        this.token=token;
        this.email = email;
        this.phone = phone;
        getReqSender(phone);
    }

    @NonNull
    @Override
    public ItemFriendSenderAdapter.MyViewHolderItemFriendSender onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_friendsender,parent,false);
        return new ItemFriendSenderAdapter.MyViewHolderItemFriendSender(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemFriendSenderAdapter.MyViewHolderItemFriendSender holder, int position) {
        ItemFriendRequest itemFriendRequest = itemFriendSenders.get(position);


        if(itemFriendRequest.getAvatar().equalsIgnoreCase(BuildConfig.API+"file/avatar/smile.png"))
            holder.imgAvatarFriendSender.setImageResource(R.drawable.smile);
        else{
            Glide.with(context)
                    .load(itemFriendRequest.getAvatar())
                    .into(holder.imgAvatarFriendSender);}
        holder.imgInfoFriendSender.setImageResource(itemFriendRequest.getImgInforFriendRequest());
        holder.tvNameFriendSender.setText(itemFriendRequest.getName());
        holder.tvDecripstion.setText(itemFriendRequest.getDob());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, FriendSenderProfile.class);
                intent.putExtra("frPhone",itemFriendRequest.getSex());
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                intent.putExtra("reqID",itemFriendRequest.getReqID());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return itemFriendSenders.size();
    }
    public class MyViewHolderItemFriendSender extends RecyclerView.ViewHolder {

        ImageView imgAvatarFriendSender,imgInfoFriendSender;
        TextView tvNameFriendSender,tvDecripstion;
        public MyViewHolderItemFriendSender(@NonNull View itemView) {
            super(itemView);
            imgAvatarFriendSender = itemView.findViewById(R.id.imgAvatarFriendSender);
            imgInfoFriendSender = itemView.findViewById(R.id.imgInforFriendSender);
            tvNameFriendSender = itemView.findViewById(R.id.tvNameFriendSender);
            tvDecripstion = itemView.findViewById(R.id.txtDes);

        }
    }

    private void getReqSender(String phone) {
        //gửi
        itemFriendSenders = new ArrayList<>();
        String url =BuildConfig.API+"request/sender/"+phone;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));

                            JSONArray respObj2 = new JSONArray(respObj.getString("data"));

                            if(respObj2.length()==0){

                                Toast.makeText(context, "Chưa gửi lời mời kết bạn nào ", Toast.LENGTH_SHORT).show();

                            }

                            else{
                                for (int i = respObj2.length() - 1; i >= 0; i--)
                                {
                                    JSONObject object =respObj2.getJSONObject(i);
                                    String description = object.getString("description");
                                    if(object.getString("status").toString().equals("false")) {
                                        String reqID = object.getString("_id");
                                        JSONObject sender = object.getJSONObject("sender");
                                        JSONObject receiver = object.getJSONObject("receiver");

                                        String frName = receiver.get("name").toString();
                                        String frSex = receiver.get("phone").toString();
                                        String frAvatar = receiver.get("avatar").toString();
                                        itemFriendSenders.add(new ItemFriendRequest(R.drawable.ic_info, frName, frSex, description, frAvatar, reqID));

                                    }
                                }}
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
    public void filterList(List<ItemFriendRequest> filteredList){
        itemFriendSenders = filteredList;
        notifyDataSetChanged();
    }
}
