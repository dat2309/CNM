package com.example.chaty;

import static com.example.chaty.FriendHome.email;
import static com.example.chaty.FriendHome.phone;
import static com.example.chaty.FriendHome.profileId;
import static com.example.chaty.FriendHome.token;

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

import com.android.volley.DefaultRetryPolicy;
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

public class ItemFriendSuggestionsAdapter extends RecyclerView.Adapter<ItemFriendSuggestionsAdapter.MyViewHolderItemFriendSuggestions> {

    private List<ItemFriendSuggestions> itemFriendSuggestionss;
    private Context context;
    String token,profileId,email,phone;
    View view;
    public ItemFriendSuggestionsAdapter(Context context,String profileId,String token,String email, String phone,List<String> list)
    {

        this.context=context;
        this.profileId=profileId;
        this.token=token;
        this.email = email;
        this.phone = phone;
//        String url=BuildConfig.API+"profile/suggestion/"+profileId;
//        HTTPConnector httpConnector=new HTTPConnector(url,context,this,token,list);
//        Log.d("http",httpConnector.toString());
        getSuggestionFr(profileId,list);
    }

    @NonNull
    @Override
    public ItemFriendSuggestionsAdapter.MyViewHolderItemFriendSuggestions onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_friendsuggestions,parent,false);
        return new MyViewHolderItemFriendSuggestions(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderItemFriendSuggestions holder, int position) {
        ItemFriendSuggestions itemFriendSuggestions = itemFriendSuggestionss.get(position);
        if(itemFriendSuggestions.getImgFriendSuggestions().equalsIgnoreCase(BuildConfig.API+"file/avatar/smile.png"))
            holder.imgAvatarFriendSuggestions.setImageResource(R.drawable.smile);
        else{
            Glide.with(context)
                    .load(itemFriendSuggestions.getImgFriendSuggestions())
                    .into(  holder.imgAvatarFriendSuggestions);}
        holder.imgInfoFriendSuggestions.setImageResource(itemFriendSuggestions.getImgInforFriendSuggestions());
        holder.tvNameFriendSuggestions.setText(itemFriendSuggestions.getTvNameFriendSuggestions());
        holder.tvPhoneNumberFriendSuggestions.setText(itemFriendSuggestions.getTvPhoneNumberFriendSuggestions());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, FriendSuggestionsProfile.class);
                intent.putExtra("respObj2",itemFriendSuggestions.getObj());
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                intent.putExtra("receiver",itemFriendSuggestions.getPhone());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemFriendSuggestionss.size();
    }




    public class MyViewHolderItemFriendSuggestions extends RecyclerView.ViewHolder {

        ImageView imgAvatarFriendSuggestions,imgInfoFriendSuggestions;
        TextView tvNameFriendSuggestions,tvPhoneNumberFriendSuggestions;
        public MyViewHolderItemFriendSuggestions(@NonNull View itemView) {
            super(itemView);
            imgAvatarFriendSuggestions = itemView.findViewById(R.id.imgAvatarFriendSuggestions);
            imgInfoFriendSuggestions = itemView.findViewById(R.id.imgInforFriendSuggestions);
            tvNameFriendSuggestions = itemView.findViewById(R.id.tvNameFriendSuggestions);
            tvPhoneNumberFriendSuggestions = itemView.findViewById(R.id.tvPhoneNumberFriendSuggestions);
        }
    }
    private void getSuggestionFr(String _id,List list) {
        itemFriendSuggestionss = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url =BuildConfig.API+"profile/suggestion/"+_id;
        JSONArray types=new JSONArray();


        for(int i = 1 ; i <= list.size(); i++){
            if(!list.get(i-1).toString().equals(phone)){
            String a=list.get(i-1).toString();
            types.put(a);}

            }
        Log.d("type",types.toString());

        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("suggestion", types);
//            object.put("avatar",null);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));

                            Log.d("sugg",respObj.toString());
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
                                    String frPhone = object.get("phone").toString();
                                    Log.d("dob", frDob);
                                    String frAvatar = object.get("avatar").toString();
                                    itemFriendSuggestionss.add(new ItemFriendSuggestions(frAvatar,R.drawable.ic_info , frName,frDob,object.toString(),frPhone));
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
}
