package me.endeal.patron.listeners;

import android.app.Dialog;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.List;

import me.endeal.patron.dialogs.CardDialog;
import me.endeal.patron.model.*;
import me.endeal.patron.system.Globals;

public class FunderButtonListener implements OnClickListener
{
    @Override
    public void onClick(final View view)
    {
        final PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.START);

        // Add all funders
        if (Globals.getPatron().getFunders() != null)
        for (int i = 0; i < Globals.getPatron().getFunders().size(); i++)
        {
            Funder funder = Globals.getPatron().getFunders().get(i);
            popup.getMenu().add(Menu.NONE, i + 1, Menu.NONE, funder.getBrand() + " " + funder.getLast4());
        }
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
                else
                {
                    int i = menuItem.getItemId() - 1;
                    Funder funder = Globals.getPatron().getFunders().get(i);
                    Globals.getOrder().setFunder(funder);
                    Button button = (Button)view;
                    button.setText(funder.toString());
                }
                return true;
            }
        });
        popup.show();
    }
}
