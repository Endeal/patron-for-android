package me.endeal.patron.listeners;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.endeal.patron.adapters.VendorAdapter;
import me.endeal.patron.model.Contact;
import me.endeal.patron.model.Location;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;

public class OnVendorRefreshListener implements OnApiExecutedListener
{
    private SwipeRefreshLayout layout;
    private RecyclerView recycler;

    public OnVendorRefreshListener(SwipeRefreshLayout layout, RecyclerView recycler)
    {
        this.layout = layout;
        this.recycler = recycler;
    }

    @Override
    public void onExecuted()
    {
        if (Globals.getVendors() == null)
            System.out.println("NULL GLOBAL VENDORS");
        if (Globals.getVendors().size() <= 0)
            System.out.println("ZERO GLOBAL VENDORS");
        for (int i = 0; i < Globals.getVendors().size(); i++)
        {
            System.out.println("Vendor to be adapted: " + Globals.getVendors().get(i).getName());
        }
        System.out.println("THAT'S ALL THE VENDORS");
        VendorAdapter adapter = new VendorAdapter(recycler.getContext(), Globals.getVendors());
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
