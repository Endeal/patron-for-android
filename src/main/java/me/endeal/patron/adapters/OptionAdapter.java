package com.endeal.patron.adapters;

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

import com.endeal.patron.bind.OptionViewHolder;
import com.endeal.patron.model.Attribute;
import com.endeal.patron.model.Fragment;
import com.endeal.patron.model.Option;
import com.endeal.patron.model.Price;
import com.endeal.patron.R;

public class OptionAdapter extends RecyclerView.Adapter<OptionViewHolder>
{
    private Fragment fragment;
    private Context context;

    public OptionAdapter(Context context, Fragment fragment)
    {
        this.fragment = fragment;
        this.context = context;
    }

    @Override
    public OptionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_option, null);
        Option option = fragment.getItem().getOptions().get(i);
        OptionViewHolder viewHolder = new OptionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OptionViewHolder optionViewHolder, int i)
    {
        Option option = fragment.getItem().getOptions().get(i);
        optionViewHolder.setFragment(fragment, option);
        optionViewHolder.getTitle().setText(option.getName());
        optionViewHolder.getSubtitle().setText(option.getPrice().toString());
    }

    @Override
    public int getItemCount()
    {
        List<Option> options = fragment.getItem().getOptions();
        return (null != options ? options.size() : 0);
    }
}
