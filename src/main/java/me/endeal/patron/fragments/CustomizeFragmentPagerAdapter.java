package com.endeal.patron.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CustomizeFragmentPagerAdapter extends FragmentPagerAdapter
{
    private com.endeal.patron.model.Fragment fragment;

    public CustomizeFragmentPagerAdapter(FragmentManager fragmentManager, com.endeal.patron.model.Fragment fragment)
    {
        super(fragmentManager);
        this.fragment = fragment;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        if (position == 0)
            return "Attributes";
        return "Additions";
    }

    @Override
    public Fragment getItem(int position)
    {
        if (position == 0)
        {
            FragmentAttributes fragmentAttributes = new FragmentAttributes();
            fragmentAttributes.setFragment(this.fragment);
            return fragmentAttributes;
        }
        FragmentOptions fragmentOptions = new FragmentOptions();
        fragmentOptions.setFragment(this.fragment);
        return fragmentOptions;
    }

    @Override
    public int getCount()
    {
        return 2;
    }
}
