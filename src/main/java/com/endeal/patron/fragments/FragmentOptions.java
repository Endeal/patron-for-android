package com.endeal.patron.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.endeal.patron.adapters.OptionAdapter;
import com.endeal.patron.model.Fragment;
import com.endeal.patron.R;

public class FragmentOptions extends android.support.v4.app.Fragment
{
    private Fragment fragment;

    public FragmentOptions()
    {
        super();
        this.fragment = null;
    }

    public void setFragment(Fragment fragment)
    {
        this.fragment = fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_options, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.fragmentOptionsRecyclerViewMain);
        final GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        OptionAdapter adapter = new OptionAdapter(view.getContext(), this.fragment);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }
}
