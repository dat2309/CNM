package com.example.chaty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import static com.example.chaty.Adapter.ItemPhoneBookAdapter.itemPhoneBooks;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.chaty.Adapter.ItemPhoneBookAdapter;
import com.example.chaty.Item.ItemPhoneBook;

import java.util.ArrayList;
import java.util.List;

public class PhoneBook extends AppCompatActivity {
    private ItemPhoneBookAdapter adapter;
    RecyclerView rcvListPhoneBook;
    ImageView imgBackPhoneBook;
    String token,profileId,email,phone;
    EditText edtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_book);
        token= getIntent().getStringExtra("token");
        profileId = getIntent().getStringExtra("profileId");
        email= getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        rcvListPhoneBook = findViewById(R.id.rcvListPhoneBook);
        imgBackPhoneBook = findViewById(R.id.imgBackPhoneBook);
        initView();
        edtSearch = findViewById(R.id.edtFriendSearch);
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
        imgBackPhoneBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneBook.this,MainActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("profileId",profileId);
                intent.putExtra("email",email);
                intent.putExtra("phone",phone);
                startActivity(intent);
                finish();

            }
        });
    }
    public void filter(String text) {
        List<ItemPhoneBook> filteredList = new ArrayList<>();
        for (ItemPhoneBook item : itemPhoneBooks) {
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
        adapter.notifyDataSetChanged();

    }
    public void initView() {
        adapter = new ItemPhoneBookAdapter(PhoneBook.this,profileId,token,email,phone);
        rcvListPhoneBook.setLayoutManager(new GridLayoutManager(PhoneBook.this, 1));
        rcvListPhoneBook.setAdapter(adapter);
    }
}