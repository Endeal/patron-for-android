package com.endeal.patron.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.endeal.patron.bind.SelectionViewHolder;
import com.endeal.patron.model.Attribute;
import com.endeal.patron.model.Fragment;
import com.endeal.patron.model.Option;
import com.endeal.patron.model.Price;
import com.endeal.patron.model.Selection;
import com.endeal.patron.R;

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
        SelectionViewHolder viewHolder = new SelectionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final SelectionViewHolder selectionViewHolder, int i)
    {
        final Selection selection = fragment.getSelections().get(i);
        selectionViewHolder.getTitle().setText(selection.getAttribute().getName());
        Option option = selection.getOption();
        selectionViewHolder.getSubtitle().setText(option.getName() + " (" + option.getPrice().toString() + ")");
        selectionViewHolder.itemView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                final PopupMenu popup = new PopupMenu(view.getContext(), view);
                for (int i = 0; i < selection.getAttribute().getOptions().size(); i++)
                {
                    Option option = selection.getAttribute().getOptions().get(i);
                    popup.getMenu().add(Menu.NONE, i, Menu.NONE, option.getName() + " (" + option.getPrice().toString() + ")");
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        Option option = selection.getAttribute().getOptions().get(menuItem.getItemId());
                        selection.setOption(option);
                        selectionViewHolder.getSubtitle().setText(option.getName() + " (" + option.getPrice().toString() + ")");
                        popup.dismiss();
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        List<Selection> selections = fragment.getSelections();
        return (null != selections ? selections.size() : 0);
    }
}
