package com.project.agroworld.fragments;

import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

    ImageView userBackgroundImage, userImageUserFrag, uploadProfilePictureImage;
    TextView userNameUserProfileFrag, userCityUserProfileFrag, userPostsCountUserProfileFrag, userEmailUserProfileFrag, aboutValueUserProfileFrag;
    ProgressBar uploadProgressBarProfile, uploadBackProgressProfile;
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
        userBackgroundImage = view.findViewById(R.id.userBackgroundImage);
        userImageUserFrag = view.findViewById(R.id.userImageUserFrag);
        uploadProfilePictureImage = view.findViewById(R.id.uploadProfilePictureImage);
        uploadProgressBarProfile = view.findViewById(R.id.uploadProgressBarProfile);
        userNameUserProfileFrag = view.findViewById(R.id.userNameUserProfileFrag);
        userCityUserProfileFrag = view.findViewById(R.id.userCityUserProfileFrag);
        userPostsCountUserProfileFrag = view.findViewById(R.id.userPostsCountUserProfileFrag);
        userEmailUserProfileFrag = view.findViewById(R.id.userEmailUserProfileFrag);
        aboutValueUserProfileFrag = view.findViewById(R.id.aboutValueUserProfileFrag);
        uploadBackProgressProfile = view.findViewById(R.id.uploadBackProgressProfile);
        updateUI(user);


    }
    private void updateUI(FirebaseUser user) {
        uploadProgressBarProfile.setVisibility(View.GONE);
        uploadBackProgressProfile.setVisibility(View.GONE);
        if (user != null){
            Glide.with(userImageUserFrag).load(user.getPhotoUrl()).into(userImageUserFrag);
            Glide.with(userBackgroundImage).load(user.getPhotoUrl()).into(userBackgroundImage);
            userNameUserProfileFrag.setText(user.getDisplayName());
            userEmailUserProfileFrag.setText(user.getEmail());
        }
    }
}