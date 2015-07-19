package me.endeal.patron.listeners;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.List;

import me.endeal.patron.model.Order;
import me.endeal.patron.model.Locale;
import me.endeal.patron.model.Location;
import me.endeal.patron.model.Retrieval;
import me.endeal.patron.model.Station;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.system.Globals;
import static me.endeal.patron.model.Retrieval.Method;

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
            buttonRetrieval.setText(order.getRetrieval().getLocale().getName() + " " + order.getRetrieval().getLocale().getNumber());
        else if (order.getRetrieval().getMethod() == Method.Delivery)
            buttonRetrieval.setText(Globals.getOrder().getRetrieval().getLocation().getAddress());
        else
            buttonRetrieval.setText("Self-Serve");
    }

    @Override
    public void onClick(View view)
    {
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
                    popup.dismiss();
                    return true;
                }
            });
            popup.show();
        }
        else if (order.getRetrieval().getMethod() == Method.Delivery)
        {
            final PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.START);
            /*
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
                    popup.dismiss();
                    return true;
                }
            });
            */
            popup.show();
        }
        update();
    }
}
