package me.endeal.patron.listeners;

import android.app.Dialog;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.List;

import me.endeal.patron.dialogs.CardDialog;
import me.endeal.patron.model.*;

public class FunderButtonListener implements OnClickListener
{
    /*
    private Order order;

    public FunderButtonListener(Order order)
    {
        this.order = order;
    }
    */

    @Override
    public void onClick(final View view)
    {
        final PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.START);

        // Add all funders
        //popup.getMenu().add(Menu.NONE, i, Menu.NONE, locale.getName() + " " + locale.getNumber());
        popup.getMenu().add(Menu.NONE, 0, Menu.NONE, "Add new card...");

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                popup.dismiss();
                if (menuItem.getItemId() == 0)
                {
                    Dialog dialog = new CardDialog(view.getContext());
                    dialog.show();
                }
                return true;
            }
        });
        popup.show();
    }
}
