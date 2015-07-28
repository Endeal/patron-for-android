package com.endeal.patron.bind;

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
