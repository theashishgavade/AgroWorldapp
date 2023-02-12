package com.project.agroworld.fragments;

import static com.project.agroworld.utils.Constants.setAppLocale;

import android.content.Context;
import android.content.res.Configuration;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.agroworld.R;
import com.project.agroworld.databinding.FragmentProfileBinding;
import com.project.agroworld.utils.Constants;
import com.project.agroworld.utils.PreferenceHelper;

import java.util.Locale;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding dataBinding;
    RecyclerView userProfilePostsRecycler;

    PreferenceHelper preferenceHelper;
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
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        preferenceHelper = PreferenceHelper.getInstance(getContext());
        updateUI(user);

        dataBinding.ivSLanguageMenu .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), dataBinding.ivSettingMenu);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.home_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
                        switch (menuItem.getItemId()){
                            case R.id.menu_english_lng:
                                setAppLocale(getContext(), "en");
                                preferenceHelper.saveData(Constants.ENGLISH_KEY, true);
                                preferenceHelper.saveData(Constants.HINDI_KEY, false);
                                return true;
                            case R.id.menu_hindi_lng:
                                setAppLocale(getContext(), "hi");
                                preferenceHelper.saveData(Constants.ENGLISH_KEY, false);
                                preferenceHelper.saveData(Constants.HINDI_KEY, true);
                                return true;
                        }
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });
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