package com.android.atpic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.atpic.adapter.SlidePagerAdapter;
import com.android.atpic.model.Product;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ExploreActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    PagerAdapter adapter;
    Button btnBack, btnSearch;

    public static String EXTRA_FRAGMENT = "extra_fragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabLayout);
        btnBack = findViewById(R.id.iconBack);
        btnSearch = findViewById(R.id.search_button1);

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

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExploreActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }
}