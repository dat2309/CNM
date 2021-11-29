package com.example.chaty;

import static com.example.chaty.Adapter.ItemRemoveMemFromGroupAdapter.itemFriendRemoveFormGroupLists;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chaty.Adapter.ItemFriendAddToGroupAdapter;
import com.example.chaty.Adapter.ItemRemoveMemFromGroupAdapter;
import com.example.chaty.Item.ItemFriendAddToGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RemoveFromGroup extends AppCompatActivity {
    RecyclerView rcvListFriend;
    EditText edtSearch;

    ItemRemoveMemFromGroupAdapter itemFriendRemoveFormGroupAdapter;
    ImageView imgBackRemoveGroup;
    String token, profileId, email, phone, frAvatar, frName, size, admin, frID;
    Button btnRemove;
    List member = new ArrayList();
    JSONArray participant = null;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_group);
        txt = findViewById(R.id.textView19);
        txt.setText("Xóa thành viên");
        try {
            participant = new JSONArray(getIntent().getStringExtra("member"));
            for (int i = 0; i < participant.length(); i++)
                member.add(participant.getString(i));
            token = getIntent().getStringExtra("token");
            profileId = getIntent().getStringExtra("profileId");
            email = getIntent().getStringExtra("email");
            phone = getIntent().getStringExtra("phone");
            admin = getIntent().getStringExtra("admin");
            size = getIntent().getStringExtra("size");
            frAvatar = getIntent().getStringExtra("frAvatar");
            frName = getIntent().getStringExtra("frName");
            frID = getIntent().getStringExtra("frID");
            rcvListFriend = findViewById(R.id.rcvListFriendRemove);
            imgBackRemoveGroup = findViewById(R.id.imgBackAddToGroup);
            btnRemove = findViewById(R.id.btnCreateRoomChat);
            btnRemove.setText("xóa khỏi phòng");
            initView();
            edtSearch = findViewById(R.id.edtSearchAddToGroup);

            edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()==0)
                        initView();
                    else
                        filter(s.toString());
                }
            });


            imgBackRemoveGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RemoveFromGroup.this, MenuChat.class);
                    intent.putExtra("frID", frID);
                    intent.putExtra("frAvatar", frAvatar);
                    intent.putExtra("frName", frName);
                    intent.putExtra("member", participant.toString());
                    intent.putExtra("token", token);
                    intent.putExtra("profileId", profileId);
                    intent.putExtra("email", email);
                    intent.putExtra("phone", phone);
                    intent.putExtra("size", size);
                    intent.putExtra("admin", admin);
                    startActivity(intent);
                    finish();
                }
            });
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemFriendRemoveFormGroupAdapter.delete();

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void filter(String text) {
        List<ItemFriendAddToGroup> filteredList = new ArrayList<>();
        for (ItemFriendAddToGroup item : itemFriendRemoveFormGroupLists) {
            if(item.getTvNameFriendAddToGroup().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        itemFriendRemoveFormGroupAdapter.filterList(filteredList);
        itemFriendRemoveFormGroupAdapter.notifyDataSetChanged();

    }
    public void initView() {

        itemFriendRemoveFormGroupAdapter = new ItemRemoveMemFromGroupAdapter(RemoveFromGroup.this, profileId, frAvatar,frName,token, frID, admin, member);
        rcvListFriend.setAdapter(itemFriendRemoveFormGroupAdapter);
        rcvListFriend.setLayoutManager(new GridLayoutManager(RemoveFromGroup.this, 1));
    }
}
