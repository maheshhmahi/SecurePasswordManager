package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.ui.AppDatabase;
import com.example.myapplication.ui.User;
import com.example.myapplication.ui.UserAdapter;
import com.example.myapplication.ui.UserDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        AppDatabase database = AppDatabase.getDatabase(requireContext());
        UserDao userDao = database.userDao();

        userDao.getAllUsers().observe(getViewLifecycleOwner(), users -> {
            if (users != null) {
                users.sort((u1, u2) -> {
                    int charComparison = Character.compare(
                            Character.toUpperCase(u1.getWebsite().charAt(0)),
                            Character.toUpperCase(u2.getWebsite().charAt(0))
                    );
                    if (charComparison == 0) {
                        return Long.compare(u1.getTimestamp(), u2.getTimestamp());
                    }
                    return charComparison;
                });

                UserAdapter adapter = new UserAdapter(users,requireContext());
                recyclerView.setAdapter(adapter);
            }
        });
        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}