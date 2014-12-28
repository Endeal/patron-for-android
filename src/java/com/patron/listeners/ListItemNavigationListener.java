package com.patron.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.patron.listeners.DrawerNavigationListener;
import com.patron.main.FlashCodes;
import com.patron.main.FlashHelp;
import com.patron.main.FlashMenu;
import com.patron.main.FlashProfile;
import com.patron.main.FlashSettings;
import com.patron.main.FlashVendors;
import static com.patron.view.NavigationListView.Hierarchy;

public class ListItemNavigationListener implements OnItemClickListener
{
    private DrawerNavigationListener drawerNavigationListener;
    private DrawerLayout drawerLayout;
    private Hierarchy hierarchy;

    public ListItemNavigationListener(DrawerNavigationListener drawerNavigationListener, DrawerLayout drawerLayout, Hierarchy hierarchy)
    {
        this.drawerNavigationListener = drawerNavigationListener;
        this.drawerLayout = drawerLayout;
        this.hierarchy = hierarchy;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (position > 0)
        {
            drawerLayout.closeDrawers();
        }
        position--;
        Intent intent = null;
        Context context = view.getContext();
        switch (position)
        {
            case 0: // Buy
                if (hierarchy != Hierarchy.BUY)
                {
                    intent = new Intent(context, FlashMenu.class);
                }
                break;
            case 1: // Orders
                if (hierarchy != Hierarchy.ORDERS)
                {
                    intent = new Intent(context, FlashCodes.class);
                }
                break;
            case 2: // Profile
                if (hierarchy != Hierarchy.PROFILE)
                {
                    intent = new Intent(context, FlashProfile.class);
                }
                break;
            case 3: // Settings
                if (hierarchy != Hierarchy.SETTINGS)
                {
                    intent = new Intent(context, FlashSettings.class);
                }
                break;
            case 4: // Help
                if (hierarchy != Hierarchy.HELP)
                {
                    intent = new Intent(context, FlashHelp.class);
                }
                break;
        }
        drawerNavigationListener.setIntent(intent);
    }
}
