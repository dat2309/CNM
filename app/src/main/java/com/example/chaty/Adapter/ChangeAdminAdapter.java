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
import android.widget.Toast;

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
import com.example.chaty.BuildConfig;
import com.example.chaty.ChangePassForgot;
import com.example.chaty.Item.ItemFriendAddToGroup;
import com.example.chaty.Item.ItemPhoneBook;
import com.example.chaty.MainActivity;
import com.example.chaty.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeAdminAdapter extends RecyclerView.Adapter<ChangeAdminAdapter.MyViewHolderChangeAdminAdapter> {
    public static List<ItemFriendAddToGroup> itemFriendLists ;
    private Context context;
    String token, profileId, email, phone, frID, admin;
    View view;
    List<Object> sumCreate = new ArrayList();
    List member = new ArrayList();

    public ChangeAdminAdapter(Context context, String profileId, String token, String frID, String admin, List member) {
        this.context = context;
        this.profileId = profileId;
        this.token = token;
        this.frID = frID;
        this.member = member;
        this.admin = admin;
        itemFriendLists = new ArrayList<>();
        for(int i = 0 ; i< member.size();i++)
            if(!profileId.equals(member.get(i).toString()))
                getFriend(member.get(i).toString());

    }

    @NonNull
    @Override
    public ChangeAdminAdapter.MyViewHolderChangeAdminAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_friendaddtogroup, parent, false);
        return new ChangeAdminAdapter.MyViewHolderChangeAdminAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChangeAdminAdapter.MyViewHolderChangeAdminAdapter holder, int position) {
        ItemFriendAddToGroup itemCreate = itemFriendLists.get(position);
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
        return itemFriendLists.size();
    }

    public class MyViewHolderChangeAdminAdapter extends RecyclerView.ViewHolder {

        ImageView imgAvatarFriendAddToGroup;
        TextView tvNameFriendAddToGroup;
        TextView tvTimeAddToGroup;
        CheckBox radAdd;

        public MyViewHolderChangeAdminAdapter(@NonNull View itemView) {
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
                            itemFriendLists.add(new ItemFriendAddToGroup(aavatar, aname, a_id, false));
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

    public void change(Context contexta){
        if(!String.valueOf(sumCreate.size()).equals("1")){
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            break;

                    }
                }
            };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Chỉ được chọn một người ").setPositiveButton("oke ", dialogClickListener)
                .show();
    }
        else {
            changeAdmin();
            Intent intent = new Intent(contexta, MainActivity.class);
            intent.putExtra("token",token);
            intent.putExtra("profileId",profileId);
            intent.putExtra("email",email);
            intent.putExtra("phone",phone);
            context.startActivity(intent);
        }

    }
    public void changeAdmin() {
        String url = BuildConfig.API + "conversation/admin/"+frID;
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JSONArray description = new JSONArray();
        for (int i = 0; i < sumCreate.size(); i++)
            description.put(sumCreate.get(i));


        JSONObject object = new JSONObject();
        try {


            object.put("accountId", description);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(context,"yêu cầu thành công",Toast.LENGTH_LONG).show();

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
        itemFriendLists = filteredList;
        notifyDataSetChanged();
    }
}
