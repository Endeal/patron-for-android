package me.endeal.patron.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import me.endeal.patron.R;

import org.joda.time.DateTime;

public class CardDialog extends Dialog
{
    public CardDialog(Context context)
    {
        super(context);
        init();
    }

    public CardDialog(Context context, int theme)
    {
        super(context, theme);
        init();
    }

    public void init()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_card);
        EditText name = (EditText)findViewById(R.id.dialogCardEditTextName);
        EditText number = (EditText)findViewById(R.id.dialogCardEditTextName);
        EditText cvv = (EditText)findViewById(R.id.dialogCardEditTextName);
        final Button month = (Button)findViewById(R.id.dialogCardButtonMonth);
        final Button year = (Button)findViewById(R.id.dialogCardButtonYear);
        final Button submit = (Button)findViewById(R.id.dialogCardButtonSubmit);

        // Set month
        final DateTime currentTime = new DateTime();
        month.setText(currentTime.getMonthOfYear() + "");
        month.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                final PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.END);
                for (int i = 1; i < 12; i++)
                {
                    popup.getMenu().add(Menu.NONE, i, Menu.NONE, i + "");
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        month.setText(menuItem.getItemId() + "");
                        popup.dismiss();
                        return true;
                    }
                });
                popup.show();
            }
        });

        // Set year
        year.setText(currentTime.getYear() + "");
        year.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                final PopupMenu popup = new PopupMenu(view.getContext(), view, Gravity.END);
                for (int i = currentTime.getYear(); i < currentTime.getYear() + 10; i++)
                {
                    popup.getMenu().add(Menu.NONE, i, Menu.NONE, i + "");
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        year.setText(menuItem.getItemId() + "");
                        popup.dismiss();
                        return true;
                    }
                });
                popup.show();
            }
        });

        // Submit button
        final Dialog dialog = this;
        submit.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });
    }
}
