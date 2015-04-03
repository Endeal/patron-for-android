package me.endeal.patron.listeners;

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
import android.widget.Toast;

import me.endeal.patron.listeners.DrawerNavigationListener;
import me.endeal.patron.main.FlashOrders;
import me.endeal.patron.main.FlashMenu;
import me.endeal.patron.main.FlashVouchers;
import me.endeal.patron.main.FlashSettings;
import me.endeal.patron.main.FlashVendors;
import static me.endeal.patron.view.NavigationListView.Hierarchy;

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
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                break;
            case 1: // Orders
                if (hierarchy != Hierarchy.ORDERS)
                {
                    intent = new Intent(context, FlashOrders.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                break;
            case 2: // Vouchers
                Toast.makeText(context, "You do not have any vouchers", Toast.LENGTH_SHORT).show();
                /*
                if (hierarchy != Hierarchy.VOUCHERS)
                {
                    intent = new Intent(context, FlashVouchers.class);
                }
                */
                break;
            default:
                if (hierarchy != Hierarchy.SETTINGS)
                {
                    intent = new Intent(context, FlashSettings.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                break;
        }
        drawerNavigationListener.setIntent(intent);
    }
}
