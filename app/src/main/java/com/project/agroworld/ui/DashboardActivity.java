package com.project.agroworld.ui;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.project.agroworld.R;
import com.project.agroworld.adapter.FragmentAdapter;
import com.project.agroworld.fragments.EducationFragment;
import com.project.agroworld.fragments.HomeFragment;
import com.project.agroworld.fragments.NewsFragment;
import com.project.agroworld.fragments.ProfileFragment;
import com.project.agroworld.fragments.ShoppingFragment;
import com.project.agroworld.fragments.TransportFragment;
import com.project.agroworld.utils.Permissions;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Permissions.checkConnection(this);
        Permissions.isGpsEnable(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Agro World");

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        fragments = new ArrayList<>();

        fragments.add(new HomeFragment());
        fragments.add(new ShoppingFragment());
        fragments.add(new EducationFragment());
        fragments.add(new TransportFragment());
        fragments.add(new NewsFragment());
        fragments.add(new ProfileFragment());

        FragmentAdapter pagerAdapter = new FragmentAdapter(getSupportFragmentManager(), getApplicationContext(), fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setEnabled(false);
        viewPager.setNestedScrollingEnabled(false);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_home_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_shop_two_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_menu_book_24);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_baseline_emoji_transportation_24);
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
                tabLayout.getTabAt(tab.getPosition()).getIcon().setColorFilter(getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.SRC_IN);
                tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#5D1F91"));
                tabLayout.setSelectedTabIndicatorHeight((int) (3 * getResources().getDisplayMetrics().density));
                tabLayout.setTabTextColors(Color.parseColor("#5D1F91"), Color.parseColor("#FF0000"));
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
    public void onBackPressed() {
        super.onBackPressed();

    }
}