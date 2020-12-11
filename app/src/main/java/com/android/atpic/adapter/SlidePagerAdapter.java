package com.android.atpic.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.android.atpic.FontsFragment;
import com.android.atpic.IconsFragment;
import com.android.atpic.PhotoFragment;
import com.android.atpic.TemplateFragment;

public class SlidePagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] childFragments;

    public SlidePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        childFragments = new Fragment[]{
                new TemplateFragment(),
                new PhotoFragment(),
                new FontsFragment(),
                new IconsFragment()
        };
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return childFragments[position];
    }

    @Override
    public int getCount() {
        return childFragments.length;
    }
}
