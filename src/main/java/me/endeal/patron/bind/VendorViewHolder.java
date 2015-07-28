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
import android.widget.CheckBox;
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

import java.util.Locale;
import java.util.List;

import com.endeal.patron.listeners.OnApiExecutedListener;
import com.endeal.patron.model.ApiResult;
import com.endeal.patron.model.Attribute;
import com.endeal.patron.model.Contact;
import com.endeal.patron.model.Fragment;
import com.endeal.patron.model.Option;
import com.endeal.patron.model.Station;
import com.endeal.patron.model.Vendor;
import com.endeal.patron.R;
import com.endeal.patron.system.Globals;
import com.endeal.patron.system.ApiExecutor;

public class VendorViewHolder extends RecyclerView.ViewHolder
{
    private View view;
    private TextView title;
    private TextView subtitle;

    public VendorViewHolder(View view)
    {
        super(view);
        this.title = (TextView)view.findViewById(R.id.viewHolderVendorTextViewTitle);
        this.subtitle = (TextView)view.findViewById(R.id.viewHolderVendorTextViewSubtitle);
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
}
