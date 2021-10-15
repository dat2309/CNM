package com.example.chaty;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemFriendSenderAdapter extends RecyclerView.Adapter<ItemFriendSenderAdapter.MyViewHolderItemFriendSender> {
    private List<ItemFriendRequest> itemFriendRequests;
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
        ItemFriendRequest itemFriendRequest = itemFriendRequests.get(position);
        Log.d("ava",itemFriendRequest.getAvatar());

        if(itemFriendRequest.getAvatar().equalsIgnoreCase("http://chaty-api.herokuapp.com/file/avatar/smile.png"))
            holder.imgAvatarFriendSender.setImageResource(R.drawable.smile);
        else{
            Glide.with(context)
                    .load(itemFriendRequest.getAvatar())
                    .into(holder.imgAvatarFriendSender);}
        holder.imgInfoFriendSender.setImageResource(itemFriendRequest.getImgInforFriendRequest());
        holder.tvNameFriendSender.setText(itemFriendRequest.getName());
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
                intent.putExtra("phone",phone);
                intent.putExtra("reqID",itemFriendRequest.getReqID());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d("size",String.valueOf( itemFriendRequests.size()));
        return itemFriendRequests.size();
    }
    public class MyViewHolderItemFriendSender extends RecyclerView.ViewHolder {

        ImageView imgAvatarFriendSender,imgInfoFriendSender;
        TextView tvNameFriendSender;
        public MyViewHolderItemFriendSender(@NonNull View itemView) {
            super(itemView);
            imgAvatarFriendSender = itemView.findViewById(R.id.imgAvatarFriendSender);
            imgInfoFriendSender = itemView.findViewById(R.id.imgInforFriendSender);
            tvNameFriendSender = itemView.findViewById(R.id.tvNameFriendSender);
        }
    }

    private void getReqSender(String phone) {
        itemFriendRequests = new ArrayList<>();
        String url ="https://chaty-api.herokuapp.com/request/sender/"+phone;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));
                            Log.d("find",respObj.toString());
                            JSONArray respObj2 = new JSONArray(respObj.getString("data"));

                            if(respObj2.length()==0){
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                break;

                                        }
                                    }
                                };

                                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                                builder2.setMessage("Không có ny đã đành còn không có ai kết bạn ").setPositiveButton("oke ", dialogClickListener)
                                        .show();


                            }
//                            Log.d("find",respObj2.toString());}
                            else{
                                for (int i = respObj2.length() - 1; i >= 0; i--)
                                {
                                    JSONObject object =respObj2.getJSONObject(i);
                                    String reqID = object.getString("_id");
                                    JSONObject receiver = object.getJSONObject("receiver");
                                    Log.d("receiver",receiver.toString());
                                    String frName = receiver.get("name").toString();
                                    String frSex = receiver.get("phone").toString();
                                    String frAvatar = receiver.get("avatar").toString();
                                    itemFriendRequests.add(new ItemFriendRequest( R.drawable.ic_info, frName, frSex, frAvatar, frAvatar,reqID));


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
}