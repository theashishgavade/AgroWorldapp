package com.project.agroworld.articles;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.project.agroworld.R;
import com.project.agroworld.articles.adapter.FruitsAdapter;
import com.project.agroworld.articles.model.TechniquesResponse;
import com.project.agroworld.ui.viewmodel.AgroViewModel;

import java.util.ArrayList;

public class FruitsActivity extends AppCompatActivity {

    private ArrayList<TechniquesResponse> techniquesResponseArrayList = new ArrayList<>();
    private FruitsAdapter fruitsAdapter;
    private AgroViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruits);
        viewModel = ViewModelProviders.of(this).get(AgroViewModel.class);

          }

}