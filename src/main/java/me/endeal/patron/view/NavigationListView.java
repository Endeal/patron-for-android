/**
A listview specifically for navigation.
Needs to understand that it has a specific number of items,
they retain checked state, they are colored for said state,
and clicking on any given one will start a new activity unless
the activity calling upon it is within its hierarchy.
*/

package me.endeal.patron.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.util.AttributeSet;

import com.squareup.picasso.Picasso;

import me.endeal.patron.bind.NavigationBinder;
import me.endeal.patron.listeners.DrawerNavigationListener;
import me.endeal.patron.listeners.ListItemNavigationListener;
import me.endeal.patron.lists.ListFonts;
import me.endeal.patron.model.Identity;
import me.endeal.patron.model.Patron;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;

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
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setBackgroundResource(R.color.background);

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

        // Add Footer
        LinearLayout linearLayoutFooter = new LinearLayout(getContext());
        linearLayoutFooter.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        RelativeLayout relativeLayoutFooter = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relativeLayoutFooter.setLayoutParams(params);
        TextView  textViewFooter = new TextView(getContext());
        textViewFooter.setTextAppearance(getContext(), R.style.subtitle);
        int margin = (int)Globals.convertPixelsToDp(10, getContext());
        LinearLayout.LayoutParams textViewFooterLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textViewFooterLayoutParams.setMargins(margin, margin, margin, margin);
        textViewFooter.setLayoutParams(textViewFooterLayoutParams);
        ImageView imageViewVendor = new ImageView(getContext());
        imageViewVendor.setLayoutParams(params);
        imageViewVendor.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (Globals.getVendor() != null)
        {
            textViewFooter.setText(Globals.getVendor().getName());
            RelativeLayout.LayoutParams textViewFooterParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            textViewFooterParams.addRule(RelativeLayout.ABOVE, imageViewVendor.getId());
            textViewFooter.setLayoutParams(textViewFooterParams);
            Picasso.with(getContext()).load(Globals.getVendor().getPicture()).into(imageViewVendor);
        }
        relativeLayoutFooter.addView(textViewFooter);
        relativeLayoutFooter.addView(imageViewVendor);
        linearLayoutFooter.addView(relativeLayoutFooter);
        addFooterView(linearLayoutFooter);

        // Get the reward points for the vendor.
        textViewHeader = new TextView(relativeLayout.getContext());
        textViewHeader.setTextAppearance(getContext(), R.style.label);
        if (Globals.getVendor() != null && Globals.getPatron() != null && Globals.getPatron().getIdentity() != null)
        {
            //int points = Globals.getPoints(Globals.getVendor().getId());
            int points = 0;
            if (points < 0)
            {
                points = 0;
            }
            Identity identity = Globals.getPatron().getIdentity();
            textViewHeader.setText(identity.getFirstName() + " " + identity.getLastName());
        }
        textViewHeader.setBackgroundResource(R.color.background);
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
