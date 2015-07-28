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
                            Retrieval retrieval = Globals.getOrder().getRetrieval();
                            if (retrieval.getMethod() == Method.Delivery && retrieval.getLocation() == null)
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
