package com.endeal.patron.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.squareup.picasso.Picasso;

import com.endeal.patron.model.Order;
import com.endeal.patron.model.Locale;
import com.endeal.patron.model.Location;
import com.endeal.patron.model.Retrieval;
import com.endeal.patron.model.Station;
import com.endeal.patron.model.Vendor;
import com.endeal.patron.R;
import com.endeal.patron.system.Globals;
import static com.endeal.patron.model.Retrieval.Method;
import static com.endeal.patron.model.Order.Status;

public class OrderDialog extends Dialog
{
    private Order order;

    public OrderDialog(Context context, Order order)
    {
        super(context);
        this.order = order;
        init();
    }

    public OrderDialog(Context context, int theme)
    {
        super(context, theme);
        init();
    }

    public void init()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_order);

        // Get xml elements
        TextView title = (TextView)findViewById(R.id.dialogOrderTextViewTitle);
        ImageView code = (ImageView)findViewById(R.id.dialogOrderImageViewCode);
        TextView delivery = (TextView)findViewById(R.id.dialogOrderTextViewDelivery);
        TextView service = (TextView)findViewById(R.id.dialogOrderTextViewLocale);
        TextView status = (TextView)findViewById(R.id.dialogOrderTextViewStatus);
        TextView location = (TextView)findViewById(R.id.dialogOrderTextViewLocation);
        ImageButton info = (ImageButton)findViewById(R.id.dialogOrderImageButtonInfo);
        ImageButton share = (ImageButton)findViewById(R.id.dialogOrderImageButtonShare);

        // Set dialog title
        String textTitle = "";
        if (order.getRetrieval().getMethod() == Method.Pickup)
        {
            textTitle = "Pickup at " + order.getRetrieval().getStation().getName();
            if (order.getStatus() == Status.READY)
                code.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(order.getCode()).into(code);
        }
        else if (order.getRetrieval().getMethod() == Method.Delivery)
        {
            textTitle = "Delivery to:";
            delivery.setVisibility(View.VISIBLE);
            delivery.setText(order.getRetrieval().getLocation().toString());
        }
        else if (order.getRetrieval().getMethod() == Method.Service)
        {
            textTitle = "Service at " + order.getRetrieval().getLocale().getName() + ":";
            service.setVisibility(View.VISIBLE);
            service.setText(order.getRetrieval().getLocale().getNumber() + "");
        }
        else
        {
            textTitle = "Self-Served";
            if (order.getStatus() == Status.READY)
                code.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(order.getCode()).into(code);
        }
        title.setText(textTitle);

        // Set status text
        String textStatus = "Your order is " + Order.getStatusText(order.getStatus());
        status.setText(textStatus);

        // Set location text
        location.setText(order.getVendor().getLocation().toString());

        // Set info button
        info.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                new AlertDialog.Builder(view.getContext())
                    .setTitle("Price Breakdown")
                    .setMessage(order.toString())
            /*
                    .setNegativeButton("Request Refund", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Snackbar.make(view.getRootView(), "Refund for order successfully requested", Snackbar.LENGTH_SHORT).show();
                        }
                    })
                    */
                    .setPositiveButton("Done", null).show();
            }
        });

        // Set share button
        share.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view.getRootView(), "Unable to share this order", Snackbar.LENGTH_SHORT).show();
                final PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.END);
                popup.getMenu().add(Menu.NONE, 0, Menu.NONE, "Share on Facebook");
                popup.getMenu().add(Menu.NONE, 1, Menu.NONE, "Share on Twitter");
                popup.getMenu().add(Menu.NONE, 2, Menu.NONE, "Share on Google+");
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        if (menuItem.getItemId() == 0)
                        {
                        }
                        else if (menuItem.getItemId() == 1)
                        {
                        }
                        else if (menuItem.getItemId() == 2)
                        {
                        }
                        popup.dismiss();
                        return true;
                    }
                });
                //popup.show();
            }
        });
    }
}
