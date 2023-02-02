package com.project.agroworld.articles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.project.agroworld.R;
import com.project.agroworld.databinding.ActivityCropsBinding;


public class CropsActivity extends AppCompatActivity {

    private ActivityCropsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_crops);

    }
}