package me.endeal.patron.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CustomizeFragmentPagerAdapter extends FragmentPagerAdapter
{
    private me.endeal.patron.model.Fragment fragment;

    public CustomizeFragmentPagerAdapter(FragmentManager fragmentManager, me.endeal.patron.model.Fragment fragment)
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
        Fragment fragment;
        if (position == 0)
        {
            fragment = new FragmentAttributes(this.fragment);
            return fragment;
        }
        fragment = new FragmentOptions(this.fragment);
        return fragment;
    }

    @Override
    public int getCount()
    {
        return 2;
    }
}
