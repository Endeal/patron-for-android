package me.endeal.patron.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.endeal.patron.bind.FragmentViewHolder;
import me.endeal.patron.model.*;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;

public class FragmentAdapter extends RecyclerView.Adapter<FragmentViewHolder>
{
    private List<Fragment> fragments;
    private Context context;

    public FragmentAdapter(Context context, List<Fragment> fragments)
    {
        this.fragments = fragments;
        this.context = context;
    }

    public void setFragments(List<Fragment> fragments)
    {
        this.fragments = fragments;
    }

    @Override
    public FragmentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_fragment, null);
        FragmentViewHolder viewHolder = new FragmentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FragmentViewHolder fragmentViewHolder, int i)
    {
        Fragment fragment = Globals.getFragments().get(i);
        fragmentViewHolder.setFragment(fragment);
        fragmentViewHolder.getName().setText(fragment.getItem().getName());
        fragmentViewHolder.getPrice().setText(fragment.getItem().getPrice().toString());
        Picasso.with(context).load(fragment.getItem().getPicture()).into(fragmentViewHolder.getPicture());
    }

    @Override
    public int getItemCount()
    {
        return (null != Globals.getFragments() ? Globals.getFragments().size() : 0);
    }
}
