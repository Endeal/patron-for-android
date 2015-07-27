package me.endeal.patron.bind;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.List;

import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.model.ApiResult;
import me.endeal.patron.model.Attribute;
import me.endeal.patron.model.Contact;
import me.endeal.patron.model.Fragment;
import me.endeal.patron.model.Option;
import me.endeal.patron.model.Station;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;
import me.endeal.patron.system.ApiExecutor;

public class VendorViewHolder extends RecyclerView.ViewHolder
{
    private View view;
    private TextView title;
    private TextView subtitle;

    public VendorViewHolder(View view)
    {
        super(view);
        this.title = (TextView)view.findViewById(R.id.viewHolderVendorTextViewTitle);
        this.subtitle = (TextView)view.findViewById(R.id.viewHolderVendorTextViewSubtitle);
        this.view = view;
    }

    public void setVendor(final Vendor vendor)
    {
        final Activity activity = (Activity)(view.getContext());
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                final Dialog dialogView = new Dialog(view.getContext());
                dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogView.setContentView(R.layout.dialog_vendor);
                ImageView picture = (ImageView)dialogView.findViewById(R.id.dialogVendorImageViewPicture);
                final ImageButton favorite = (ImageButton)dialogView.findViewById(R.id.dialogVendorImageButtonFavorite);
                ImageButton contact = (ImageButton)dialogView.findViewById(R.id.dialogVendorImageButtonContact);
                ImageButton navigation = (ImageButton)dialogView.findViewById(R.id.dialogVendorImageButtonNavigate);
                TextView title = (TextView)dialogView.findViewById(R.id.dialogVendorTextViewTitle);
                TextView description = (TextView)dialogView.findViewById(R.id.dialogVendorTextViewDescription);
                Button select = (Button)dialogView.findViewById(R.id.dialogVendorButtonSelect);

                // Set picture
                Picasso.with(view.getContext()).load(vendor.getPicture()).into(picture);

                // Set favorite
                if (Globals.getPatron().getVendors().contains(vendor.getId()))
                {
                    favorite.setImageResource(R.drawable.ic_favorite_white_48dp);
                }
                else
                {
                    favorite.setImageResource(R.drawable.ic_favorite_border_white_48dp);
                }
                favorite.setOnClickListener(new OnClickListener() {
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
                contact.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View view)
                    {
                        if (vendor.getContact() == null)
                        {
                            return;
                        }
                        final PopupMenu contactPopup = new PopupMenu(view.getContext(), view, Gravity.END);
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
                                    view.getContext().startActivity(intent);
                                }
                                else if (menuItem.getItemId() == 1)
                                {
                                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", vendor.getContact().getEmail(), null));
                                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Patron customer");
                                    view.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
                                }
                                else if (menuItem.getItemId() == 2)
                                {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(vendor.getContact().getFacebook()));
                                    view.getContext().startActivity(i);
                                }
                                else if (menuItem.getItemId() == 3)
                                {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(vendor.getContact().getTwitter()));
                                    view.getContext().startActivity(i);
                                }
                                else if (menuItem.getItemId() == 4)
                                {
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(vendor.getContact().getGooglePlus()));
                                    view.getContext().startActivity(i);
                                }
                                contactPopup.dismiss();
                                return true;
                            }
                        });
                        contactPopup.show();
                    }
                });

                // Set navigation
                navigation.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        String uri = String.format(Locale.ENGLISH, "geo:%f,%f?q=%s", vendor.getLocation().getLatitude(), vendor.getLocation().getLongitude(), vendor.getLocation().toString());
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        v.getContext().startActivity(intent);
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
                select.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View v)
                    {
                        Globals.setVendor(vendor);
                        activity.finish();
                    }
                });

                // Show dialog
                dialogView.show();
            }
        });
    }

    public TextView getTitle()
    {
        return this.title;
    }

    public TextView getSubtitle()
    {
        return this.subtitle;
    }
}
