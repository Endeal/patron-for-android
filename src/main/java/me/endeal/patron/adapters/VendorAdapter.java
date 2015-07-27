package me.endeal.patron.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import me.endeal.patron.bind.VendorViewHolder;
import me.endeal.patron.model.Attribute;
import me.endeal.patron.model.Fragment;
import me.endeal.patron.model.Option;
import me.endeal.patron.model.Location;
import me.endeal.patron.model.Price;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.R;

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
        Vendor vendor = vendors.get(i);
        vendorViewHolder.setVendor(vendor);
        if (vendor.getName() != null)
            vendorViewHolder.getTitle().setText(vendor.getName());
        if (vendor.getLocation() != null && vendor.getLocation().getAddress() != null && vendor.getLocation().getZip() != null)
            vendorViewHolder.getSubtitle().setText(vendor.getLocation().getAddress() + ", " + vendor.getLocation().getZip());
    }

    @Override
    public int getItemCount()
    {
        return (null != vendors ? vendors.size() : 0);
    }
}
