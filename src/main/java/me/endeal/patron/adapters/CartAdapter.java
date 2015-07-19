package me.endeal.patron.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import me.endeal.patron.bind.CartViewHolder;
import me.endeal.patron.model.Attribute;
import me.endeal.patron.model.Fragment;
import me.endeal.patron.model.Option;
import me.endeal.patron.model.Location;
import me.endeal.patron.model.Price;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.system.Globals;
import me.endeal.patron.R;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>
{
    private Context context;
    private List<Fragment> fragments;

    public CartAdapter(Context context, List<Fragment> fragments)
    {
        this.context = context;
        this.fragments = fragments;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_cart, null);
        CartViewHolder viewHolder = new CartViewHolder(this, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CartViewHolder cartViewHolder, int i)
    {
        Fragment fragment = fragments.get(i);
        cartViewHolder.setFragment(fragment, i);
        cartViewHolder.getTitle().setText(fragment.toString());
        cartViewHolder.getSubtitle().setText(fragment.getPrice().toString());
    }

    @Override
    public int getItemCount()
    {
        return (null != fragments ? fragments.size() : 0);
    }
}
