package com.endeal.patron.dialogs;

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

public class FragmentDialog extends Dialog
{
    private FragmentActivity activity;
    private Fragment fragment;

    public FragmentDialog(Context context)
    {
        super(context);
        init();
    }

    public FragmentDialog(FragmentActivity activity, Fragment fragment)
    {
        super(activity);
        this.activity = activity;
        this.fragment = fragment;
        init();
    }

    public FragmentDialog(Context context, int theme)
    {
        super(context, theme);
        init();
    }

    public void init()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_item);
        final Dialog dialog = this;
        final ImageView imageViewPicture = (ImageView)findViewById(R.id.dialogItemImageViewPicture);
        final ImageButton favorite = (ImageButton)findViewById(R.id.dialogItemImageButtonFavorite);
        final ImageButton imageButtonNutrition = (ImageButton)findViewById(R.id.dialogItemImageButtonNutrition);
        final Button buttonQuantity = (Button)findViewById(R.id.dialogItemButtonQuantity);
        final TextView textViewName = (TextView)findViewById(R.id.dialogItemTextViewName);
        final TextView textViewPrice = (TextView)findViewById(R.id.dialogItemTextViewPrice);
        final TextView textViewDescription = (TextView)findViewById(R.id.dialogItemTextViewDescription);
        final Button buttonCustomize = (Button)findViewById(R.id.dialogItemButtonCustomize);
        final Button buttonAdd = (Button)findViewById(R.id.dialogItemButtonAdd);

        // Set components
        Picasso.with(getContext()).load(fragment.getItem().getPicture()).into(imageViewPicture);
        textViewName.setText(fragment.getItem().getName());
        textViewPrice.setText(fragment.getItem().getPrice().toString());
        textViewDescription.setText(fragment.getItem().getDescription());

        // Setup favorite
        final Item item = fragment.getItem();
        if (Globals.getPatron().getItems().contains(item.getId()))
        {
            favorite.setImageResource(R.drawable.ic_favorite_white_48dp);
        }
        else
        {
            favorite.setImageResource(R.drawable.ic_favorite_border_white_48dp);
        }
        favorite.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                int found = -1;
                List<String> favorites = Globals.getPatron().getItems();
                for (int i = 0; i < favorites.size(); i++)
                {
                    if (favorites.get(i).equals(item.getId()))
                    {
                        found = i;
                        break;
                    }
                }
                // Add if not favorited
                if (found == -1)
                {
                    favorite.setImageResource(R.drawable.ic_favorite_white_48dp);
                    favorites.add(item.getId());
                }
                // Remove if favorited
                else
                {
                    favorite.setImageResource(R.drawable.ic_favorite_border_white_48dp);
                    favorites.remove(found);
                }
                Globals.getPatron().setItems(favorites);
                // Update the patron
                ApiExecutor executor = new ApiExecutor();
                executor.updatePatron(Globals.getPatron(), new OnApiExecutedListener() {
                    @Override
                    public void onExecuted(ApiResult result)
                    {
                        if (result.getStatusCode() != 200)
                        {
                            Snackbar.make(view.getRootView(), result.getMessage(), Snackbar.LENGTH_SHORT).show();
                            if (Globals.getPatron().getItems().contains(item.getId()))
                            {
                                favorite.setImageResource(R.drawable.ic_favorite_border_white_48dp);
                                Globals.getPatron().getItems().remove(item.getId());
                            }
                            else
                            {
                                favorite.setImageResource(R.drawable.ic_favorite_white_48dp);
                                Globals.getPatron().getItems().add(item.getId());
                            }
                        }
                        else
                        {
                            if (Globals.getPatron().getItems().contains(item.getId()))
                                Snackbar.make(view.getRootView(), "Item successfully added to favorites", Snackbar.LENGTH_SHORT).show();
                            else
                                Snackbar.make(view.getRootView(), "Item successfully removed from favorites", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Setup nutrition
        imageButtonNutrition.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String message = "No nutrition info provided";
                if (fragment.getItem().getNutrition() != null)
                    message = fragment.getItem().getNutrition().toString();
                new AlertDialog.Builder(view.getContext()).setTitle("Nutrition Information").setMessage(message)
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                     }).show();
            }
        });

        // Setup quantity
        buttonQuantity.setText(fragment.getQuantity() + "");
        buttonQuantity.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                final PopupMenu popup = new PopupMenu(view.getContext(), view);
                for (int i = 0; i < 9; i++)
                {
                   popup.getMenu().add(Menu.NONE, i, Menu.NONE, (i + 1) + "");
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        fragment.setQuantity(menuItem.getItemId() + 1);
                        Button button = (Button)view;
                        button.setText(fragment.getQuantity() + "");
                        popup.dismiss();
                        return true;
                    }
                });
                popup.show();
            }
        });

        // Create customize components
        final DialogFragment customize = new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState)
            {
                Dialog dialog = super.onCreateDialog(savedInstanceState);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                return dialog;
            }

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
            {
                View v = inflater.inflate(R.layout.dialog_customize, container, false);
                CustomizeViewPager pager = (CustomizeViewPager)v.findViewById(R.id.dialogCustomizeViewPagerMain);
                Button doneButton = (Button)v.findViewById(R.id.dialogCustomizeButtonDone);
                doneButton.setOnClickListener(new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        dismiss();
                    }
                });
                pager.setAdapter(new CustomizeFragmentPagerAdapter(getChildFragmentManager(), fragment));
                MaterialTabs tabs = (MaterialTabs)v.findViewById(R.id.dialogCustomizeMaterialTabs);
                tabs.setViewPager(pager);
                return v;
            }
        };

        buttonCustomize.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                customize.show(activity.getSupportFragmentManager(), "Customize");
            }
        });

        buttonAdd.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (Globals.getOrder() == null)
                {
                    Retrieval retrieval = null;
                    if (Globals.getVendor().getStations() != null && Globals.getVendor().getStations().size() > 0)
                        retrieval = new Retrieval(Method.Pickup, Globals.getVendor().getStations().get(0), null, null);
                    else if (Globals.getVendor().getLocales() != null && Globals.getVendor().getLocales().size() > 0)
                        retrieval = new Retrieval(Method.Service, null, Globals.getVendor().getLocales().get(0), null);
                    else if (Globals.getVendor().getRange() > 0)
                    {
                        Location location = null;
                        if (Globals.getPatron().getIdentity().getLocations() != null && Globals.getPatron().getIdentity().getLocations().size() > 0)
                            location = Globals.getPatron().getIdentity().getLocations().get(0);
                        retrieval = new Retrieval(Method.Delivery, null, null, location);
                    }
                    else
                        retrieval = new Retrieval(Method.SelfServe, null, null, null);
                    Funder funder = null;
                    if (Globals.getPatron().getFunders() != null && Globals.getPatron().getFunders().size() > 0)
                        funder = Globals.getPatron().getFunders().get(0);
                    DateTime currentDateTime = new DateTime();
                    Order order = new Order(null, new ArrayList<Fragment>(), new ArrayList<Voucher>(), new Price(0, "USD"),
                        "", retrieval, currentDateTime.getMillis(), Status.PREPARING, funder, Globals.getVendor(), "", Globals.getPatron().getId());
                    Globals.setOrder(order);
                }

                final Fragment clone = (Fragment)Globals.deepClone(fragment);
                Globals.getOrder().getFragments().add(clone);
                View coordinatorLayout = activity.findViewById(R.id.menuCoordinatorLayoutMain);
                Snackbar.make(coordinatorLayout, clone.getItem().getName() + " added to basket", Snackbar.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
