package com.endeal.patron.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import com.endeal.patron.listeners.OnApiExecutedListener;
import com.endeal.patron.lists.ListKeys;
import com.endeal.patron.model.*;
import com.endeal.patron.R;
import com.endeal.patron.system.ApiExecutor;
import com.endeal.patron.system.Globals;

import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;

public class VendorDialog extends Dialog
{
    private Activity activity;
    private Vendor vendor;

    public VendorDialog(Activity activity)
    {
        super(activity);
        this.activity = activity;
        init();
    }

    public VendorDialog(Activity activity, Vendor vendor)
    {
        super(activity);
        this.activity = activity;
        this.vendor = vendor;
        init();
    }

    public VendorDialog(Context context, int theme)
    {
        super(context, theme);
        init();
    }

    public void init()
    {
        final Dialog dialogView = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_vendor);
        final ImageView picture = (ImageView)findViewById(R.id.dialogVendorImageViewPicture);
        final ImageButton favorite = (ImageButton)findViewById(R.id.dialogVendorImageButtonFavorite);
        final ImageButton contact = (ImageButton)findViewById(R.id.dialogVendorImageButtonContact);
        final ImageButton navigation = (ImageButton)findViewById(R.id.dialogVendorImageButtonNavigate);
        final TextView title = (TextView)findViewById(R.id.dialogVendorTextViewTitle);
        final TextView description = (TextView)findViewById(R.id.dialogVendorTextViewDescription);
        final Button select = (Button)findViewById(R.id.dialogVendorButtonSelect);

        // Set picture
        Picasso.with(getContext()).load(vendor.getPicture()).into(picture);

        // Set favorite
        if (Globals.getPatron().getVendors().contains(vendor.getId()))
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
                List<String> favorites = Globals.getPatron().getVendors();
                for (int i = 0; i < favorites.size(); i++)
                {
                    if (favorites.get(i).equals(vendor.getId()))
                    {
                        found = i;
                        break;
                    }
                }
                // Add if not favorited
                if (found == -1)
                {
                    favorite.setImageResource(R.drawable.ic_favorite_white_48dp);
                    favorites.add(vendor.getId());
                }
                // Remove if favorited
                else
                {
                    favorite.setImageResource(R.drawable.ic_favorite_border_white_48dp);
                    favorites.remove(found);
                }
                Globals.getPatron().setVendors(favorites);
                // Update the patron
                ApiExecutor executor = new ApiExecutor();
                executor.updatePatron(Globals.getPatron(), new OnApiExecutedListener() {
                    @Override
                    public void onExecuted(ApiResult result)
                    {
                        if (result.getStatusCode() != 200)
                        {
                            Snackbar.make(view.getRootView(), result.getMessage(), Snackbar.LENGTH_SHORT).show();
                            if (Globals.getPatron().getVendors().contains(vendor.getId()))
                            {
                                favorite.setImageResource(R.drawable.ic_favorite_border_white_48dp);
                                Globals.getPatron().getVendors().remove(vendor.getId());
                            }
                            else
                            {
                                favorite.setImageResource(R.drawable.ic_favorite_white_48dp);
                                Globals.getPatron().getVendors().add(vendor.getId());
                            }
                        }
                        else
                        {
                            if (Globals.getPatron().getVendors().contains(vendor.getId()))
                                Snackbar.make(view.getRootView(), "Vendor successfully added to favorites", Snackbar.LENGTH_SHORT).show();
                            else
                                Snackbar.make(view.getRootView(), "Vendor successfully removed from favorites", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Set contact
        contact.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                if (vendor.getContact() == null)
                {
                    return;
                }
                final PopupMenu contactPopup = new PopupMenu(dialogView.getContext(), contact, Gravity.END);
                if (vendor.getContact().getPhone() != null)
                    contactPopup.getMenu().add(Menu.NONE, 0, Menu.NONE, "Call at " + vendor.getContact().getPhone());
                if (vendor.getContact().getEmail() != null)
                    contactPopup.getMenu().add(Menu.NONE, 1, Menu.NONE, "E-mail at " + vendor.getContact().getEmail());
                if (vendor.getContact().getFacebook() != null)
                    contactPopup.getMenu().add(Menu.NONE, 2, Menu.NONE, "Visit Facebook");
                if (vendor.getContact().getTwitter() != null)
                    contactPopup.getMenu().add(Menu.NONE, 3, Menu.NONE, "Visit Twitter ");
                if (vendor.getContact().getGooglePlus() != null)
                    contactPopup.getMenu().add(Menu.NONE, 4, Menu.NONE, "Visit Google+");
                contactPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        if (menuItem.getItemId() == 0)
                        {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + vendor.getContact().getPhone()));
                            dialogView.getContext().startActivity(intent);
                        }
                        else if (menuItem.getItemId() == 1)
                        {
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", vendor.getContact().getEmail(), null));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Patron customer");
                            dialogView.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
                        }
                        else if (menuItem.getItemId() == 2)
                        {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(vendor.getContact().getFacebook()));
                            dialogView.getContext().startActivity(i);
                        }
                        else if (menuItem.getItemId() == 3)
                        {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(vendor.getContact().getTwitter()));
                            dialogView.getContext().startActivity(i);
                        }
                        else if (menuItem.getItemId() == 4)
                        {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(vendor.getContact().getGooglePlus()));
                            dialogView.getContext().startActivity(i);
                        }
                        contactPopup.dismiss();
                        return true;
                    }
                });
                contactPopup.show();
            }
        });

        // Set navigation
        navigation.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%s", vendor.getLocation().getLatitude(), vendor.getLocation().getLongitude(), vendor.getLocation().toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                view.getContext().startActivity(intent);
            }
        });

        // Set title
        title.setText(vendor.getName());

        // Set description
        String textDelivery = "";
        if (vendor.getRange() > 0.0)
            textDelivery = "\nDelivery up to " + vendor.getRange() + " miles";
        String textOffers = "";
        if (vendor.getStations() != null && vendor.getStations().size() > 0)
            textOffers = textOffers + "\nOffers pickup";
        if (vendor.getRange() > 0.0)
        {
            if (textOffers.equals(""))
                textOffers = "\nOffers delivery";
            else
                textOffers = textOffers + ", delivery";
        }
        if (vendor.getLocales() != null && vendor.getLocales().size() > 0)
        {
            if (textOffers.equals(""))
                textOffers = "\nOffers service";
            else
                textOffers = textOffers + ", service";
        }
        if (vendor.getSelfServe())
        {
            if (textOffers.equals(""))
                textOffers = "\nOffers self-serve";
            else
                textOffers = textOffers + ", self-serve";
        }
        String textFavorites = "\n" + vendor.getFavorites() + " Favorites";
        description.setText(vendor.getLocation().toString() + textDelivery + textOffers + textFavorites);

        // Set select
        select.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                Globals.setVendor(vendor);
                activity.finish();
            }
        });
        if (!vendor.getOpen())
        {
            select.setEnabled(false);
            select.setText("Closed");
        }
    }
}
