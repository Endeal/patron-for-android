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
import android.support.v7.widget.RecyclerView.Adapter;
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

import me.endeal.patron.model.Fragment;
import me.endeal.patron.model.Locale;
import me.endeal.patron.model.Location;
import me.endeal.patron.model.Order;
import me.endeal.patron.model.Retrieval;
import me.endeal.patron.model.Station;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;
import static me.endeal.patron.model.Order.Status;

public class CartViewHolder extends RecyclerView.ViewHolder
{
    private View view;
    private Adapter adapter;
    private TextView title;
    private TextView subtitle;

    public CartViewHolder(Adapter adapter, View view)
    {
        super(view);
        this.view = view;
        this.adapter = adapter;
        this.title = (TextView)view.findViewById(R.id.viewHolderCartTextViewTitle);
        this.subtitle = (TextView)view.findViewById(R.id.viewHolderCartTextViewSubtitle);
    }

    public void setFragment(final Fragment fragment, final int position)
    {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Order order = Globals.getOrder();
                if (order.getFragments() != null)
                {
                    order.getFragments().remove(position);
                    adapter.notifyDataSetChanged();
                }
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
}
