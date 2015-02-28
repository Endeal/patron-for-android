package com.patron.listeners;

import android.content.Context;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.patron.bind.CartProductBinder;
import com.patron.model.Fragment;
import com.patron.model.Funder;
import com.patron.model.Order;
import com.patron.R;
import com.patron.system.Globals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnCartRefreshListener implements OnApiExecutedListener
{
    private ListView listCart;
    private OnApiExecutedListener listener;

    public OnCartRefreshListener(ListView listView, OnApiExecutedListener listener)
    {
        this.listCart = listView;
        this.listener = listener;
    }

    @Override
    public void onExecuted()
    {
        List<Map<String, Fragment>> products = new ArrayList<Map<String, Fragment>>();

        String[] from = {"name",
                "price",
                "quantity",
                "categories",
                "buttonRemove",
                "layout"};

        int[] to = {R.id.cartListItemTextName,
                R.id.cartListItemTextPrice,
                R.id.cartListItemSpinnerQuantity,
                R.id.cartListItemTextCategories,
                R.id.cartListItemButtonRemove,
                R.id.cartListItemLayout};

        for (int i = 0; i < Globals.getOrder().getFragments().size(); i++)
        {
            Map<String, Fragment> mapping = new HashMap<String, Fragment>();
            Fragment fragment = Globals.getOrder().getFragments().get(i);
            mapping.put("name", fragment);
            mapping.put("price", fragment);
            mapping.put("quantity", fragment);
            mapping.put("categories", fragment);
            mapping.put("buttonRemove", fragment);
            mapping.put("layout", fragment);
            products.add(mapping);
        }
        SimpleAdapter adapter = new SimpleAdapter(listCart.getContext(), products,
            R.layout.list_item_cart, from, to);
        adapter.setViewBinder(new CartProductBinder(this));
        listCart.setAdapter(adapter);
        listener.onExecuted();
    }
}
