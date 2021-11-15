package com.example.chaty.ui.notifications;
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

import com.example.chaty.Adapter.ItemFriendRequestAdapter;
import com.example.chaty.Item.ItemFriendSuggestions;
import com.example.chaty.databinding.FragmentNotificationsBinding;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {
//nhan
    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;
    RecyclerView recyclerView;

    private ItemFriendRequestAdapter adapter;
    ArrayList<ItemFriendSuggestions> itemFriendSuggestions;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView= binding.rcvFriendReve;
        adapter = new ItemFriendRequestAdapter(root.getContext(),profileId,token,email,phone);
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