package com.patron.listeners;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Button;
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
import com.patron.model.Item;
import com.patron.model.Vendor;
import com.patron.R;
import com.patron.system.Globals;

public class OnMenuRefreshListener implements OnApiExecutedListener
{
    private FlashMenu activity;
    private SwipeRefreshLayout swipeRefreshLayoutItems;
    private ListView listMenu;
    private LinearLayout linearLayout;
    private Button buttonSelectVendor;

    public OnMenuRefreshListener(SwipeRefreshLayout swipeRefreshLayoutItems,
            ListView listMenu, LinearLayout linearLayout, Button buttonSelectVendor)
    {
        this.activity = (FlashMenu)swipeRefreshLayoutItems.getContext();
        this.swipeRefreshLayoutItems = swipeRefreshLayoutItems;
        this.listMenu = listMenu;
        this.linearLayout = linearLayout;
        this.buttonSelectVendor = buttonSelectVendor;
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
            Toast.makeText(activity, "No items for this filter.", Toast.LENGTH_SHORT).show();
        }

        List<Map<String, String>> products = new ArrayList<Map<String, String>>();
        String[] from = {"name",
                "price",
                "categories",
                "toggleButtonFavorite",
                "layout"};
        int[] to = {R.id.productListItemTextName,
                R.id.productListItemTextPrice,
                R.id.productListItemTextCategories,
                R.id.productListItemToggleButtonFavorite,
                R.id.productListItemLayout};
        for (int i = 0; i < Globals.getVendor().getFilteredItems().size(); i++)
        {
            Map<String, String> mapping = new HashMap<String, String>();
            Item item = Globals.getVendor().getFilteredItems().get(i);
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            String price = formatter.format(item.getPrice());
            String categoriesText = "";
            for (int j = 0; j < item.getCategories().size(); j++)
            {
                if (!categoriesText.equals(""))
                {
                    categoriesText = categoriesText + "\n";
                }
                categoriesText = categoriesText + item.getCategories().get(j).getName();
            }
            mapping.put("name", item.getName());
            mapping.put("price", price);
            mapping.put("categories", categoriesText);
            mapping.put("toggleButtonFavorite", item.getId());
            mapping.put("layout", item.getId());
            products.add(mapping);
        }
        SimpleAdapter adapter = new SimpleAdapter(activity, products, R.layout.list_item_product, from, to);
        adapter.setViewBinder(new ProductBinder());

        // Quick Return
        int space = (int)Globals.convertDpToPixel(49, activity);
        listMenu.setAdapter(new QuickReturnAdapter(adapter));
        QuickReturnAttacher quickReturnAttacher = QuickReturnAttacher.forView((listMenu));
        QuickReturnTargetView targetView = quickReturnAttacher.addTargetView(buttonSelectVendor, QuickReturnTargetView.POSITION_TOP, space);
        targetView.setAnimatedTransition(true);
        final AbsListViewQuickReturnAttacher attacher = (AbsListViewQuickReturnAttacher) quickReturnAttacher;
        attacher.setOnItemClickListener(new ListItemMenuAddListener());

        // Update the categories
        if (Globals.getCategories() != null && !Globals.getCategories().isEmpty())
        {
            for (int i = 0; i < Globals.getCategories().size(); i++)
            {
                Button button = new Button(linearLayout.getContext());
                button.setBackgroundResource(R.drawable.button_filter_off);
                button.setTextAppearance(activity, R.style.StyleMenuButtonFavorites);
                float width = Globals.convertDpToPixel(60, activity);
                float height = Globals.convertDpToPixel(60, activity);
                LayoutParams params = new LayoutParams((int)width, (int)height);
                params.gravity = Gravity.CENTER;
                params.setMargins(10, 10, 10, 10);
                button.setLayoutParams(params);
                button.setText(Globals.getCategories().get(i).getName());
                button.setOnClickListener(new ButtonCategoriesListener(activity, Globals.getCategories().get(i), this));
                linearLayout.addView(button);
                if (activity.getButtonCategories() == null)
                {
                    activity.setButtonCategories(new ArrayList<Button>());
                }
                activity.getButtonCategories().add(button);
            }
        }
    }
}
