package com.endeal.patron.listeners;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.List;

import com.endeal.patron.dialogs.LocationDialog;
import com.endeal.patron.model.Order;
import com.endeal.patron.model.Locale;
import com.endeal.patron.model.Location;
import com.endeal.patron.model.Retrieval;
import com.endeal.patron.model.Station;
import com.endeal.patron.model.Vendor;
import com.endeal.patron.system.Globals;
import static com.endeal.patron.model.Retrieval.Method;

public class RetrievalButtonListener implements OnClickListener
{
    private Button buttonRetrieval;

    public RetrievalButtonListener(Button buttonRetrieval)
    {
        this.buttonRetrieval = buttonRetrieval;
    }

    public void update()
    {
        Order order = Globals.getOrder();
        if (order.getRetrieval().getMethod() == Method.Pickup)
            buttonRetrieval.setText(Globals.getOrder().getRetrieval().getStation().getName());
        else if (order.getRetrieval().getMethod() == Method.Service)
            buttonRetrieval.setText(order.getRetrieval().getLocale().toString());
        else if (order.getRetrieval().getMethod() == Method.Delivery)
        {
            if (order.getRetrieval().getLocation() != null)
                buttonRetrieval.setText(Globals.getOrder().getRetrieval().getLocation().getAddress());
            else
                buttonRetrieval.setText("N/A");
        }
        else
            buttonRetrieval.setText("Self-Serve");
    }

    @Override
    public void onClick(final View view)
    {
        final RetrievalButtonListener listener = this;
        final Button button = (Button)view;
        final Order order = Globals.getOrder();
        if (order.getRetrieval().getMethod() == Method.Pickup)
        {
            final PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.START);
            for (int i = 0; i < order.getVendor().getStations().size(); i++)
            {
                Station station = order.getVendor().getStations().get(i);
                popup.getMenu().add(Menu.NONE, i, Menu.NONE, station.getName());
            }
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem)
                {
                    Station station = order.getVendor().getStations().get(menuItem.getItemId());
                    order.getRetrieval().setStation(station);
                    update();
                    popup.dismiss();
                    return true;
                }
            });
            popup.show();
        }
        else if (order.getRetrieval().getMethod() == Method.Service)
        {
            final PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.START);
            for (int i = 0; i < order.getVendor().getLocales().size(); i++)
            {
                Locale locale = order.getVendor().getLocales().get(i);
                popup.getMenu().add(Menu.NONE, i, Menu.NONE, locale.getName() + " " + locale.getNumber());
            }
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem)
                {
                    Locale locale = order.getVendor().getLocales().get(menuItem.getItemId());
                    order.getRetrieval().setLocale(locale);
                    update();
                    popup.dismiss();
                    return true;
                }
            });
            popup.show();
        }
        else if (order.getRetrieval().getMethod() == Method.Delivery)
        {
            final PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.START);
            int i = 0;
            if (Globals.getPatron() != null && Globals.getPatron().getIdentity() != null &&
                    Globals.getPatron().getIdentity().getLocations() != null)
            for (i = 0; i < Globals.getPatron().getIdentity().getLocations().size(); i++)
            {
                Location location = Globals.getPatron().getIdentity().getLocations().get(i);
                popup.getMenu().add(Menu.NONE, i, Menu.NONE, location.toString());
            }
            popup.getMenu().add(Menu.NONE, i + 1, Menu.NONE, "Add new location...");
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem)
                {
                    int i = menuItem.getItemId();
                    Location location = null;
                    if (Globals.getPatron() != null && Globals.getPatron().getIdentity() != null &&
                        Globals.getPatron().getIdentity().getLocations() != null &&
                        i < Globals.getPatron().getIdentity().getLocations().size())
                    {
                        location = Globals.getPatron().getIdentity().getLocations().get(i);
                        order.getRetrieval().setLocation(location);
                        update();
                    }
                    else
                    {
                        LocationDialog dialog = new LocationDialog(view.getContext(), listener);
                        dialog.show();
                    }
                    popup.dismiss();
                    return true;
                }
            });
            popup.show();
        }
        update();
    }
}
