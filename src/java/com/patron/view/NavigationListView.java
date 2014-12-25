/**
A listview specifically for navigation.
Needs to understand that it has a specific number of items,
they retain checked state, they are colored for said state,
and clicking on any given one will start a new activity unless
the activity calling upon it is within its hierarchy.
*/

package com.patron.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.util.AttributeSet;

import com.patron.bind.NavigationBinder;
import com.patron.listeners.DrawerNavigationListener;
import com.patron.listeners.ListItemNavigationListener;
import com.patron.main.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigationListView extends ListView
{
    public static enum Hierarchy
    {
        BUY, ORDERS, PROFILE, SETTINGS, HELP
    }

    public NavigationListView(Context context)
    {
        super(context);
        init();
    }

    public NavigationListView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        init();
    }

    public NavigationListView(Context context, AttributeSet attributeSet, int defStyle)
    {
        super(context, attributeSet, defStyle);
        init();
    }

    public void init()
    {
        // Set up the drawer layout items.
        String[] navigationItems = getContext().getResources().getStringArray(R.array.array_navigation);
        List<Map<String, String>> navigation = new ArrayList<Map<String, String>>();
        String[] from = {"title"};
        int[] to = {R.id.navigationListItemTextTitle};
        for (int i = 0; i < navigationItems.length; i++)
        {
            Map<String, String> mapping = new HashMap<String, String>();
            String navigationItem = navigationItems[i];
            mapping.put("title", navigationItem);
            navigation.add(mapping);
        }
        SimpleAdapter adapter = new SimpleAdapter(getContext(), navigation, R.layout.list_item_navigation, from, to);
        adapter.setViewBinder(new NavigationBinder());
        setAdapter(adapter);

        // Add Header
        TextView textAppTitle = new TextView(getContext());
        textAppTitle.setText("Flash VIP");
        addView(textAppTitle);
    }

    public void setHierarchy(DrawerNavigationListener drawerNavigationListener, DrawerLayout drawerLayout, Hierarchy hierarchy)
    {
        ListItemNavigationListener navigationListItemListener = new ListItemNavigationListener(drawerNavigationListener, drawerLayout, hierarchy);
        setOnItemClickListener(navigationListItemListener);
    }
}
