package com.android.atpic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.android.atpic.adapter.SlidePagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ExploreActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    PagerAdapter adapter;

    public static String EXTRA_FRAGMENT = "extra_fragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);

        int pos = getIntent().getIntExtra(EXTRA_FRAGMENT, 0);

        adapter = new SlidePagerAdapter(getSupportFragmentManager());
        viewPager.setCurrentItem(pos);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}