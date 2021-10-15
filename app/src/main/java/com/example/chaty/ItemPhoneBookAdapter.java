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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemPhoneBookAdapter extends RecyclerView.Adapter<ItemPhoneBookAdapter.MyViewHolderItemPhoneBook>{

    private List<ItemPhoneBook> itemPhoneBooks;
    private Context context;
    String token,profileId,email,phone;
    View view;
    public ItemPhoneBookAdapter(Context context,String profileId,String token,String email, String phone)
    {
        this.context=context;
        this.profileId=profileId;
        this.token=token;
        this.email = email;
        this.phone = phone;
        getFriend(profileId);
    }
    @NonNull
    @Override
        public ItemPhoneBookAdapter.MyViewHolderItemPhoneBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_phonebook,parent,false);
        return new MyViewHolderItemPhoneBook(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderItemPhoneBook holder, int position) {
        ItemPhoneBook itemPhoneBook = itemPhoneBooks.get(position);
        if(itemPhoneBook.getAvatar().equalsIgnoreCase("http://chaty-api.herokuapp.com/file/avatar/smile.png"))
            holder.imgAvatarPhoneBook.setImageResource(R.drawable.smile);
        else{
            Glide.with(context)
                    .load(itemPhoneBook.getAvatar())
                    .into(  holder.imgAvatarPhoneBook);}
        holder.imgActiveStatus.setImageResource(itemPhoneBook.getImgActiveStatus());
        holder.imgInfoPhoneBook.setImageResource(itemPhoneBook.getImgInfoPhoneBook());
        holder.txtNamePhoneBook.setText(itemPhoneBook.getName());
//        holder.imgInfoPhoneBook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context,FriendProfile.class);
//                intent.putExtra("avt",itemPhoneBook.getImgAvatarPhoneBook());
//                intent.putExtra("status", itemPhoneBook.getImgActiveStatus());
//                intent.putExtra("info", itemPhoneBook.getImgInfoPhoneBook());
//                intent.putExtra("name",itemPhoneBook.getTxtNamePhoneBook());
//                context.startActivity(intent);
//            }
//      });
    view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent intent = new Intent(context, FriendProfile.class);
            intent.putExtra("frAvater",itemPhoneBook.getAvatar());
            intent.putExtra("frName",itemPhoneBook.getName());
            intent.putExtra("frSex",itemPhoneBook.getSex());
            intent.putExtra("frDob",itemPhoneBook.getDob());
            intent.putExtra("token",token);
            intent.putExtra("profileId",profileId);
            intent.putExtra("email",email);
            intent.putExtra("phone",phone);
            context.startActivity(intent);
        }
    });
    }
    @Override
    public int getItemCount() {
        return itemPhoneBooks.size();
    }
    public class MyViewHolderItemPhoneBook extends RecyclerView.ViewHolder {

        ImageView imgAvatarPhoneBook, imgActiveStatus, imgInfoPhoneBook;
        TextView txtNamePhoneBook;

        public MyViewHolderItemPhoneBook(@NonNull View itemView) {
            super(itemView);
            imgAvatarPhoneBook = itemView.findViewById(R.id.imgAvatarPhoneBook);
            imgActiveStatus = itemView.findViewById(R.id.imgActiveStatus);
            imgInfoPhoneBook = itemView.findViewById(R.id.imgInfoPhoneBook);
            txtNamePhoneBook = itemView.findViewById(R.id.txtNamePhoneBook);
        }
    }
    private void getFriend(String profileID) {
        itemPhoneBooks = new ArrayList<>();
        String url ="https://chaty-api.herokuapp.com/account/friend/able/"+profileID;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));

                            Log.d("find",respObj.toString());
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
                                String frDob = object.get("dob").toString();
                                Log.d("dob", frDob);
                                String frAvatar = object.get("avatar").toString();
                                itemPhoneBooks.add(new ItemPhoneBook(R.drawable.ic_activestatus, R.drawable.ic_info, frName, frSex, frDob, frAvatar));
                            } }
                            else {
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

}
