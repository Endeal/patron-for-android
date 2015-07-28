package com.endeal.patron.bind;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.view.animation.TranslateAnimation;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import com.squareup.picasso.Picasso;
import io.karim.MaterialTabs;

import java.util.ArrayList;
import java.util.List;

import com.endeal.patron.fragments.CustomizeFragmentPagerAdapter;
import com.endeal.patron.fragments.CustomizeViewPager;
import com.endeal.patron.fragments.FragmentAttributes;
import com.endeal.patron.fragments.FragmentOptions;
import com.endeal.patron.listeners.OnApiExecutedListener;
import com.endeal.patron.model.*;
import com.endeal.patron.R;
import com.endeal.patron.system.ApiExecutor;
import com.endeal.patron.system.Globals;
import static com.endeal.patron.model.Order.Status;
import static com.endeal.patron.model.Retrieval.Method;

import org.joda.time.DateTime;

public class FragmentViewHolder extends RecyclerView.ViewHolder
{
    private ImageView picture;
    private TextView name;
    private TextView price;

    public FragmentViewHolder(View view)
    {
        super(view);
        this.picture = (ImageView) view.findViewById(R.id.viewHolderFragmentImageViewPicture);
        this.name = (TextView) view.findViewById(R.id.viewHolderFragmentTextViewName);
        this.price = (TextView) view.findViewById(R.id.viewHolderFragmentTextViewPrice);
    }

    public ImageView getPicture()
    {
        return this.picture;
    }

    public TextView getName()
    {
        return this.name;
    }

    public TextView getPrice()
    {
        return this.price;
    }
}
