package com.endeal.patron.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
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
import java.util.ArrayList;

import com.endeal.patron.bind.FragmentViewHolder;
import com.endeal.patron.dialogs.FragmentDialog;
import com.endeal.patron.model.*;
import com.endeal.patron.R;
import com.endeal.patron.system.Globals;

public class FragmentAdapter extends RecyclerView.Adapter<FragmentViewHolder>
{
    private List<Item> items;
    private Context context;

    public FragmentAdapter(Context context, List<Item> items)
    {
        this.items = items;
        this.context = context;
    }

    public void setItems(List<Item> items)
    {
        this.items = items;
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
        Item item = items.get(i);

        // Create Default fragment from items
        List<Selection> selections = new ArrayList<Selection>();
        if (item.getAttributes() != null && item.getAttributes().size() > 0)
        for (int j = 0; j < item.getAttributes().size(); j++)
        {
          Attribute attribute = item.getAttributes().get(j);
          if (attribute.getOptions() != null && attribute.getOptions().size() > 0)
          {
            Option option = attribute.getOptions().get(0);
            Selection selection = new Selection(attribute, option);
            selections.add(selection);
          }
        }
        List<Option> options = new ArrayList<Option>();
        final Fragment fragment = new Fragment("", item, options, selections, 1);

        // Bind fragment to view holder
        fragmentViewHolder.getName().setText(fragment.getItem().getName());
        fragmentViewHolder.getPrice().setText(fragment.getItem().getPrice().toString());
        Picasso.with(context).load(fragment.getItem().getPicture()).into(fragmentViewHolder.getPicture());
        fragmentViewHolder.itemView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FragmentDialog dialog = new FragmentDialog((FragmentActivity)context, fragment);
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return (null != items ? items.size() : 0);
    }
}
