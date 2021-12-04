package com.example.chaty.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import androidx.core.content.ContextCompat;
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
import com.example.chaty.ChangePassword;
import com.example.chaty.CreateGroupChat;
import com.example.chaty.Item.ItemFriendAddToGroup;
import com.example.chaty.MainActivity;
import com.example.chaty.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemFriendCreateGroupAdapter extends RecyclerView.Adapter<ItemFriendCreateGroupAdapter.MyViewHolderItemFriendCreateToGroup>{
    public static List<ItemFriendAddToGroup> itemFriendCreateToGroupLists;
    private Context context;
    String token,profileId,email,phone,name;
    View view;
    List<Object> sumCreate = new ArrayList();
    List sumName = new ArrayList();
    String nameCon = "";

    public ItemFriendCreateGroupAdapter(Context context,String profileId,String token,String email, String phone,String name)
    {
        this.context=context;
        this.profileId=profileId;
        this.token=token;
        this.email = email;
        this.phone = phone;
        this.name = name;
        sumCreate.add(profileId);

        sumName.add(name);
        nameCon+=name;
        getFriend(profileId);

    }
    @NonNull
    @Override
    public ItemFriendCreateGroupAdapter.MyViewHolderItemFriendCreateToGroup onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_friendaddtogroup,parent,false);
        return new ItemFriendCreateGroupAdapter.MyViewHolderItemFriendCreateToGroup(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemFriendCreateGroupAdapter.MyViewHolderItemFriendCreateToGroup holder, int position) {
        ItemFriendAddToGroup itemCreate= itemFriendCreateToGroupLists.get(position);
        if(itemCreate.getImgAvatarFriendAddToGroup().equalsIgnoreCase(BuildConfig.API+"file/avatar/smile.png"))
            holder.imgAvatarFriendAddToGroup.setImageResource(R.drawable.smile);
        else{
            Glide.with(context)
                    .load(itemCreate.getImgAvatarFriendAddToGroup())
                    .into(   holder.imgAvatarFriendAddToGroup);}
        holder.tvNameFriendAddToGroup.setText(itemCreate.getTvNameFriendAddToGroup());
        holder.tvTimeAddToGroup.setText(itemCreate.getTvTimeAddToGroup());
        holder.tvTimeAddToGroup.setVisibility(View.INVISIBLE);
        holder.radAdd.setChecked(itemCreate.isRadAdd());

        holder.radAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    for(int i= 1 ; i<sumCreate.size(); i++)
                        if(itemCreate.getTvTimeAddToGroup().equals(sumCreate.get(i))){
                            sumCreate.remove(i);
                            sumName.remove(i);}
                }
                else {
                    boolean add = false;
                    for(int i= 1 ; i<sumCreate.size(); i++)
                        if(itemCreate.getTvTimeAddToGroup().equals(sumCreate.get(i)))
                            add = true;
                    if(!add) {
                        sumCreate.add(itemCreate.getTvTimeAddToGroup());
                        sumName.add(itemCreate.getTvNameFriendAddToGroup());
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
        return itemFriendCreateToGroupLists.size();
    }
    public class MyViewHolderItemFriendCreateToGroup extends RecyclerView.ViewHolder {

        ImageView imgAvatarFriendAddToGroup;
        TextView tvNameFriendAddToGroup;
        TextView tvTimeAddToGroup;
        CheckBox radAdd;

        public MyViewHolderItemFriendCreateToGroup(@NonNull View itemView) {
            super(itemView);
            imgAvatarFriendAddToGroup = itemView.findViewById(R.id.imgAvatarFriendAddToGroup);
            tvNameFriendAddToGroup = itemView.findViewById(R.id.tvNameFriendAddToGroup);
            tvTimeAddToGroup = itemView.findViewById(R.id.tvTimeAddToGroup);
            radAdd = itemView.findViewById(R.id.radAdd);
        }
    }

    private void getFriend(String profileID) {
        itemFriendCreateToGroupLists = new ArrayList<>();
        String url =BuildConfig.API+"account/friend/able/"+profileID;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));

                            JSONArray respObj2 = new JSONArray(respObj.getString("data"));
                            if(respObj2.length()>0){
                                for (int i = respObj2.length() - 1; i >= 0; i--) {
                                    JSONObject object = respObj2.getJSONObject(i);
                                    String frName = object.get("name").toString();
                                    String frSex;
                                    if (object.get("sex").equals(true)) {
                                        frSex = "Nam";
                                    } else {
                                        frSex = "Nữ";
                                    }
                                    String frID = object.get("_id").toString();
                                    String frAvatar = object.get("avatar").toString();
                                    itemFriendCreateToGroupLists.add(new ItemFriendAddToGroup(frAvatar, frName, frID, false));
                                } }
                            else {
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                dialog.dismiss();
                                                dialog.cancel();
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
                        }}
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {

            //token
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("authorization",token );
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
    public void createGroupChat(){
        if(sumCreate.size()<=2){
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            dialog.dismiss();
                            dialog.cancel();
                            break;

                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("nhóm phải có ít nhất 3 người ").setPositiveButton("oke ", dialogClickListener)
                    .show();
            Toast.makeText(context,"nhóm phải có ít nhất 3 người ",Toast.LENGTH_LONG).show();
        }
        else
            creatConversationGroup();

    }

    public void creatConversationGroup() {
        String url =BuildConfig.API+"conversation/";
        RequestQueue requestQueue = Volley.newRequestQueue(context);


        JSONArray description=new JSONArray();
        for(int i = 0 ; i< sumCreate.size(); i++)
            description.put(sumCreate.get(i));
        for(int i = 1 ; i<sumCreate.size();i++)
            nameCon+=","+sumName.get(i);

        JSONObject object = new JSONObject();
        try {
//            object.put("avatar", new VolleyMultipartRequest.DataPart( "avatar"+".jpeg", getFileDataFromDrawable(icon),"image/jpeg"));
            object.put("name", nameCon);
            object.put("admin",sumCreate.get(0));
            object.put("participant",description);
//            object.put("avatar", new VolleyMultipartRequest.DataPart( "avatar"+".jpeg", getFileDataFromDrawable(bitmap),"image/jpeg"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("profileId", profileId);
                        intent.putExtra("email", email);
                        intent.putExtra("phone", phone);
                        notifyDataSetChanged();
                        context.startActivity(intent);
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
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,30 , byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();

    }
    public static byte[] getFileDataFromDrawable2(Context context, int id) {
        Drawable drawable = ContextCompat.getDrawable(context, id);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    public static byte[] getFileDataFromDrawable2(Context context, Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void filterList(List<ItemFriendAddToGroup> filteredList){
        itemFriendCreateToGroupLists = filteredList;
        notifyDataSetChanged();
    }
}
