package com.android.atpic.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.atpic.FontsFragment;
import com.android.atpic.IconsFragment;
import com.android.atpic.PhotoFragment;
import com.android.atpic.TemplateFragment;

public class SlidePagerAdapter extends FragmentPagerAdapter {

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

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Template";
        }
        else if (position == 1)
        {
            title = "Photo";
        }
        else if (position == 2)
        {
            title = "Font";
        }else if (position == 3)
        {
            title = "Icon";
        }
        return title;
    }
}
