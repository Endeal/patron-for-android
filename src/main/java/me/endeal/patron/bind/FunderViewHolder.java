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

import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.R;

public class FunderViewHolder extends RecyclerView.ViewHolder
{
    private View view;
    private TextView title;

    public FunderViewHolder(View view)
    {
        super(view);
        this.view = view;
        this.title = (TextView)view.findViewById(R.id.viewHolderFunderTextViewTitle);
    }

    public TextView getTitle()
    {
        return this.title;
    }
}
