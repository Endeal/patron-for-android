package com.patron.listeners;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.net.URI;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.felipecsl.quickreturn.library.AbsListViewQuickReturnAttacher;
import com.felipecsl.quickreturn.library.QuickReturnAttacher;
import com.felipecsl.quickreturn.library.widget.QuickReturnAdapter;
import com.felipecsl.quickreturn.library.widget.QuickReturnTargetView;

import com.patron.bind.ProductBinder;
import com.patron.listeners.OnApiExecutedListener;
import com.patron.listeners.OnMenuRefreshListener;
import com.patron.main.FlashMenu;
import com.patron.model.Category;
import com.patron.model.Fragment;
import com.patron.model.Item;
import com.patron.model.Supplement;
import com.patron.model.Vendor;
import com.patron.model.Attribute;
import com.patron.model.Selection;
import com.patron.model.Option;
import com.patron.R;
import com.patron.system.Globals;
import com.patron.view.ButtonCategory;
import com.patron.view.ButtonFilter;

public class OnMenuRefreshListener implements OnApiExecutedListener
{
    private FlashMenu activity;
    private SwipeRefreshLayout swipeRefreshLayoutItems;
    private ListView listMenu;
    private LinearLayout linearLayout;
    private Button buttonSelectVendor;
    private List<ButtonFilter> filters;

    public OnMenuRefreshListener(SwipeRefreshLayout swipeRefreshLayoutItems,
            ListView listMenu, LinearLayout linearLayout, Button buttonSelectVendor)
    {
        this.activity = (FlashMenu)swipeRefreshLayoutItems.getContext();
        this.swipeRefreshLayoutItems = swipeRefreshLayoutItems;
        this.listMenu = listMenu;
        this.linearLayout = linearLayout;
        this.buttonSelectVendor = buttonSelectVendor;
        this.filters = new ArrayList<ButtonFilter>();
    }

    @Override
    public void onExecuted()
    {
        // Set list of items to a list of drinks.
        if (Globals.getVendor() == null || Globals.getVendor().getFilteredItems() == null)
        {
            Toast.makeText(activity, "An error has occurred in fetching the vendor.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (Globals.getVendor().getItems().size() <= 0)
        {
            Toast.makeText(activity, "No items for this vendor.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (Globals.getVendor().getFilteredItems().size() <= 0)
        {
            //Toast.makeText(activity, "No items for this filter.", Toast.LENGTH_SHORT).show();
        }

        List<Map<String, Fragment>> products = new ArrayList<Map<String, Fragment>>();
        String[] from = {"name",
                "price",
                "supplements",
                "toggleButtonFavorite",
                "layout"};
        int[] to = {R.id.productListItemTextName,
                R.id.productListItemTextPrice,
                R.id.productListItemButtonSupplements,
                R.id.productListItemToggleButtonFavorite,
                R.id.productListItemLayout};

        // If the menu was refreshed, apply any filters
        if (swipeRefreshLayoutItems.isRefreshing())
        {
          for (int i = 0; i < filters.size(); i++)
          {
            ButtonFilter filter = filters.get(i);
            if (filter.getChecked())
            {
              filter.setChecked(true);
            }
          }
        }

        List<Fragment> fragments = new ArrayList<Fragment>();
        for (int i = 0; i < Globals.getVendor().getFilteredItems().size(); i++)
        {
            Map<String, Fragment> mapping = new HashMap<String, Fragment>();

            // Create default fragment
            Item item = Globals.getVendor().getFilteredItems().get(i);
            List<Selection> selections = new ArrayList<Selection>();
            if (item.getAttributes() != null && item.getAttributes().size() > 0)
            for (int j = 0; j < item.getAttributes().size(); j++)
            {
              Attribute attribute = item.getAttributes().get(j);
              if (attribute.getOptions() != null && attribute.getOptions().size() > 0)
              {
                Option option = attribute.getOptions().get(0);
                Selection selection = new Selection(attribute, option);
                selections.add(selection);
              }
            }
            List<Supplement> supplements = new ArrayList<Supplement>();
            Fragment fragment = new Fragment("", item, selections, supplements, 1);
            fragments.add(fragment);

            // Create Mappings
            mapping.put("name", fragment);
            mapping.put("price", fragment);
            mapping.put("supplements", fragment);
            mapping.put("toggleButtonFavorite", fragment);
            mapping.put("layout", fragment);
            products.add(mapping);
        }
        Globals.setFragments(fragments);
        SimpleAdapter adapter = new SimpleAdapter(activity, products, R.layout.list_item_product, from, to);
        adapter.setViewBinder(new ProductBinder());

        // Go over the views.
        // Go over the categories for each view.
        // If the categoryId matches the view's categoryId...
        // We know the view should exist.
        // If no categoryId matches the view's categoryId...
        // We know the view should not exist.
        boolean buttonShouldExist = false;
        for(int i = 0; i < linearLayout.getChildCount(); i++)
        {
          ButtonFilter view = (ButtonFilter)linearLayout.getChildAt(i);
          for (int j = 0; j < Globals.getCategories().size(); j++)
          {
            if (Globals.getCategories() != null && !Globals.getCategories().isEmpty())
            {
              Category category = Globals.getCategories().get(j);
              if (view.getTag() != null && view.getTag().toString().equals(category.getId()))
              {
                buttonShouldExist = true;
              }
            }
          }
          if (!buttonShouldExist && view.getId() != R.id.menuButtonSearch && view.getId() != R.id.menuButtonFavorites)
          {
            filters.remove(view);
            linearLayout.removeView(view);
          }
        }

        // Go over each category.
        // For each category go over every view.
        // If the view has an id equal to the category's id...
        // We know it has already been added.
        // If no view has an id equal to the category's id...
        // We know it should be added.
        boolean viewAdded = false;
        for (int i = 0; i < Globals.getCategories().size(); i++)
        {
          Category category = Globals.getCategories().get(i);
          for (int j = 0; j < linearLayout.getChildCount(); j++)
          {
            View view = linearLayout.getChildAt(j);
            if (view.getTag() != null && view.getTag().toString().equals(category.getId()))
            {
              viewAdded = true;
            }
          }
          if (!viewAdded)
          {
            ButtonCategory button = new ButtonCategory(activity, category, this);
            filters.add(button);
            linearLayout.addView(button);
          }
        }

        // Quick Return
        int space = (int)Globals.convertDpToPixel(49, activity);
        listMenu.setAdapter(new QuickReturnAdapter(adapter));
        QuickReturnAttacher quickReturnAttacher = QuickReturnAttacher.forView((listMenu));
        QuickReturnTargetView targetView = quickReturnAttacher.addTargetView(buttonSelectVendor, QuickReturnTargetView.POSITION_TOP, space);
        targetView.setAnimatedTransition(true);
        final AbsListViewQuickReturnAttacher attacher = (AbsListViewQuickReturnAttacher) quickReturnAttacher;
        attacher.setOnItemClickListener(new ListItemMenuAddListener());
        //targetView.setPosition(QuickReturnTargetView.POSITION_BOTTOM);
    }
}
