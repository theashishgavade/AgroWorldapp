package com.project.agroworld.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.project.agroworld.R;
import com.project.agroworld.fragments.NewsFragment;

public class ArticlesActivity extends AppCompatActivity {
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

    }
}