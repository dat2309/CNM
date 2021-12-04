package com.example.chaty.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.chaty.BuildConfig;
import com.example.chaty.Item.ItemMessage;
import com.example.chaty.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemMessageAdapter extends RecyclerView.Adapter<ItemMessageAdapter.MyViewHolderItemMessage> {

    private List<ItemMessage> itemMessages;
    private Context context;
    private String token, roomID,profileId;

    public ItemMessageAdapter( Context context, String token,String roomID,String profileId) {
        this.token = token;
        this.roomID = roomID;
        this.context = context;
        this.profileId= profileId;
        getListChat(roomID,token);
    }

    public void setItemMessages(List<ItemMessage> itemMessages) {
        this.itemMessages = itemMessages;
    }

    public List<ItemMessage> getItemMessages() {
        return itemMessages;
    }

    @NonNull
    @Override
    public MyViewHolderItemMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message,parent,false);
        return new MyViewHolderItemMessage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderItemMessage holder, int position) {
        ItemMessage itemMessage = itemMessages.get(position);
        if(profileId.equals(itemMessage.getSenderID()))
        {
            if(itemMessage.getImgAvatarMessage().equalsIgnoreCase(BuildConfig.API+"file/avatar/smile.png"))
                holder.imgAvatarMessage2.setImageResource(R.drawable.smile);
            else{
                Glide.with(context)
                        .load(itemMessage.getImgAvatarMessage())
                        .into(  holder.imgAvatarMessage2);}
            if(itemMessage.getTvMessageChat().contains(BuildConfig.API+"file/")&&(itemMessage.getTvMessageChat().contains(".jpg")||
                    itemMessage.getTvMessageChat().contains(".JPG")||
                    itemMessage.getTvMessageChat().contains(".PNG")||
                    itemMessage.getTvMessageChat().contains(".png") ||
                    itemMessage.getTvMessageChat().contains(".JPEG")||
                    itemMessage.getTvMessageChat().contains(".jpeg"))){
                holder.imgMess2.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(itemMessage.getTvMessageChat())
                        .into(  holder.imgMess2);
                holder.tvMessageChat2.setVisibility(View.INVISIBLE);
                holder.tvTimeMessage2.setVisibility(View.INVISIBLE);
            }
            else{
                holder.tvMessageChat2.setText(itemMessage.getTvMessageChat());
                holder.tvMessageChat2.setVisibility(View.VISIBLE);
                holder.imgMess2.setVisibility(View.GONE);
                holder.tvTimeMessage2.setVisibility(View.VISIBLE);
            }

            holder.tvTimeMessage2.setText(itemMessage.getTvTimeMessage());
            holder.imgAvatarMessage2.setVisibility(View.VISIBLE);


            holder.cardView2.setVisibility(View.VISIBLE);
            holder.imgAvatarMessage.setVisibility(View.INVISIBLE);
            holder.tvMessageChat.setVisibility(View.INVISIBLE);
            holder.tvTimeMessage.setVisibility(View.INVISIBLE);
            holder.imgMess.setVisibility(View.GONE);
            holder.cardView.setVisibility(View.INVISIBLE);

        }
        else{
        if(itemMessage.getImgAvatarMessage().equalsIgnoreCase(BuildConfig.API+"file/avatar/smile.png"))
            holder.imgAvatarMessage.setImageResource(R.drawable.smile);
        else{
            Glide.with(context)
                    .load(itemMessage.getImgAvatarMessage())
                    .into(  holder.imgAvatarMessage);}
            if(itemMessage.getTvMessageChat().contains(BuildConfig.API+"file/")&&(itemMessage.getTvMessageChat().contains(".jpg")||
                    itemMessage.getTvMessageChat().contains(".JPG")||
                    itemMessage.getTvMessageChat().contains(".PNG")||
                    itemMessage.getTvMessageChat().contains(".png"))){
                holder.imgMess.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(itemMessage.getTvMessageChat())
                        .into(holder.imgMess);
                holder.tvMessageChat.setVisibility(View.INVISIBLE);
                holder.tvTimeMessage.setVisibility(View.INVISIBLE);
            }
            else{
                holder.tvMessageChat.setText(itemMessage.getTvMessageChat());
                holder.tvMessageChat.setVisibility(View.VISIBLE);
                holder.tvTimeMessage.setVisibility(View.VISIBLE);
                holder.imgMess.setVisibility(View.GONE);
            }
            holder.tvTimeMessage.setText(itemMessage.getTvTimeMessage());
            holder.imgAvatarMessage.setVisibility(View.VISIBLE);

            holder.cardView.setVisibility(View.VISIBLE);
            holder.imgAvatarMessage2.setVisibility(View.INVISIBLE);
            holder.tvMessageChat2.setVisibility(View.INVISIBLE);
            holder.tvTimeMessage2.setVisibility(View.INVISIBLE);
            holder.cardView2.setVisibility(View.INVISIBLE);
            holder.imgMess2.setVisibility(View.GONE);

    }}

    @Override
    public int getItemCount() {
        return itemMessages.size();
    }

    public class MyViewHolderItemMessage extends RecyclerView.ViewHolder {
        CardView cardView,cardView2;
        ImageView imgAvatarMessage,imgAvatarMessage2,imgMess,imgMess2;
        TextView tvMessageChat,tvTimeMessage,tvMessageChat2,tvTimeMessage2;
        public MyViewHolderItemMessage(@NonNull View itemView) {
            super(itemView);
            imgAvatarMessage = itemView.findViewById(R.id.imgAvatarMessage);
            tvMessageChat = itemView.findViewById(R.id.tvMessageChat);
            tvTimeMessage = itemView.findViewById(R.id.tvTimeMessage);
            cardView = itemView.findViewById(R.id.getUserImgMessage);
            imgMess = itemView.findViewById(R.id.imageMess);
            imgAvatarMessage2 = itemView.findViewById(R.id.imgAvatarMessage2);
            tvMessageChat2 = itemView.findViewById(R.id.tvMessageChat2);
            tvTimeMessage2 = itemView.findViewById(R.id.tvTimeMessage2);
            imgMess2 = itemView.findViewById(R.id.imageMess2);
            cardView2 = itemView.findViewById(R.id.getUserImgMessage2);
        }

    }
    private void getListChat(String roomID,String token) {
        itemMessages = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url =BuildConfig.API+"message?conversationId="+roomID;
        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));


                            JSONArray respObj2 = new JSONArray(respObj.getString("data"));
                            for (int i = respObj2.length() - 1; i >= 0; i--) {
                                JSONObject object = respObj2.getJSONObject(i);
                                String frName = object.get("sendAt").toString();
                                String chat = object.get("body").toString();
                                String sender = object.get("sender").toString();
                                String avatar = object.get("avatar").toString();
                                itemMessages.add(new ItemMessage(avatar,chat,frName,sender));

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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,-1,0));
        requestQueue.add(jsonObjectRequest);
    }
    public void sendnewMess(String cavatar,String cchat,String cname,String csender){
        itemMessages.add(new ItemMessage(cavatar,cchat,cname,csender));
        notifyDataSetChanged();

    }
}
