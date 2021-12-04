package com.example.chaty.Adapter;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
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

public class ItemRemoveMemFromGroupAdapter extends RecyclerView.Adapter<ItemRemoveMemFromGroupAdapter.MyViewHolderItemFriendRemoveFromGroup> {
    public static List<ItemFriendAddToGroup> itemFriendRemoveFormGroupLists;
    private Context context;
    String token, profileId, email, phone, frID, admin, frAvatar, frName;
    View view;
    List<Object> sumCreate = new ArrayList();
    List member = new ArrayList();

    public ItemRemoveMemFromGroupAdapter(Context context, String profileId, String frAvatar, String frName, String token, String frID, String admin, List member) {
        this.context = context;
        this.profileId = profileId;

        this.token = token;
        this.frID = frID;
        this.frAvatar = frAvatar;
        this.frName = frName;
        this.member = member;
        this.admin = admin;
        itemFriendRemoveFormGroupLists = new ArrayList<>();
        for (int i = 0; i < member.size(); i++)
            getFriend(member.get(i).toString());

    }

    @NonNull
    @Override
    public ItemRemoveMemFromGroupAdapter.MyViewHolderItemFriendRemoveFromGroup onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_friendaddtogroup, parent, false);
        return new ItemRemoveMemFromGroupAdapter.MyViewHolderItemFriendRemoveFromGroup(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRemoveMemFromGroupAdapter.MyViewHolderItemFriendRemoveFromGroup holder, int position) {
        ItemFriendAddToGroup itemCreate = itemFriendRemoveFormGroupLists.get(position);
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
        return itemFriendRemoveFormGroupLists.size();
    }

    public class MyViewHolderItemFriendRemoveFromGroup extends RecyclerView.ViewHolder {

        ImageView imgAvatarFriendAddToGroup;
        TextView tvNameFriendAddToGroup;
        TextView tvTimeAddToGroup;
        CheckBox radAdd;

        public MyViewHolderItemFriendRemoveFromGroup(@NonNull View itemView) {
            super(itemView);
            imgAvatarFriendAddToGroup = itemView.findViewById(R.id.imgAvatarFriendAddToGroup);
            tvNameFriendAddToGroup = itemView.findViewById(R.id.tvNameFriendAddToGroup);
            tvTimeAddToGroup = itemView.findViewById(R.id.tvTimeAddToGroup);
            radAdd = itemView.findViewById(R.id.radAdd);
        }
    }

    private void getFriend(String profileID) {
        String url = BuildConfig.API + "profile/" + profileID;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONObject respObj = new JSONObject(response);

                            JSONObject respObj2 = new JSONObject(respObj.getString("data"));
                            String a_id = respObj2.get("_id").toString();
                            if (!a_id.equals(profileId)) {
                                String aname = respObj2.get("name").toString();
                                String aavatar = respObj2.get("avatar").toString();
                                itemFriendRemoveFormGroupLists.add(new ItemFriendAddToGroup(aavatar, aname, a_id, false));
                                notifyDataSetChanged();

                            }

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


    public void delete() {
        for (int i = 0; i < sumCreate.size(); i++) {
            deleteMemberFromGroup(sumCreate.get(i).toString(), frID);
            for (int j = 0; j < member.size(); j++)
                if (member.get(j).equals(sumCreate.get(i)))
                    member.remove(j);
        }
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

    public void deleteMemberFromGroup(String deleteID, String conID) {
        JSONArray description = new JSONArray();
        String url = BuildConfig.API + "conversation/member/" + conID + "?accountId=" + deleteID;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        notifyDataSetChanged();

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
    public void filterList(List<ItemFriendAddToGroup> filteredList) {
        itemFriendRemoveFormGroupLists = filteredList;
        notifyDataSetChanged();
    }

}

