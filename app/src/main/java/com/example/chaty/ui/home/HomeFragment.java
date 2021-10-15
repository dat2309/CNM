package com.example.chaty.ui.home;
import static com.example.chaty.FriendHome.phone;
import static com.example.chaty.FriendHome.email;
import static com.example.chaty.FriendHome.profileId;
import static com.example.chaty.FriendHome.token;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.chaty.FriendSuggestionsProfile;
import com.example.chaty.ItemFriendRequest;
import com.example.chaty.ItemFriendRequestAdapter;
import com.example.chaty.ItemFriendSuggestions;
import com.example.chaty.ItemFriendSuggestionsAdapter;
import com.example.chaty.MakeFriend;
import com.example.chaty.R;
import com.example.chaty.databinding.FragmentHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    ArrayList<ItemFriendRequest> itemFriendRequests;
    ItemFriendRequestAdapter itemFriendRequestAdapter;
    ArrayList<ItemFriendSuggestions> itemFriendSuggestions;
    ItemFriendSuggestionsAdapter itemFriendSuggestionsAdapter;
    EditText edtFind;
    ImageView imageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        itemFriendSuggestions = new ArrayList<>();
        itemFriendSuggestions.add(new ItemFriendSuggestions(R.drawable.blueduck, R.drawable.ic_info, "Nguyễn Thế Đạt", "0123456789"));
        itemFriendSuggestions.add(new ItemFriendSuggestions(R.drawable.brownduck, R.drawable.ic_info, "Ngô Quang Long", "0111122222"));
        itemFriendSuggestions.add(new ItemFriendSuggestions(R.drawable.spiderduck, R.drawable.ic_info, "Lê Tuấn Tú", "0987654321"));
        itemFriendSuggestions.add(new ItemFriendSuggestions(R.drawable.supermanduck, R.drawable.ic_info, "Nguyễn Thế Đạt", "0123459876"));
        itemFriendSuggestions.add(new ItemFriendSuggestions(R.drawable.pinkduck, R.drawable.ic_info, "Lê Tuấn Tú", "0123456798"));
        itemFriendSuggestions.add(new ItemFriendSuggestions(R.drawable.cuteduck, R.drawable.ic_info, "Ngô Quang Long", "0135798642"));
        edtFind = binding.editTextTextPersonName;


        final RecyclerView recyclerView = binding.rcvFriendSuggestions;
        itemFriendSuggestionsAdapter = new ItemFriendSuggestionsAdapter(itemFriendSuggestions, root.getContext());
        recyclerView.setAdapter(itemFriendSuggestionsAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(),
                1));
        imageView = binding.imageView3;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtFind.getText().toString().equals(phone))
                {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    edtFind.setText("");
                                    edtFind.requestFocus();
                                    break;

                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
                    builder.setMessage("Không thể tìm số điện thoại của bạn nhé ahihi đồ ngốc").setPositiveButton("oke ", dialogClickListener)
                            .show();
                }
                else if(validData(root.getContext())){
                    findProfile(edtFind.getText().toString(),root.getContext());}

            }
        });

        return root;


    }
    private void findProfile(String receiver , Context context) {
        String url ="https://chaty-api.herokuapp.com/profile?phone="+receiver;

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject respObj = new JSONObject(String.valueOf(response));
                            JSONObject respObj2 = new JSONObject(respObj.get("data").toString());
                            Log.d("find",response.toString());
                            Log.d("find",respObj2.toString());
                            Intent intent = new Intent(context,FriendSuggestionsProfile.class);
                            intent.putExtra("respObj2",respObj2.toString());
                            intent.putExtra("token",token);
                            intent.putExtra("profileId",profileId);
                            intent.putExtra("email",email);
                            intent.putExtra("phone",phone);
                            intent.putExtra("receiver",receiver);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error.networkResponse.statusCode == 400)
                {

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    edtFind.setText("");
                                    edtFind.requestFocus();
                                    break;

                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Không tìm thấy").setPositiveButton("oke ", dialogClickListener)
                            .show();
                }
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public boolean validData(Context context)
    {
        if(edtFind.length() !=10)
        {
            Toast.makeText( context,"Số điện thoại không được để trống", Toast.LENGTH_SHORT).show();
            edtFind.setError("");
            edtFind.requestFocus();
            return false;
        }
        edtFind.setError(null);
        return true;
    }

}