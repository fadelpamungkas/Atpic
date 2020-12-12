package com.android.atpic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.atpic.adapter.SlidePagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ExploreActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    PagerAdapter adapter;
    Button btnBack;

    public static String EXTRA_FRAGMENT = "extra_fragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);
        btnBack = findViewById(R.id.iconBack);

        int pos = getIntent().getIntExtra(EXTRA_FRAGMENT, 0);

        adapter = new SlidePagerAdapter(getSupportFragmentManager());
        viewPager.setCurrentItem(pos);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}