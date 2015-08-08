package com.endeal.patron.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.endeal.patron.listeners.OnApiExecutedListener;
import com.endeal.patron.listeners.RetrievalButtonListener;
import com.endeal.patron.model.ApiResult;
import com.endeal.patron.model.Location;
import com.endeal.patron.model.Patron;
import com.endeal.patron.model.Retrieval;
import com.endeal.patron.system.Globals;
import com.endeal.patron.system.ApiExecutor;
import com.endeal.patron.R;
import static com.endeal.patron.model.Retrieval.Method;

import org.joda.time.DateTime;

public class LocationDialog extends Dialog
{
    private RetrievalButtonListener button;
    private boolean submitting;

    public LocationDialog(Context context)
    {
        super(context);
        init();
    }

    public LocationDialog(Context context, RetrievalButtonListener button)
    {
        super(context);
        this.button = button;
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
        submitting = false;
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
                popup.getMenu().add(Menu.NONE, 8, Menu.NONE, "FL");
                popup.getMenu().add(Menu.NONE, 9, Menu.NONE, "GA");
                popup.getMenu().add(Menu.NONE, 10, Menu.NONE, "HI");
                popup.getMenu().add(Menu.NONE, 11, Menu.NONE, "ID");
                popup.getMenu().add(Menu.NONE, 12, Menu.NONE, "IL");
                popup.getMenu().add(Menu.NONE, 13, Menu.NONE, "IN");
                popup.getMenu().add(Menu.NONE, 14, Menu.NONE, "IA");
                popup.getMenu().add(Menu.NONE, 15, Menu.NONE, "KS");
                popup.getMenu().add(Menu.NONE, 16, Menu.NONE, "KY");
                popup.getMenu().add(Menu.NONE, 17, Menu.NONE, "LA");
                popup.getMenu().add(Menu.NONE, 18, Menu.NONE, "ME");
                popup.getMenu().add(Menu.NONE, 19, Menu.NONE, "MD");
                popup.getMenu().add(Menu.NONE, 20, Menu.NONE, "MA");
                popup.getMenu().add(Menu.NONE, 21, Menu.NONE, "MI");
                popup.getMenu().add(Menu.NONE, 22, Menu.NONE, "MN");
                popup.getMenu().add(Menu.NONE, 23, Menu.NONE, "MS");
                popup.getMenu().add(Menu.NONE, 24, Menu.NONE, "MO");
                popup.getMenu().add(Menu.NONE, 25, Menu.NONE, "MT");
                popup.getMenu().add(Menu.NONE, 26, Menu.NONE, "NE");
                popup.getMenu().add(Menu.NONE, 27, Menu.NONE, "NV");
                popup.getMenu().add(Menu.NONE, 28, Menu.NONE, "NH");
                popup.getMenu().add(Menu.NONE, 29, Menu.NONE, "NJ");
                popup.getMenu().add(Menu.NONE, 30, Menu.NONE, "NM");
                popup.getMenu().add(Menu.NONE, 31, Menu.NONE, "NY");
                popup.getMenu().add(Menu.NONE, 32, Menu.NONE, "NC");
                popup.getMenu().add(Menu.NONE, 33, Menu.NONE, "ND");
                popup.getMenu().add(Menu.NONE, 34, Menu.NONE, "OH");
                popup.getMenu().add(Menu.NONE, 35, Menu.NONE, "OK");
                popup.getMenu().add(Menu.NONE, 36, Menu.NONE, "OR");
                popup.getMenu().add(Menu.NONE, 37, Menu.NONE, "PA");
                popup.getMenu().add(Menu.NONE, 38, Menu.NONE, "RI");
                popup.getMenu().add(Menu.NONE, 39, Menu.NONE, "SC");
                popup.getMenu().add(Menu.NONE, 40, Menu.NONE, "SD");
                popup.getMenu().add(Menu.NONE, 41, Menu.NONE, "TN");
                popup.getMenu().add(Menu.NONE, 42, Menu.NONE, "TX");
                popup.getMenu().add(Menu.NONE, 43, Menu.NONE, "UT");
                popup.getMenu().add(Menu.NONE, 44, Menu.NONE, "VT");
                popup.getMenu().add(Menu.NONE, 45, Menu.NONE, "VA");
                popup.getMenu().add(Menu.NONE, 46, Menu.NONE, "WA");
                popup.getMenu().add(Menu.NONE, 47, Menu.NONE, "WV");
                popup.getMenu().add(Menu.NONE, 48, Menu.NONE, "WI");
                popup.getMenu().add(Menu.NONE, 49, Menu.NONE, "WY");
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
        final ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setIndeterminate(true);
        submit.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                if (submitting)
                    return;
                submitting = true;
                final RelativeLayout layout = (RelativeLayout)view.getRootView().findViewById(R.id.dialogLocationRelativeLayoutMain);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                layout.addView(progressBar, params);

                final Location location = new Location(address.getText().toString(), city.getText().toString(), state.getText().toString(), zip.getText().toString(), 0, 0);
                Patron patron = Globals.getPatron();
                if (patron != null && patron.getIdentity() != null && patron.getIdentity().getLocations() != null)
                {
                    patron.getIdentity().getLocations().add(location);
                }
                ApiExecutor executor = new ApiExecutor();
                executor.updatePatron(patron, new OnApiExecutedListener() {
                    @Override
                    public void onExecuted(ApiResult result)
                    {
                        submitting = false;
                        layout.removeView(progressBar);
                        if (result.getStatusCode() == 200)
                        {
                            if (Globals.getOrder() == null)
                            {
                                dialog.dismiss();
                                return;
                            }
                            Retrieval retrieval = Globals.getOrder().getRetrieval();
                            if (retrieval.getMethod() == Method.Delivery)
                            {
                                retrieval.setLocation(location);
                                if (button != null)
                                    button.update();
                            }
                            dialog.dismiss();
                            return;
                        }
                        Snackbar.make(view.getRootView(), result.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
