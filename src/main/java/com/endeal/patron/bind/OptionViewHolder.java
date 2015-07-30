package com.endeal.patron.bind;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.endeal.patron.model.Attribute;
import com.endeal.patron.model.Option;
import com.endeal.patron.model.Fragment;
import com.endeal.patron.R;

public class OptionViewHolder extends RecyclerView.ViewHolder
{
    private View view;
    private TextView title;
    private TextView subtitle;
    private CheckBox checkBox;

    public OptionViewHolder(View view)
    {
        super(view);
        this.title = (TextView)view.findViewById(R.id.viewHolderOptionTextViewTitle);
        this.subtitle = (TextView)view.findViewById(R.id.viewHolderOptionTextViewSubtitle);
        this.checkBox = (CheckBox)view.findViewById(R.id.viewHolderOptionCheckBoxMain);
        this.view = view;
    }

    public OptionViewHolder(View view, final Fragment fragment, final Option option)
    {
        super(view);
        this.title = (TextView) view.findViewById(R.id.viewHolderOptionTextViewTitle);
        this.subtitle = (TextView)view.findViewById(R.id.viewHolderOptionTextViewSubtitle);
        this.checkBox = (CheckBox)view.findViewById(R.id.viewHolderOptionCheckBoxMain);
        this.view = view;
    }

    public void setFragment(final Fragment fragment, final Option option)
    {
        // Check the box if the option is already in the fragment
        boolean found = false;
        for (int i = 0; i < fragment.getOptions().size(); i++)
        {
            if (fragment.getOptions().get(i).getId().equals(option.getId()))
            {
                this.checkBox.setChecked(true);
                found = true;
            }
        }
        if (found != checkBox.isChecked())
            this.checkBox.setChecked(found);

        // Add and remove from fragment on check/uncheck
        AddOptionListener listener = new AddOptionListener(fragment, option, checkBox);
        view.setOnClickListener(listener);
    }

    public TextView getTitle()
    {
        return this.title;
    }

    public TextView getSubtitle()
    {
        return this.subtitle;
    }

    public CheckBox getCheckBox()
    {
        return this.checkBox;
    }

    class AddOptionListener implements OnClickListener
    {
        private Fragment fragment;
        private Option option;
        private CheckBox checkBox;

        public AddOptionListener(Fragment fragment, Option option, CheckBox checkBox)
        {
            this.fragment = fragment;
            this.option = option;
            this.checkBox = checkBox;
        }

        @Override
        public void onClick(View view)
        {
            if (checkBox.isChecked())
            {
                checkBox.setChecked(false);
                for (int i = 0; i < fragment.getOptions().size(); i++)
                {
                    if (fragment.getOptions().get(i).getId().equals(option.getId()))
                    {
                        fragment.getOptions().remove(i);
                    }
                }
                return;
            }
            checkBox.setChecked(true);
            fragment.getOptions().add(option);
        }
    }
}
