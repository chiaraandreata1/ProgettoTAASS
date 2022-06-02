package com.example.ballnet;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ballnet.business.LoginManager;
import com.example.ballnet.business.UserInfo;
import com.example.ballnet.databinding.FragmentUserBinding;

public class UserFragment extends Fragment {

    FragmentUserBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentUserBinding.inflate(inflater, container, false);

        Bundle arguments = getArguments();

//        if (arguments != null && arguments.containsKey("userID")) {
//
//        } else {
            if (getActivity() != null) {
                ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                if (actionBar != null)
                    actionBar.setTitle("Me");
            }

            UserInfo user = LoginManager.getInstance().getUser();

            if (user != null) {
                binding.userDisplayName.setText(user.getDisplayName());
                binding.userEmail.setText(user.getEmail());
                binding.userType.setText(user.getType().toPrettyString());
            } else {
                NavHostFragment.findNavController(UserFragment.this).navigateUp();
            }
//        }

        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}