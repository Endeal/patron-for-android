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

import me.endeal.patron.bind.SelectionViewHolder;
import me.endeal.patron.model.Attribute;
import me.endeal.patron.model.Fragment;
import me.endeal.patron.model.Option;
import me.endeal.patron.model.Price;
import me.endeal.patron.model.Selection;
import me.endeal.patron.R;

public class SelectionAdapter extends RecyclerView.Adapter<SelectionViewHolder>
{
    private Fragment fragment;
    private Context context;

    public SelectionAdapter(Context context, Fragment fragment)
    {
        this.fragment = fragment;
        this.context = context;
    }

    @Override
    public SelectionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_selection, null);
        Selection selection = fragment.getSelections().get(i);
        SelectionViewHolder viewHolder = new SelectionViewHolder(view, selection);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SelectionViewHolder selectionViewHolder, int i)
    {
        Selection selection = fragment.getSelections().get(i);
        selectionViewHolder.getTitle().setText(selection.getAttribute().getName());
        Option option = selection.getOption();
        selectionViewHolder.getSubtitle().setText(option.getName() + " (" + option.getPrice().toString() + ")");
    }

    @Override
    public int getItemCount()
    {
        List<Selection> selections = fragment.getSelections();
        return (null != selections ? selections.size() : 0);
    }
}
