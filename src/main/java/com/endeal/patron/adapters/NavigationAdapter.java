package com.endeal.patron.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.endeal.patron.activity.*;
import com.endeal.patron.bind.NavigationViewHolder;
import com.endeal.patron.model.Attribute;
import com.endeal.patron.model.Fragment;
import com.endeal.patron.model.Option;
import com.endeal.patron.model.Location;
import com.endeal.patron.model.Price;
import com.endeal.patron.model.Vendor;
import com.endeal.patron.R;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationViewHolder>
{
    private Context context;
    private String[] screens;

    public NavigationAdapter(Context context)
    {
        this.context = context;
        this.screens = new String[]{"Buy", "Orders", "Vouchers", "Settings"};
    }

    @Override
    public NavigationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_navigation, null);
        NavigationViewHolder viewHolder = new NavigationViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NavigationViewHolder navigationViewHolder, final int i)
    {
        final Activity activity = (Activity)navigationViewHolder.itemView.getContext();
        navigationViewHolder.getTitle().setText(screens[i]);
        navigationViewHolder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (i == 0) // Buy
                {
                    Intent intent = new Intent(activity.getApplicationContext(), MenuActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
                else if (i == 1) // Orders
                {
                    Intent intent = new Intent(activity.getApplicationContext(), OrdersActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
                else if (i == 2) // Vouchers
                {
                    Intent intent = new Intent(activity.getApplicationContext(), VouchersActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
                else // Settings
                {
                    Intent intent = new Intent(activity.getApplicationContext(), SettingsActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return (null != screens ? screens.length : 0);
    }
}
