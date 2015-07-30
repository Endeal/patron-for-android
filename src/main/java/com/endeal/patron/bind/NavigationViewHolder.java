package com.endeal.patron.bind;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.List;

import com.endeal.patron.R;

public class NavigationViewHolder extends RecyclerView.ViewHolder
{
    private View view;
    private TextView title;

    public NavigationViewHolder(View view)
    {
        super(view);
        this.view = view;
        this.title = (TextView)view.findViewById(R.id.viewHolderNavigationTextViewTitle);
    }

    public TextView getTitle()
    {
        return this.title;
    }
}
