package com.endeal.patron.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.endeal.patron.bind.VendorViewHolder;
import com.endeal.patron.dialogs.VendorDialog;
import com.endeal.patron.model.Attribute;
import com.endeal.patron.model.Fragment;
import com.endeal.patron.model.Option;
import com.endeal.patron.model.Location;
import com.endeal.patron.model.Price;
import com.endeal.patron.model.Vendor;
import com.endeal.patron.R;

public class VendorAdapter extends RecyclerView.Adapter<VendorViewHolder>
{
    private Context context;
    private List<Vendor> vendors;

    public VendorAdapter(Context context, List<Vendor> vendors)
    {
        this.context = context;
        this.vendors = vendors;
    }

    public void setVendors(List<Vendor> vendors)
    {
        this.vendors = vendors;
    }

    @Override
    public VendorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_vendor, null);
        VendorViewHolder viewHolder = new VendorViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VendorViewHolder vendorViewHolder, int i)
    {
        final Vendor vendor = vendors.get(i);
        final Activity activity = (Activity)context;
        if (vendor.getName() != null)
            vendorViewHolder.getTitle().setText(vendor.getName());
        if (vendor.getLocation() != null && vendor.getLocation().getAddress() != null && vendor.getLocation().getZip() != null)
            vendorViewHolder.getSubtitle().setText(vendor.getLocation().getAddress() + ", " + vendor.getLocation().getZip());
        vendorViewHolder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                VendorDialog dialog = new VendorDialog(activity, vendor);
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return (null != vendors ? vendors.size() : 0);
    }
}
