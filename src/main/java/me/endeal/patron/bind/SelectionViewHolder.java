package com.endeal.patron.bind;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
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

import com.endeal.patron.model.Attribute;
import com.endeal.patron.model.Option;
import com.endeal.patron.model.Selection;
import com.endeal.patron.R;

public class SelectionViewHolder extends RecyclerView.ViewHolder
{
        private TextView title;
        private TextView subtitle;

        public SelectionViewHolder(View view)
        {
            super(view);
            this.title = (TextView)view.findViewById(R.id.viewHolderSelectionTextViewTitle);
            this.subtitle = (TextView)view.findViewById(R.id.viewHolderSelectionTextViewSubtitle);
        }

        public SelectionViewHolder(View view, final Selection selection)
        {
            super(view);
            this.title = (TextView) view.findViewById(R.id.viewHolderSelectionTextViewTitle);
            this.subtitle = (TextView)view.findViewById(R.id.viewHolderSelectionTextViewSubtitle);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    final PopupMenu popup = new PopupMenu(v.getContext(), v);
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
                            subtitle.setText(option.getName() + " (" + option.getPrice().toString() + ")");
                            popup.dismiss();
                            return true;
                        }
                    });
                    popup.show();
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
