package com.example.chaty.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.chaty.BuildConfig;
import com.example.chaty.Item.ItemFriendAddToGroup;
import com.example.chaty.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberGroupAdapter extends RecyclerView.Adapter<MemberGroupAdapter.MyViewHolderMemberGroupAdapter> {
    public static List<ItemFriendAddToGroup> itemMemberGroupLists ;
    private Context context;
    String token, profileId, email, phone, frID, admin;
    View view;
    List<Object> sumCreate = new ArrayList();
    List member = new ArrayList();

    public MemberGroupAdapter(Context context, String profileId, String token, String frID, String admin, List member) {
        this.context = context;
        this.profileId = profileId;
        this.token = token;
        this.frID = frID;
        this.member = member;
        this.admin = admin;
        itemMemberGroupLists = new ArrayList<>();
        for(int i = 0 ; i< member.size();i++)
            getFriend(member.get(i).toString());

    }

    @NonNull
    @Override
    public MemberGroupAdapter.MyViewHolderMemberGroupAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_friendaddtogroup, parent, false);
        return new MemberGroupAdapter.MyViewHolderMemberGroupAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberGroupAdapter.MyViewHolderMemberGroupAdapter holder, int position) {
        ItemFriendAddToGroup itemCreate = itemMemberGroupLists.get(position);
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
        holder.radAdd.setVisibility(View.INVISIBLE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    public int getItemCount() {
        return itemMemberGroupLists.size();
    }

    public class MyViewHolderMemberGroupAdapter extends RecyclerView.ViewHolder {

        ImageView imgAvatarFriendAddToGroup;
        TextView tvNameFriendAddToGroup;
        TextView tvTimeAddToGroup;
        CheckBox radAdd;

        public MyViewHolderMemberGroupAdapter(@NonNull View itemView) {
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

                                String aname= respObj2.get("name").toString();
                                String aavatar = respObj2.get("avatar").toString();
                            itemMemberGroupLists.add(new ItemFriendAddToGroup(aavatar, aname, a_id, false));
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


    public void filterList(List<ItemFriendAddToGroup> filteredList){
        itemMemberGroupLists = filteredList;
        notifyDataSetChanged();
    }

}
