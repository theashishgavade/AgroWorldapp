package com.project.agroworldapp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.project.agroworldapp.BuildConfig;
import com.project.agroworldapp.R;
import com.project.agroworldapp.db.PreferenceHelper;
import com.project.agroworldapp.ui.adapter.FragmentAdapter;
import com.project.agroworldapp.ui.fragments.EducationFragment;
import com.project.agroworldapp.ui.fragments.HomeFragment;
import com.project.agroworldapp.ui.fragments.NewsFragment;
import com.project.agroworldapp.ui.fragments.ProfileFragment;
import com.project.agroworldapp.ui.fragments.ShoppingFragment;
import com.project.agroworldapp.ui.fragments.TransportFragment;
import com.project.agroworldapp.utils.Constants;
import com.project.agroworldapp.utils.Permissions;

import java.util.ArrayList;

/**
* This Activity will setup the bottom tab layout & view pager
* */

public class DashboardActivity extends AppCompatActivity {
    long back_pressed;
    ViewPager viewPager;
    TabLayout tabLayout;
    ArrayList<Fragment> fragments;
    PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceHelper = PreferenceHelper.getInstance(this);
        boolean isHindi = preferenceHelper.getData(Constants.HINDI_KEY);
        if (isHindi) {
            Constants.setAppLocale(DashboardActivity.this, "hi");
        } else {
            Constants.setAppLocale(DashboardActivity.this, "en");
        }
        setContentView(R.layout.activity_dashboard);
        Permissions.checkConnection(this);
        Permissions.isGpsEnable(this);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);

        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new ShoppingFragment());
        fragments.add(new TransportFragment());
        fragments.add(new EducationFragment());
        fragments.add(new NewsFragment());
        fragments.add(new ProfileFragment());

        FragmentAdapter pagerAdapter = new FragmentAdapter(getSupportFragmentManager(), getApplicationContext(), fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setEnabled(false);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_home_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_shop_two_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_emoji_transportation_24);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_baseline_menu_book_24);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_baseline_newspaper_24);
        tabLayout.getTabAt(5).setIcon(R.drawable.ic_baseline_person_24);

        tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(3).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(4).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(5).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#5D1F91"));
        tabLayout.setSelectedTabIndicatorHeight((int) (3 * getResources().getDisplayMetrics().density));
        tabLayout.setTabTextColors(Color.parseColor("#5D1F91"), Color.parseColor("#FF0000"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabLayout.getTabAt(tab.getPosition()).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
                tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#0d0233"));
                tabLayout.setSelectedTabIndicatorHeight((int) (3 * getResources().getDisplayMetrics().density));
                tabLayout.setTabTextColors(Color.parseColor("#5D1F91"), Color.parseColor("#0d0233"));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabLayout.getTabAt(tab.getPosition()).getIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tabLayout.getTabAt(tab.getPosition()).getIcon().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_IN);
                tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#5D1F91"));
                tabLayout.setSelectedTabIndicatorHeight((int) (3 * getResources().getDisplayMetrics().density));
                tabLayout.setTabTextColors(Color.parseColor("#5D1F91"), Color.parseColor("#FF0000"));
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 1000 > System.currentTimeMillis()) {
            finishAffinity();
            super.onBackPressed();
        } else {
            Constants.showToast(this, "Press once again to exit!");
        }
        back_pressed = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishAffinity();
    }
}