package me.endeal.patron.bind;

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

import me.endeal.patron.fragments.CustomizeFragmentPagerAdapter;
import me.endeal.patron.fragments.CustomizeViewPager;
import me.endeal.patron.fragments.FragmentAttributes;
import me.endeal.patron.fragments.FragmentOptions;
import me.endeal.patron.main.FlashMenu;
import me.endeal.patron.model.*;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;
import static me.endeal.patron.model.Order.Status;
import static me.endeal.patron.model.Retrieval.Method;

public class FragmentViewHolder extends RecyclerView.ViewHolder
{
    private View view;
    private ImageView picture;
    private TextView name;
    private TextView price;

    public FragmentViewHolder(View view)
    {
        super(view);
        this.picture = (ImageView) view.findViewById(R.id.viewHolderFragmentImageViewPicture);
        this.name = (TextView) view.findViewById(R.id.viewHolderFragmentTextViewName);
        this.price = (TextView) view.findViewById(R.id.viewHolderFragmentTextViewPrice);
        this.view = view;
    }

    public void setFragment(final Fragment fragment)
    {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final FragmentActivity activity = (FragmentActivity)view.getContext();
                final View rootView = view.getRootView();
                final Dialog dialogView = new Dialog(view.getContext());
                dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogView.setContentView(R.layout.dialog_item);
                ImageView imageViewPicture = (ImageView)dialogView.findViewById(R.id.dialogItemImageViewPicture);
                ImageButton imageButtonFavorite = (ImageButton)dialogView.findViewById(R.id.dialogItemImageButtonFavorite);
                ImageButton imageButtonNutrition = (ImageButton)dialogView.findViewById(R.id.dialogItemImageButtonNutrition);
                Button buttonQuantity = (Button)dialogView.findViewById(R.id.dialogItemButtonQuantity);
                TextView textViewName = (TextView)dialogView.findViewById(R.id.dialogItemTextViewName);
                TextView textViewPrice = (TextView)dialogView.findViewById(R.id.dialogItemTextViewPrice);
                TextView textViewDescription = (TextView)dialogView.findViewById(R.id.dialogItemTextViewDescription);
                Button buttonCustomize = (Button)dialogView.findViewById(R.id.dialogItemButtonCustomize);
                Button buttonAdd = (Button)dialogView.findViewById(R.id.dialogItemButtonAdd);

                // Set components
                Picasso.with(view.getContext()).load(fragment.getItem().getPicture()).into(imageViewPicture);
                textViewName.setText(fragment.getItem().getName());
                textViewPrice.setText(fragment.getItem().getPrice().toString());
                textViewDescription.setText(fragment.getItem().getDescription());

                // Setup favorite
                imageButtonFavorite.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        /*
                        if (Globals.getUser().hasFavoriteItem(fragment.getItem()))
                        {
                            // Remove from user favorites
                            Snackbar.make(view.getRootView(), fragment.getItem().getName() + " removed to favorites", Snackbar.LENGTH_SHORT).show();
                        }
                        else
                        {
                        */
                            // Add to user favorites
                            Snackbar.make(view.getRootView(), fragment.getItem().getName() + " added to favorites", Snackbar.LENGTH_SHORT).show();
                        //}
                    }
                });

                // Setup nutrition
                imageButtonNutrition.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        new AlertDialog.Builder(view.getContext()).setTitle("Nutrition Information").setMessage(fragment.getItem().getNutrition().toString())
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
                buttonQuantity.setOnClickListener(new OnClickListener() {
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
                        doneButton.setOnClickListener(new OnClickListener() {
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

                buttonCustomize.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        customize.show(activity.getSupportFragmentManager(), "Customize");
                    }
                });

                buttonAdd.setOnClickListener(new OnClickListener() {
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
                                Location location = new Location("2773 Wanda Road", "Sandelberg", "CA", "97022", 99.83792, 62.29863);
                                retrieval = new Retrieval(Method.Delivery, null, null, location);
                            }
                            else
                                retrieval = new Retrieval(Method.SelfServe, null, null, null);
                            Order order = new Order("92872", new ArrayList<Fragment>(), new ArrayList<Voucher>(), new Price(0, "USD"),
                                "", retrieval, 1433833200, Status.WAITING, null, Globals.getVendor(),
                                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/41/QR_Code_Example.svg/368px-QR_Code_Example.svg.png");
                            Globals.setOrder(order);
                        }

                        final Fragment clone = (Fragment)Globals.deepClone(fragment);
                        Globals.getOrder().getFragments().add(clone);
                        View coordinatorLayout = rootView.findViewById(R.id.menuCoordinatorLayoutMain);
                        Snackbar.make(coordinatorLayout, clone.getItem().getName() + " added to order", Snackbar.LENGTH_SHORT).show();
                        dialogView.dismiss();
                    }
                });

                dialogView.show();
            }
        });
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
