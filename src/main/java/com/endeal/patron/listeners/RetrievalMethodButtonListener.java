package com.endeal.patron.listeners;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.List;

import com.endeal.patron.model.Identity;
import com.endeal.patron.model.Locale;
import com.endeal.patron.model.Location;
import com.endeal.patron.model.Order;
import com.endeal.patron.model.Retrieval;
import com.endeal.patron.model.Station;
import com.endeal.patron.model.Vendor;
import com.endeal.patron.system.Globals;
import com.endeal.patron.R;
import static com.endeal.patron.model.Retrieval.Method;

public class RetrievalMethodButtonListener implements OnClickListener
{
    private ImageButton imageButton;
    private Button buttonTotal;
    private Order order;
    private RetrievalButtonListener listener;

    public RetrievalMethodButtonListener(ImageButton imageButton, Button buttonTotal, Order order, RetrievalButtonListener listener)
    {
        this.imageButton = imageButton;
        this.buttonTotal = buttonTotal;
        this.order = order;
        this.listener = listener;
    }

    public void update()
    {
        Order order = Globals.getOrder();
        buttonTotal.setText("Total: " + order.getTotalPrice().toString());
        if (order.getRetrieval().getMethod() == Method.Pickup)
            imageButton.setImageDrawable(ContextCompat.getDrawable(imageButton.getContext(), R.drawable.ic_directions_walk_black_48dp));
        else if (order.getRetrieval().getMethod() == Method.Service)
            imageButton.setImageDrawable(ContextCompat.getDrawable(imageButton.getContext(), R.drawable.ic_pin_drop_black_48dp));
        else if (order.getRetrieval().getMethod() == Method.Delivery)
            imageButton.setImageDrawable(ContextCompat.getDrawable(imageButton.getContext(), R.drawable.ic_local_shipping_black_48dp));
        else if (order.getRetrieval().getMethod() == Method.SelfServe)
            imageButton.setImageDrawable(ContextCompat.getDrawable(imageButton.getContext(), R.drawable.ic_local_grocery_store_black_48dp));
    }

    @Override
    public void onClick(final View view)
    {
        view.clearAnimation();
        final PopupMenu methodPopup = new PopupMenu(view.getContext(), view, Gravity.START);
        if (order.getVendor().getStations() != null && order.getVendor().getStations().size() > 0)
            methodPopup.getMenu().add(Menu.NONE, 0, Menu.NONE, "Pickup");
        if (order.getVendor().getLocales() != null && order.getVendor().getLocales().size() > 0)
            methodPopup.getMenu().add(Menu.NONE, 1, Menu.NONE, "Service");
        if (order.getVendor().getRange() > 0.0)
            methodPopup.getMenu().add(Menu.NONE, 2, Menu.NONE, "Delivery");
        if (order.getVendor().getSelfServe())
            methodPopup.getMenu().add(Menu.NONE, 3, Menu.NONE, "Self-Serve");
        methodPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                Order order = Globals.getOrder();
                if (menuItem.getItemId() == 0)
                {
                    order.getRetrieval().setMethod(Method.Pickup);
                    order.getRetrieval().setStation(order.getVendor().getStations().get(0));
                    order.getRetrieval().setLocale(null);
                    order.getRetrieval().setLocation(null);
                }
                else if (menuItem.getItemId() == 1)
                {
                    order.getRetrieval().setMethod(Method.Service);
                    order.getRetrieval().setLocale(order.getVendor().getLocales().get(0));
                    order.getRetrieval().setStation(null);
                    order.getRetrieval().setLocation(null);
                }
                else if (menuItem.getItemId() == 2)
                {
                    order.getRetrieval().setMethod(Method.Delivery);
                    List<Location> locations = Globals.getPatron().getIdentity().getLocations();
                    if (locations != null && locations.size() > 0)
                    {
                        order.getRetrieval().setLocation(locations.get(0));
                    }
                    order.getRetrieval().setStation(null);
                    order.getRetrieval().setLocale(null);
                }
                else
                {
                    order.getRetrieval().setMethod(Method.SelfServe);
                    order.getRetrieval().setStation(null);
                    order.getRetrieval().setLocale(null);
                    order.getRetrieval().setLocation(null);
                }
                update();
                listener.update();
                methodPopup.dismiss();
                return true;
            }
        });
        methodPopup.show();
    }
}
