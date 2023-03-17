package com.project.agroworld.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.agroworld.R;
import com.project.agroworld.articles.ArticleDetailsActivity;
import com.project.agroworld.articles.adapter.CropsAdapter;
import com.project.agroworld.articles.adapter.DiseaseAdapter;
import com.project.agroworld.articles.adapter.FlowersAdapter;
import com.project.agroworld.articles.adapter.FruitsAdapter;
import com.project.agroworld.articles.adapter.HowToExpandAdapter;
import com.project.agroworld.articles.listener.CropsClickListener;
import com.project.agroworld.articles.listener.DiseasesListener;
import com.project.agroworld.articles.listener.ExpandClickListener;
import com.project.agroworld.articles.listener.FlowerClickListener;
import com.project.agroworld.articles.listener.FruitsClickListener;
import com.project.agroworld.articles.model.CropsResponse;
import com.project.agroworld.articles.model.DiseasesResponse;
import com.project.agroworld.articles.model.FlowersResponse;
import com.project.agroworld.articles.model.FruitsResponse;
import com.project.agroworld.articles.model.HowToExpandResponse;
import com.project.agroworld.databinding.ActivityCropsBinding;
import com.project.agroworld.utils.CustomMultiColorProgressBar;
import com.project.agroworld.utils.Permissions;
import com.project.agroworld.viewmodel.AgroViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {

    Context context;
    ArrayList<Fragment> fragments;

    public FragmentAdapter(FragmentManager fm, Context context, ArrayList<Fragment> fragments) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }


}