package com.example.chaty.ui.dashboard;
import static com.example.chaty.FriendHome.phone;
import static com.example.chaty.FriendHome.email;
import static com.example.chaty.FriendHome.profileId;
import static com.example.chaty.FriendHome.token;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chaty.ItemFriendRequest;
import com.example.chaty.ItemFriendRequestAdapter;
import com.example.chaty.ItemFriendSenderAdapter;
import com.example.chaty.ItemFriendSuggestions;
import com.example.chaty.ItemFriendSuggestionsAdapter;
import com.example.chaty.R;
import com.example.chaty.databinding.FragmentDashboardBinding;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
//send
    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    ItemFriendSenderAdapter adapter;
    ArrayList<ItemFriendSuggestions> itemFriendSuggestions;
    ItemFriendSuggestionsAdapter itemFriendSuggestionsAdapter;
    RecyclerView recyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        itemFriendSuggestions = new ArrayList<>();
        itemFriendSuggestions.add(new ItemFriendSuggestions(R.drawable.blueduck, R.drawable.ic_info,"Nguyễn Thế Đạt","0123456789"));
        itemFriendSuggestions.add(new ItemFriendSuggestions(R.drawable.brownduck, R.drawable.ic_info,"Ngô Quang Long","0111122222"));
        itemFriendSuggestions.add(new ItemFriendSuggestions(R.drawable.spiderduck, R.drawable.ic_info,"Lê Tuấn Tú","0987654321"));
        itemFriendSuggestions.add(new ItemFriendSuggestions(R.drawable.supermanduck, R.drawable.ic_info,"Nguyễn Thế Đạt","0123459876"));
        itemFriendSuggestions.add(new ItemFriendSuggestions(R.drawable.pinkduck, R.drawable.ic_info,"Lê Tuấn Tú","0123456798"));
        itemFriendSuggestions.add(new ItemFriendSuggestions(R.drawable.cuteduck, R.drawable.ic_info,"Ngô Quang Long","0135798642"));


        recyclerView= binding.rcvFriendSender;
        adapter = new ItemFriendSenderAdapter(root.getContext(),profileId,token,email,phone);
        recyclerView.setLayoutManager(new GridLayoutManager(root.getContext(),
                1));
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}