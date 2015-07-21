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

import me.endeal.patron.model.Location;
import me.endeal.patron.system.Globals;
import me.endeal.patron.R;

import org.joda.time.DateTime;

public class LocationDialog extends Dialog
{
    public LocationDialog(Context context)
    {
        super(context);
        init();
    }

    public LocationDialog(Context context, int theme)
    {
        super(context, theme);
        init();
    }

    public void init()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_location);
        final EditText address = (EditText)findViewById(R.id.dialogLocationEditTextAddress);
        final EditText city = (EditText)findViewById(R.id.dialogLocationEditTextCity);
        final EditText zip = (EditText)findViewById(R.id.dialogLocationEditTextZip);
        final Button state = (Button)findViewById(R.id.dialogLocationButtonState);
        final Button submit = (Button) findViewById(R.id.dialogLocationButtonSubmit);

        // State button
        state.setText("CA");
        state.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final PopupMenu popup = new PopupMenu(getContext(), view, Gravity.END);
                popup.getMenu().add(Menu.NONE, 0, Menu.NONE, "AL");
                popup.getMenu().add(Menu.NONE, 1, Menu.NONE, "AK");
                popup.getMenu().add(Menu.NONE, 2, Menu.NONE, "AZ");
                popup.getMenu().add(Menu.NONE, 3, Menu.NONE, "AR");
                popup.getMenu().add(Menu.NONE, 4, Menu.NONE, "CA");
                popup.getMenu().add(Menu.NONE, 5, Menu.NONE, "CO");
                popup.getMenu().add(Menu.NONE, 6, Menu.NONE, "CT");
                popup.getMenu().add(Menu.NONE, 7, Menu.NONE, "DE");
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        state.setText(menuItem.getTitle());
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
                if (Globals.getPatron() != null && Globals.getPatron().getIdentity() != null &&
                    Globals.getPatron().getIdentity().getLocations() != null)
                {
                    Location location = new Location(address.getText().toString(), city.getText().toString(), state.getText().toString(), zip.getText().toString(), 0, 0);
                    Globals.getPatron().getIdentity().getLocations().add(location);
                }
                dialog.dismiss();
            }
        });
    }
}
