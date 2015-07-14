package me.endeal.patron.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.endeal.patron.adapters.SelectionAdapter;
import me.endeal.patron.model.Fragment;

import me.endeal.patron.R;

public class FragmentAttributes extends android.support.v4.app.Fragment
{
    private Fragment fragment;

    public FragmentAttributes(Fragment fragment)
    {
        this.fragment = fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_attributes, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.fragmentAttributesRecyclerViewMain);
        final GridLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        SelectionAdapter adapter = new SelectionAdapter(view.getContext(), this.fragment);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }
}
