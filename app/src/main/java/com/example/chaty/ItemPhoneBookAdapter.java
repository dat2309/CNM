package com.example.chaty;
import static  com.example.chaty.MainActivity.namePr;
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

    public static List<ItemPhoneBook> itemPhoneBooks;
    private Context context;
    String token,profileId,email,phone;
    View view;
    List<String> a;
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
        if(itemPhoneBook.getAvatar().equalsIgnoreCase(BuildConfig.API+"file/avatar/smile.png"))
            holder.imgAvatarPhoneBook.setImageResource(R.drawable.smile);
        else{
            Glide.with(context)
                    .load(itemPhoneBook.getAvatar())
                    .into(  holder.imgAvatarPhoneBook);}
        holder.imgActiveStatus.setImageResource(itemPhoneBook.getImgActiveStatus());
        holder.imgInfoPhoneBook.setImageResource(itemPhoneBook.getImgInfoPhoneBook());
        holder.txtNamePhoneBook.setText(itemPhoneBook.getName());
        holder.imgInfoPhoneBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context,FriendProfile.class);
                intent.putExtra("frAvater",itemPhoneBook.getAvatar());
                intent.putExtra("frName",itemPhoneBook.getName());
                intent.putExtra("frSex",itemPhoneBook.getSex());
                intent.putExtra("frDob",itemPhoneBook.getDob());
                intent.putExtra("frID",itemPhoneBook.getFrID());
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                context.startActivity(intent);
            }
      });
    view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent intent = new Intent(context, Chat.class);
//            intent.putExtra("frAvater",itemPhoneBook.getAvatar());
//            intent.putExtra("frName",itemPhoneBook.getName());
//            intent.putExtra("frSex",itemPhoneBook.getSex());
//            intent.putExtra("frDob",itemPhoneBook.getDob());
//            intent.putExtra("frID",itemPhoneBook.getFrID());
//            intent.putExtra("token",token);
//            intent.putExtra("profileId",profileId);
//            intent.putExtra("email",email);
//            intent.putExtra("phone",phone);
            creatConversation(profileId,itemPhoneBook.getFrID(),namePr);
            context.startActivity(intent);
            Log.d("ahihiighjkl","met");
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
        String url =BuildConfig.API+"account/friend/able/"+profileID;
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
                                itemPhoneBooks.add(new ItemPhoneBook(R.drawable.ic_activestatus, R.drawable.ic_info, frName, frSex, frDob, frAvatar, object.get("_id").toString()));
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
    private void create(String id1,String id2,String name) {
       a = new ArrayList<>();
       a.add(id1);
       a.add(id2);
        String url =BuildConfig.API+"conversation/";

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONArray description=new JSONArray();
        description.put(id1);
        description.put(id2);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject respObj = new JSONObject(response);
                    Log.d("json", response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("loi","loicc");

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("name", name);
                params.put("admin", id1);
                params.put("participant", a.toString());
//                params.put("avatar",BuildConfig.API+"file/avatar/smile.png");
                Log.d("pã ",params.toString());
                return params;
            }
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("authorization",token );
                return headers;
            }
        };

        requestQueue.add(request);
    }
    private void creatConversation(String id1,String id2,String name) {
        String url =BuildConfig.API+"conversation/";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        a = new ArrayList<>();
        a.add(id1);
        a.add(id2);

        JSONArray description=new JSONArray();
        description.put(id1);
        description.put(id2);
        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("name", name);
            object.put("admin",id1);
            object.put("participant",description);
//            object.put("avatar",null);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("duma",object.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("data",response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("loi","loicc");

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

    public void filterList(List<ItemPhoneBook> filteredList){
        itemPhoneBooks = filteredList;
        notifyDataSetChanged();
    }

}
