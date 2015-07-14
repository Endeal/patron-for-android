package me.endeal.patron.bind;

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

import me.endeal.patron.dialogs.DialogOrderInfo;
import me.endeal.patron.main.FlashMenu;
import me.endeal.patron.model.Order;
import me.endeal.patron.model.Locale;
import me.endeal.patron.model.Location;
import me.endeal.patron.model.Retrieval;
import me.endeal.patron.model.Station;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;
import static me.endeal.patron.model.Order.Status;

public class OrderViewHolder extends RecyclerView.ViewHolder
{
    private View view;
    private TextView title;
    private TextView subtitle;
    private ImageView status;
    private ImageView retrieval;

    public OrderViewHolder(View view)
    {
        super(view);
        this.title = (TextView)view.findViewById(R.id.viewHolderOrderTextViewTitle);
        this.subtitle = (TextView)view.findViewById(R.id.viewHolderOrderTextViewSubtitle);
        this.status = (ImageView)view.findViewById(R.id.viewHolderOrderImageViewStatus);
        this.retrieval = (ImageView)view.findViewById(R.id.viewHolderOrderImageViewRetrieval);
        this.view = view;
    }

    public void setOrder(final Order order)
    {
        final Activity activity = (Activity)(view.getContext());
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Dialog dialogView = new Dialog(v.getContext());
                dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogView.setContentView(R.layout.dialog_order);

                // Get xml elements
                TextView title = (TextView)dialogView.findViewById(R.id.dialogOrderTextViewTitle);
                ImageView code = (ImageView)dialogView.findViewById(R.id.dialogOrderImageViewCode);
                TextView delivery = (TextView)dialogView.findViewById(R.id.dialogOrderTextViewDelivery);
                TextView service = (TextView)dialogView.findViewById(R.id.dialogOrderTextViewLocale);
                TextView status = (TextView)dialogView.findViewById(R.id.dialogOrderTextViewStatus);
                TextView location = (TextView)dialogView.findViewById(R.id.dialogOrderTextViewLocation);
                ImageButton info = (ImageButton)dialogView.findViewById(R.id.dialogOrderImageButtonInfo);
                ImageButton share = (ImageButton)dialogView.findViewById(R.id.dialogOrderImageButtonShare);

                // Set dialog title
                String textTitle = "";
                if (order.getRetrieval().getMethod().equals("pickup"))
                {
                    textTitle = "Pickup at " + order.getRetrieval().getStation().getName();
                }
                else if (order.getRetrieval().getMethod().equals("delivery"))
                {
                    textTitle = "Delivery to:";
                }
                else if (order.getRetrieval().getMethod().equals("service"))
                {
                    textTitle = "Service at " + order.getRetrieval().getLocale().getName() + ":";
                }
                else
                {
                    textTitle = "Self-Served";
                }
                title.setText(textTitle);

                // Set code
                if (order.getRetrieval().getMethod().equals("pickup") || order.getRetrieval().getMethod().equals("selfServe"))
                {
                    code.setVisibility(View.VISIBLE);
                    Picasso.with(v.getContext()).load(order.getCode()).into(code);
                }

                // Set delivery text
                if (order.getRetrieval().getMethod().equals("delivery"))
                {
                    delivery.setVisibility(View.VISIBLE);
                    delivery.setText(order.getRetrieval().getLocation().toString());
                }

                // Set locale text
                if (order.getRetrieval().getMethod().equals("service"))
                {
                    service.setVisibility(View.VISIBLE);
                    service.setText(order.getRetrieval().getLocale().getNumber() + "");
                }

                // Set status text
                String textStatus = "Your order is " + Order.getStatusText(order.getStatus());
                status.setText(textStatus);

                // Set location text
                location.setText(order.getVendor().getLocation().toString());

                // Set info button
                info.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Dialog dialog = new DialogOrderInfo(view.getContext(), true, null, null, order);
                        dialog.show();
                    }
                });

                // Set share button
                share.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
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
                        popup.show();
                    }
                });

                // Show dialog
                dialogView.show();
            }
        });
    }

    public TextView getTitle()
    {
        return this.title;
    }

    public TextView getSubtitle()
    {
        return this.subtitle;
    }

    public ImageView getStatus()
    {
        return this.status;
    }

    public ImageView getRetrieval()
    {
        return this.retrieval;
    }
}
