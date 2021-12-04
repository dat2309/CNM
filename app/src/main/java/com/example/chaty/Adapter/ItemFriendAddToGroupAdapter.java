package com.example.chaty.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.chaty.AddToGroup;
import com.example.chaty.BuildConfig;
import com.example.chaty.Item.ItemFriendAddToGroup;
import com.example.chaty.MenuChat;
import com.example.chaty.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemFriendAddToGroupAdapter extends RecyclerView.Adapter<ItemFriendAddToGroupAdapter.MyViewHolderItemFriendAddToGroup> {
    public static List<ItemFriendAddToGroup> itemFriendAddToGroupLists;
    private Context context;
    String token, profileId, email, phone, frID, admin,frAvatar, frName;
    View view;
    List<Object> sumCreate = new ArrayList();
    List member = new ArrayList();

    public ItemFriendAddToGroupAdapter(Context context, String profileId, String frAvatar,String frName ,String token, String frID,String admin,List member) {
        this.context = context;
        this.profileId = profileId;
        this.token = token;
        this.frID = frID;
        this.frAvatar = frAvatar;
        this.frName = frName;

        this.member = member;
        this.admin = admin;
        getFriend(profileId);

    }

    @NonNull
    @Override
    public ItemFriendAddToGroupAdapter.MyViewHolderItemFriendAddToGroup onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_friendaddtogroup, parent, false);
        return new ItemFriendAddToGroupAdapter.MyViewHolderItemFriendAddToGroup(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemFriendAddToGroupAdapter.MyViewHolderItemFriendAddToGroup holder, int position) {
        ItemFriendAddToGroup itemCreate = itemFriendAddToGroupLists.get(position);
        if (itemCreate.getImgAvatarFriendAddToGroup().equalsIgnoreCase(BuildConfig.API + "file/avatar/smile.png"))
            holder.imgAvatarFriendAddToGroup.setImageResource(R.drawable.smile);
        else {
            Glide.with(context)
                    .load(itemCreate.getImgAvatarFriendAddToGroup())
                    .into(holder.imgAvatarFriendAddToGroup);
        }
        holder.tvNameFriendAddToGroup.setText(itemCreate.getTvNameFriendAddToGroup());
        holder.tvTimeAddToGroup.setText(itemCreate.getTvTimeAddToGroup());
        holder.tvTimeAddToGroup.setVisibility(View.INVISIBLE);
        holder.radAdd.setChecked(itemCreate.isRadAdd());

        holder.radAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    for (int i = 0; i < sumCreate.size(); i++)
                        if (itemCreate.getTvTimeAddToGroup().equals(sumCreate.get(i))) {
                            sumCreate.remove(i);
                        }
                } else {
                    boolean add = false;
                    for (int i = 0; i < sumCreate.size(); i++)
                        if (itemCreate.getTvTimeAddToGroup().equals(sumCreate.get(i)))
                            add = true;
                    if (!add) {
                        sumCreate.add(itemCreate.getTvTimeAddToGroup());
                    }
                }


            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    public int getItemCount() {
        return itemFriendAddToGroupLists.size();
    }

    public class MyViewHolderItemFriendAddToGroup extends RecyclerView.ViewHolder {

        ImageView imgAvatarFriendAddToGroup;
        TextView tvNameFriendAddToGroup;
        TextView tvTimeAddToGroup;
        CheckBox radAdd;

        public MyViewHolderItemFriendAddToGroup(@NonNull View itemView) {
            super(itemView);
            imgAvatarFriendAddToGroup = itemView.findViewById(R.id.imgAvatarFriendAddToGroup);
            tvNameFriendAddToGroup = itemView.findViewById(R.id.tvNameFriendAddToGroup);
            tvTimeAddToGroup = itemView.findViewById(R.id.tvTimeAddToGroup);
            radAdd = itemView.findViewById(R.id.radAdd);
        }
    }

    private void getFriend(String profileID) {
        itemFriendAddToGroupLists = new ArrayList<>();
        String url = BuildConfig.API + "account/friend/able/" + profileID;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));


                            JSONArray respObj2 = new JSONArray(respObj.getString("data"));
                            if (respObj2.length() > 0) {
                                for (int i = respObj2.length() - 1; i >= 0; i--) {
                                    Boolean same = false;
                                    JSONObject object = respObj2.getJSONObject(i);
                                    String frID = object.get("_id").toString();
                                    for(int j = 0; j<member.size() ; j++)
                                        if(frID.equals(member.get(j)))
                                            same = true;
                                    if(!same){
                                    String frName = object.get("name").toString();
                                    String frSex;
                                    if (object.get("sex").equals(true)) {
                                        frSex = "Nam";
                                    } else {
                                        frSex = "Nữ";
                                    }

                                    String frAvatar = object.get("avatar").toString();
                                        itemFriendAddToGroupLists.add(new ItemFriendAddToGroup(frAvatar, frName, frID, false));
                                }}
                            } else {
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:

                                                break;

                                        }
                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("lêu lêu đồ ko có bạn ").setPositiveButton("oke ", dialogClickListener)
                                        .show();
                            }
                            notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            //token
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("authorization", token);
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }


    public void addMemberToGroup() {
        String url = BuildConfig.API + "conversation/member/"+frID;
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JSONArray description = new JSONArray();
        for (int i = 0; i < sumCreate.size(); i++){
            description.put(sumCreate.get(i));
            member.add(sumCreate.get(i));
        }




        JSONObject object = new JSONObject();
        try {


            object.put("participant", description);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        notifyDataSetChanged();
                        Intent intent = new Intent(context, MenuChat.class);
                        intent.putExtra("frID", frID);
                        intent.putExtra("frAvatar", frAvatar);
                        intent.putExtra("frName", frName);
                        intent.putExtra("member", String.valueOf(member));
                        intent.putExtra("token", token);
                        intent.putExtra("profileId", profileId);
                        intent.putExtra("email", email);
                        intent.putExtra("phone", phone);
                        intent.putExtra("size", String.valueOf(member.size()));
                        intent.putExtra("admin", admin);
                        context.startActivity(intent);


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

    public void filterList(List<ItemFriendAddToGroup> filteredList){
        itemFriendAddToGroupLists = filteredList;
        notifyDataSetChanged();
    }
}

