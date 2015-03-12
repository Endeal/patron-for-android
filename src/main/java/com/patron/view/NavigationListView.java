/**
A listview specifically for navigation.
Needs to understand that it has a specific number of items,
they retain checked state, they are colored for said state,
and clicking on any given one will start a new activity unless
the activity calling upon it is within its hierarchy.
*/

package com.patron.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.util.AttributeSet;

import com.patron.bind.NavigationBinder;
import com.patron.listeners.DrawerNavigationListener;
import com.patron.listeners.ListItemNavigationListener;
import com.patron.lists.ListFonts;
import com.patron.model.User;
import com.patron.R;
import com.patron.system.Globals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigationListView extends ListView
{
    public static enum Hierarchy
    {
        BUY, ORDERS, VOUCHERS, SETTINGS
    }

    private TextView textViewHeader;

    public NavigationListView(Context context)
    {
        super(context);
        init();
    }

    public NavigationListView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        init();
    }

    public NavigationListView(Context context, AttributeSet attributeSet, int defStyle)
    {
        super(context, attributeSet, defStyle);
        init();
    }

    public void init()
    {
        // Set background of list view.
        setBackgroundColor(Color.parseColor("#000000"));

        // Add Header
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        ImageView imageView = new ImageView(relativeLayout.getContext());
        imageView.setId(1);
        imageView.setImageResource(R.drawable.logo_app);
        final float scale = getResources().getDisplayMetrics().density;
        int width  = (int)(35 * scale);
        int height = (int)(35 * scale);
        int leftMargin = (int)(5 * scale);
        int topMargin = (int)(5 * scale);
        int bottomMargin = (int)(5 * scale);
        RelativeLayout.LayoutParams imageViewLayoutParams = new RelativeLayout.LayoutParams(width, height);
        imageViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        imageViewLayoutParams.setMargins(leftMargin, topMargin, 0, bottomMargin);
        imageView.setLayoutParams(imageViewLayoutParams);
        relativeLayout.setBackgroundColor(Color.parseColor("#000000"));

        // Get the reward points for the vendor.
        textViewHeader = new TextView(relativeLayout.getContext());
        if (Globals.getVendor() != null)
        {
            int points = Globals.getPoints(Globals.getVendor().getId());
            if (points == -1)
            {
                points = 0;
            }
            textViewHeader.setText(Globals.getVendor().getName() + "\n" + points + " Points");
        }
        textViewHeader.setTextColor(Color.parseColor("#FFFFFF"));
        textViewHeader.setBackgroundColor(Color.TRANSPARENT);
        textViewHeader.setGravity(Gravity.CENTER_VERTICAL);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), ListFonts.FONT_MAIN_BOLD);
        textViewHeader.setTypeface(typeface);
        RelativeLayout.LayoutParams textViewLayoutParams = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT, height);
        textViewLayoutParams.addRule(RelativeLayout.RIGHT_OF, imageView.getId());
        textViewLayoutParams.setMargins(leftMargin, topMargin, 0, 0);
        textViewHeader.setLayoutParams(textViewLayoutParams);

        relativeLayout.addView(imageView);
        relativeLayout.addView(textViewHeader);
        addHeaderView(relativeLayout);

        // Set up the drawer layout items.
        String[] navigationItems = getContext().getResources().getStringArray(R.array.array_navigation);
        List<Map<String, String>> navigation = new ArrayList<Map<String, String>>();
        String[] from = {"title"};
        int[] to = {R.id.navigationListItemTextTitle};
        for (int i = 0; i < navigationItems.length; i++)
        {
            Map<String, String> mapping = new HashMap<String, String>();
            String navigationItem = navigationItems[i];
            mapping.put("title", navigationItem);
            navigation.add(mapping);
        }
        SimpleAdapter adapter = new SimpleAdapter(getContext(), navigation, R.layout.list_item_navigation, from, to);
        adapter.setViewBinder(new NavigationBinder());
        setAdapter(adapter);
    }

    public void setHierarchy(DrawerNavigationListener drawerNavigationListener, DrawerLayout drawerLayout, Hierarchy hierarchy)
    {
        ListItemNavigationListener navigationListItemListener = new ListItemNavigationListener(drawerNavigationListener, drawerLayout, hierarchy);
        setOnItemClickListener(navigationListItemListener);
        int i = 0;
        switch (hierarchy)
        {
            case BUY:
                i = 1;
                break;
            case ORDERS:
                i = 2;
                break;
            case VOUCHERS:
                i = 3;
                break;
            case SETTINGS:
                i = 4;
                break;
            default:
                i = 0;
                break;
        }
        if (i > 0 && getChildAt(i) != null)
        {
            getChildAt(i).setBackgroundColor(Color.BLUE);
        }
    }

    public TextView getTextViewHeader()
    {
        return textViewHeader;
    }
}
