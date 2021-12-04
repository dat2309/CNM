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
import com.example.chaty.FriendRequestProfile;
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

public class ItemFriendRequestAdapter extends RecyclerView.Adapter<ItemFriendRequestAdapter.MyViewHolderItemFriendRequest> {
    public static List<ItemFriendRequest> itemFriendRequests;
    private Context context;
    String token,profileId,email,phone;
    View view;
    String reqID;
    public ItemFriendRequestAdapter(Context context,String profileId,String token,String email, String phone)
    {
        this.context=context;
        this.profileId=profileId;
        this.token=token;
        this.email = email;
        this.phone = phone;
        getReqRevice(phone);
    }

    @NonNull
    @Override
    public ItemFriendRequestAdapter.MyViewHolderItemFriendRequest onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_friendrequest,parent,false);
        return new MyViewHolderItemFriendRequest(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderItemFriendRequest holder, int position) {
           ItemFriendRequest itemFriendRequest = itemFriendRequests.get(position);

        if(itemFriendRequest.getAvatar().equalsIgnoreCase(BuildConfig.API+"file/avatar/smile.png"))
            holder.imgAvatarFriendRequest.setImageResource(R.drawable.smile);
        else{
            Glide.with(context)
                    .load(itemFriendRequest.getAvatar())
                    .into(holder.imgAvatarFriendRequest);}
        holder.imgInfoFriendRequest.setImageResource(itemFriendRequest.getImgInforFriendRequest());
        holder.tvNameFriendRequest.setText(itemFriendRequest.getName());
        holder.txtDescription.setText(itemFriendRequest.getDob());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, FriendRequestProfile.class);
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

        return itemFriendRequests.size();
    }
    public class MyViewHolderItemFriendRequest extends RecyclerView.ViewHolder {

        ImageView imgAvatarFriendRequest,imgInfoFriendRequest;
        TextView tvNameFriendRequest,txtDescription;
        public MyViewHolderItemFriendRequest(@NonNull View itemView) {
            super(itemView);
            imgAvatarFriendRequest = itemView.findViewById(R.id.imgAvatarFriendRequest);
            imgInfoFriendRequest = itemView.findViewById(R.id.imgInforFriendRequest);
            tvNameFriendRequest = itemView.findViewById(R.id.tvNameFriendRequest);
            txtDescription = itemView.findViewById(R.id.txtDescription);
        }
    }
    private void getReqRevice(String phone) {
        //nhận
        itemFriendRequests = new ArrayList<>();
        String url =BuildConfig.API+"request/receiver/"+phone;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));

                            JSONArray respObj2 = new JSONArray(respObj.getString("data"));

                            if(respObj2.length()>0)
                                for (int i = respObj2.length() - 1; i >= 0; i--)
                                {
                                    JSONObject object =respObj2.getJSONObject(i);
                                    if(object.getString("status").toString().equals("false")){
                                    String description = object.getString("description");
                                    reqID = object.getString("_id");
                                    JSONObject sernderJS = object.getJSONObject("sender");

                                    String frName = sernderJS.get("name").toString();
                                    String frSex = sernderJS.get("phone").toString();
                                    String frAvatar = sernderJS.get("avatar").toString();
                                    itemFriendRequests.add(new ItemFriendRequest( R.drawable.ic_info, frName, frSex, description, frAvatar,reqID));


                                }}
                            else{


                                Toast.makeText(context, "Không nhận được lời mời kết bạn nào ", Toast.LENGTH_SHORT).show();
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
    public void filterList(List<ItemFriendRequest> filteredList){
        itemFriendRequests = filteredList;
        notifyDataSetChanged();
    }
}
