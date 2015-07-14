package me.endeal.patron.listeners;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
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

import me.endeal.patron.adapters.FragmentAdapter;
import me.endeal.patron.bind.ProductBinder;
import me.endeal.patron.decor.GridSpacingItemDecoration;
import me.endeal.patron.main.FlashMenu;
import me.endeal.patron.model.Category;
import me.endeal.patron.model.Fragment;
import me.endeal.patron.model.Item;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.model.Attribute;
import me.endeal.patron.model.Selection;
import me.endeal.patron.model.Option;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;
import me.endeal.patron.view.ButtonCategory;
import me.endeal.patron.view.ButtonFilter;

public class OnMenuRefreshListener implements OnApiExecutedListener
{
    private FlashMenu activity;
    private SwipeRefreshLayout swipeRefreshLayoutItems;
    private RecyclerView recyclerView;

    public OnMenuRefreshListener(SwipeRefreshLayout swipeRefreshLayoutItems, RecyclerView recyclerView)
    {
        this.activity = (FlashMenu)swipeRefreshLayoutItems.getContext();
        this.swipeRefreshLayoutItems = swipeRefreshLayoutItems;
        this.recyclerView = recyclerView;
    }

    @Override
    public void onExecuted()
    {
        System.out.println("REFRESH MENU");
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

        // If the menu was refreshed, apply any filters
        if (swipeRefreshLayoutItems.isRefreshing())
        {
            /*
          for (int i = 0; i < filters.size(); i++)
          {
            ButtonFilter filter = filters.get(i);
            if (filter.getChecked())
            {
              filter.setChecked(true);
            }
          }
          */
        }

        List<Fragment> fragments = new ArrayList<Fragment>();
        for (int i = 0; i < Globals.getVendor().getFilteredItems().size(); i++)
        {
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
            List<Option> options = new ArrayList<Option>();
            Fragment fragment = new Fragment("", item, options, selections, 1);
            fragments.add(fragment);
        }
        Globals.setFragments(fragments);
        FragmentAdapter adapter = new FragmentAdapter(recyclerView.getContext(), fragments);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 20, true));
        adapter.notifyDataSetChanged();
        //Globals.filterCategories(Globals.getVendor().getItems());

        // Go over the views.
        // Go over the categories for each view.
        // If the categoryId matches the view's categoryId...
        // We know the view should exist.
        // If no categoryId matches the view's categoryId...
        // We know the view should not exist.
        /*
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
        */
    }
}
