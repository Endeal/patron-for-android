package me.endeal.patron.listeners;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import android.transitions.everywhere.TransitionManager;
import android.transitions.everywhere.Explode;

import me.endeal.patron.adapters.FragmentAdapter;
import me.endeal.patron.model.*;
import me.endeal.patron.system.Globals;
import me.endeal.patron.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterButtonListener implements OnClickListener
{
    private FragmentAdapter adapter;

    public FilterButtonListener(FragmentAdapter adapter)
    {
        this.adapter = adapter;
    }
    @Override
    public void onClick(final View view)
    {
        final PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.RIGHT | Gravity.BOTTOM);
        List<Category> categories = new ArrayList<Category>();
        if (Globals.getCategories() != null)
            categories = Globals.getCategories();
        int i = 0;
        for (i = 0; i < categories.size(); i++)
        {
            Category category = categories.get(i);
            popup.getMenu().add(Menu.NONE, i, Menu.NONE, category.getName());
        }
        popup.getMenu().add(Menu.NONE, i + 1, Menu.NONE, "Favorites");
        popup.getMenu().add(Menu.NONE, i + 2, Menu.NONE, "Search...");
        popup.getMenu().add(Menu.NONE, i + 3, Menu.NONE, "All");

        // Popup Menu Item Selections
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                int position = menuItem.getItemId();
                List<Item> items = new ArrayList<Item>();
                // Selected a category
                if (position <= Globals.getCategories().size() - 1)
                {
                    View rootView = view.getRootView();
                    TextView title = (TextView)rootView.findViewById(R.id.menuTextViewTitle);
                    EditText search = (EditText)rootView.findViewById(R.id.menuEditTextSearch);
                    title.setVisibility(View.VISIBLE);
                    search.setVisibility(View.GONE);
                    search.setText("");
                    Category category = Globals.getCategories().get(position);
                    for (int i = 0; i < Globals.getVendor().getItems().size(); i++)
                    {
                        Item item = Globals.getVendor().getItems().get(i);
                        for (int j = 0; j < item.getCategories().size(); j++)
                        {
                            if (item.getCategories().get(j).getId().equals(category.getId()))
                            {
                                items.add(item);
                                break;
                            }
                        }
                    }
                    adapter.setItems(items);
                }
                // Selected Favorites
                else if (position == Globals.getCategories().size())
                {
                    View rootView = view.getRootView();
                    TextView title = (TextView)rootView.findViewById(R.id.menuTextViewTitle);
                    EditText search = (EditText)rootView.findViewById(R.id.menuEditTextSearch);
                    title.setVisibility(View.VISIBLE);
                    search.setVisibility(View.GONE);
                    search.setText("");
                    for (int i = 0; i < Globals.getVendor().getItems().size(); i++)
                    {
                        Item item = Globals.getVendor().getItems().get(i);
                        if (Globals.getPatron().getItems().contains(item.getId()));
                        {
                            items.add(item);
                        }
                    }
                    adapter.setItems(items);
                }
                // Selected Search
                else if (position == Globals.getCategories().size() + 2)
                {
                    View rootView = view.getRootView();
                    TextView title = (TextView)rootView.findViewById(R.id.menuTextViewTitle);
                    EditText search = (EditText)rootView.findViewById(R.id.menuEditTextSearch);
                    title.setVisibility(View.GONE);
                    search.setVisibility(View.VISIBLE);
                    search.requestFocus();

                    InputMethodManager inputMethodManager = (InputMethodManager)rootView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    search.addTextChangedListener(new TextWatcher() {
                        public void afterTextChanged(Editable s)
                        {
                            if (s == null)
                                return;
                            String term = s.toString();
                            List<Item> items = new ArrayList<Item>();
                            for (int i = 0; i < Globals.getVendor().getItems().size(); i++)
                            {
                                Item item = Globals.getVendor().getItems().get(i);
                                if (item.getName().toLowerCase().contains(term.toLowerCase()))
                                {
                                    items.add(item);
                                }
                            }
                            adapter.setItems(items);
                        }
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        {
                        }
                        public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    });
                }
                // Selected All
                else
                {
                    View rootView = view.getRootView();
                    TextView title = (TextView)rootView.findViewById(R.id.menuTextViewTitle);
                    EditText search = (EditText)rootView.findViewById(R.id.menuEditTextSearch);
                    title.setVisibility(View.VISIBLE);
                    search.setVisibility(View.GONE);
                    search.setText("");
                    adapter.setItems(Globals.getVendor().getItems());
                }
                adapter.notifyDataSetChanged();
                popup.dismiss();
                return true;
            }
        });

        // Show popup
        popup.show();
    }
}
