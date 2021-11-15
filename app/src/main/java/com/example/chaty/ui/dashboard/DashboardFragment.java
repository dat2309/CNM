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

import com.example.chaty.Adapter.ItemFriendSenderAdapter;
import com.example.chaty.Item.ItemFriendSuggestions;
import com.example.chaty.Adapter.ItemFriendSuggestionsAdapter;
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