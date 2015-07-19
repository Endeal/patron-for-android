package me.endeal.patron.listeners;

import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import java.util.List;

import me.endeal.patron.model.Order;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.model.Retrieval;
import me.endeal.patron.system.Globals;
import me.endeal.patron.R;
import static me.endeal.patron.model.Retrieval.Method;

public class RetrievalMethodButtonListener implements OnClickListener
{
    private ImageButton imageButton;
    private Order order;
    private RetrievalButtonListener listener;

    public RetrievalMethodButtonListener(ImageButton imageButton, Order order, RetrievalButtonListener listener)
    {
        this.imageButton = imageButton;
        this.order = order;
        this.listener = listener;
    }

    public void update()
    {
        Order order = Globals.getOrder();
        if (order.getRetrieval().getMethod() == Method.Pickup)
            imageButton.setImageDrawable(imageButton.getContext().getDrawable(R.drawable.ic_directions_walk_black_48dp));
        else if (order.getRetrieval().getMethod() == Method.Service)
            imageButton.setImageDrawable(imageButton.getContext().getDrawable(R.drawable.ic_pin_drop_black_48dp));
        else if (order.getRetrieval().getMethod() == Method.Delivery)
            imageButton.setImageDrawable(imageButton.getContext().getDrawable(R.drawable.ic_local_shipping_black_48dp));
        else if (order.getRetrieval().getMethod() == Method.SelfServe)
            imageButton.setImageDrawable(imageButton.getContext().getDrawable(R.drawable.ic_local_grocery_store_black_48dp));
    }

    @Override
    public void onClick(final View view)
    {
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
                }
                else if (menuItem.getItemId() == 1)
                {
                    order.getRetrieval().setMethod(Method.Service);
                    order.getRetrieval().setLocale(order.getVendor().getLocales().get(0));
                }
                else if (menuItem.getItemId() == 2)
                {
                    order.getRetrieval().setMethod(Method.Delivery);
                }
                else
                {
                    order.getRetrieval().setMethod(Method.SelfServe);
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
