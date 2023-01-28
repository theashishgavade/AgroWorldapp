package com.project.agroworld.fragments;

import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.agroworld.R;
import com.project.agroworld.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding dataBinding;
    RecyclerView userProfilePostsRecycler;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Agro world");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        updateUI(user);
    }

    private void updateUI(FirebaseUser user) {
        dataBinding.uploadProgressBarProfile.setVisibility(View.GONE);

        if (user != null){
            Glide.with(requireContext()).load(user.getPhotoUrl()).into(dataBinding.userImageUserFrag);
            dataBinding.tvProfileUserName.setText(user.getDisplayName());
            dataBinding.tvProfileUserEmail.setText(user.getEmail());
        }
    }
}