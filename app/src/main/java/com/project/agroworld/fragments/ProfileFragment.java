package com.project.agroworld.fragments;

import static com.project.agroworld.utils.Constants.setAppLocale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.agroworld.R;
import com.project.agroworld.databinding.FragmentProfileBinding;
import com.project.agroworld.db.FarmerModel;
import com.project.agroworld.db.FarmerViewModel;
import com.project.agroworld.db.adapter.FarmerAdapter;
import com.project.agroworld.db.listener.OnItemClickListener;
import com.project.agroworld.ui.taskManager.AddTaskActivity;
import com.project.agroworld.utils.Constants;
import com.project.agroworld.utils.PreferenceHelper;

import java.util.List;


public class ProfileFragment extends Fragment implements OnItemClickListener {
    private final int REQUEST_CODE = 6124;

    private int maxIdCount;
    private FragmentProfileBinding dataBinding;
    PreferenceHelper preferenceHelper;
    FarmerViewModel viewModel;
    FarmerAdapter farmerAdapter;
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
        viewModel = ViewModelProviders.of(requireActivity()).get(FarmerViewModel.class);
        updateUI(user);
        setUpRecyclerView();
        viewModel.getAllCourses().observe(getViewLifecycleOwner(), new Observer<List<FarmerModel>>() {
            @Override
            public void onChanged(List<FarmerModel> farmerModels) {
                updateTaskUI(farmerModels);
            }
        });

        viewModel.getMaxIDCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer != null) {
                    maxIdCount = integer;
                }
            }
        });


        dataBinding.ivSLanguageMenu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(getContext(), dataBinding.ivSettingMenu);

            // Inflating popup menu from popup_menu.xml file
            popupMenu.getMenuInflater().inflate(R.menu.home_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                // Toast message on menu item clicked
                switch (menuItem.getItemId()) {
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
            });
            // Showing the popup menu
            popupMenu.show();
        });

        dataBinding.addAlarmFab.setOnClickListener(v -> {
            printLog(maxIdCount + "");
            Intent intent = new Intent(getContext(), AddTaskActivity.class);
            intent.putExtra("maxIDCount", maxIdCount);
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    private void updateTaskUI(List<FarmerModel> farmerModels) {
        if (farmerModels.isEmpty()) {
            dataBinding.userProfilePostsRecycler.setVisibility(View.GONE);
            dataBinding.tvProfileNoDataFound.setVisibility(View.VISIBLE);
        } else {
            farmerAdapter.submitList(farmerModels);
        }
    }

    private void setUpRecyclerView() {
        farmerAdapter = new FarmerAdapter(requireContext(), this);
        dataBinding.userProfilePostsRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        dataBinding.userProfilePostsRecycler.setAdapter(farmerAdapter);
    }

    private void updateUI(FirebaseUser user) {
        dataBinding.uploadProgressBarProfile.setVisibility(View.GONE);
        if (user != null) {
            Glide.with(requireContext()).load(user.getPhotoUrl()).into(dataBinding.userImageUserFrag);
            dataBinding.tvProfileUserName.setText(user.getDisplayName());
            dataBinding.tvProfileUserEmail.setText(user.getEmail());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.getAllCourses().observe(getViewLifecycleOwner(), this::updateTaskUI);
    }

    @Override
    public void markTaskCompleted(FarmerModel model) {
        viewModel.delete(model);
        Constants.showToast(requireContext(), "Task Completed");
    }

    @Override
    public void onDeleteClick(FarmerModel model) {
        viewModel.delete(model);
        Constants.showToast(requireContext(), "Item deleted successfully");
    }

    private void printLog(String message) {
        Log.d("ProfileFragment", message);
    }
}