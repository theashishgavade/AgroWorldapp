package com.project.agroworld.fragments;

import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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


public class ProfileFragment extends Fragment {

    ImageView userBackgroundImage, userImageUserFrag;
    TextView tvProfileUserName, tvProfileUserEmail, tvProfileNoDataFound;
    ProgressBar uploadProgressBarProfile;
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        initViews(view);
    }

    private void initViews(View view) {
        userBackgroundImage = view.findViewById(R.id.userImageUserFrag);
        uploadProgressBarProfile = view.findViewById(R.id.uploadProgressBarProfile);
        tvProfileUserName = view.findViewById(R.id.tvProfileUserName);
        tvProfileUserEmail = view.findViewById(R.id.tvProfileUserEmail);
        tvProfileNoDataFound = view.findViewById(R.id.tvProfileNoDataFound);
        updateUI(user);
    }

    private void updateUI(FirebaseUser user) {
        uploadProgressBarProfile.setVisibility(View.GONE);

        if (user != null){
            Glide.with(userImageUserFrag).load(user.getPhotoUrl()).into(userImageUserFrag);
            Glide.with(userBackgroundImage).load(user.getPhotoUrl()).into(userBackgroundImage);
            tvProfileUserName.setText(user.getDisplayName());
            tvProfileUserEmail.setText(user.getEmail());
        }
    }
}