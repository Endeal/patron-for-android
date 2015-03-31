package com.patron.main;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appboy.Appboy;

import com.patron.db.ScanConnector;
import com.patron.listeners.DrawerNavigationListener;
import com.patron.listeners.OnApiExecutedListener;
import com.patron.lists.ListLinks;
import com.patron.model.Order;
import com.patron.R;
import com.patron.system.ApiExecutor;
import com.patron.system.Globals;
import com.patron.system.Loadable;
import com.patron.system.Patron;
import com.patron.view.NavigationListView;
import static com.patron.view.NavigationListView.Hierarchy;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashScan extends Activity
{
    private Order order;

	// Activity
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_scan);
		Bundle bundle = getIntent().getExtras();
        final ApiExecutor api = new ApiExecutor();
        final Context context = this;

        // Set up the UI elements.
		final ImageView imageCode = (ImageView)findViewById(R.id.codeImageImageViewCode);
        final LinearLayout layout = (LinearLayout)findViewById(R.id.scanLayoutMain);
        final TextView textView = (TextView)findViewById(R.id.scanTextOrder);
        final TextView textViewStatus = (TextView)findViewById(R.id.scanTextViewStatus);

		// Set up the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout)findViewById(R.id.scanDrawerNavigation);
		NavigationListView listNavigation = (NavigationListView)findViewById(R.id.scanListNavigation);
		DrawerNavigationListener drawerNavigationListener = new DrawerNavigationListener(this);
		drawerLayoutNavigation.setDrawerListener(drawerNavigationListener);
		listNavigation.setHierarchy(drawerNavigationListener, drawerLayoutNavigation, Hierarchy.ORDERS);

        // Loading indicator
        final RelativeLayout loadingLayout = (RelativeLayout)findViewById(R.id.scanRelativeLayoutLoading);
        final ProgressBar progressIndicator = new ProgressBar(this);
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        progressIndicator.setLayoutParams(params);
        progressIndicator.setBackgroundColor(Color.TRANSPARENT);
        loadingLayout.addView(progressIndicator);

        // Listener to get code image.
        final OnApiExecutedListener updateListener = new OnApiExecutedListener() {
            @Override
            public void onExecuted()
            {
                // Remove the loading indicator.
                loadingLayout.removeView(progressIndicator);

                // If the scan is null, error out to the orders screen
                if (Globals.getScan() == null)
                {
                    Intent intent = new Intent(context, FlashOrders.class);
                    context.startActivity(intent);
                    return;
                }

                // Set and resize the image.
                int margin = 20;
                imageCode.setImageBitmap(Globals.getScan());
                LayoutParams params = new LayoutParams(layout.getWidth() - margin, layout.getWidth() - margin);
                params.setMargins(margin, margin, margin, margin);
                imageCode.setLayoutParams(params);

                // Set the order text.
                String text = order.getOrderText();
                textView.setText(text);

                // Set the status text.
                text = Order.getStatusText(order.getStatus());
                textViewStatus.setText(text);
            }
        };

		if (bundle != null)
		{
			String orderRow = bundle.getString("orderRow");
			int row = Integer.parseInt(orderRow);

            // Go to the login screen if the user has no credentials.
            if (Globals.getEmail() == null || Globals.getEmail().equals("") ||
                    Globals.getPassword() == null || Globals.getPassword().equals(""))
            {
                Intent intent = new Intent(this, FlashLogin.class);
                startActivity(intent);
                finish();
                return;
            }
            // Update the orders if coming from push notification.
            final String orderId = bundle.getString("orderId");
            if (orderId != null && !orderId.equals(""))
            {
                api.getOrders(new OnApiExecutedListener() {
                    @Override
                    public void onExecuted()
                    {
                        for (int i = 0; i < Globals.getOrders().size(); i++)
                        {
                            Order tempOrder = Globals.getOrders().get(i);
                            if (tempOrder.getId().equals(orderId))
                            {
                                order = tempOrder;
                                break;
                            }
                        }
                        api.getScan(order, updateListener);
                    }
                });
                return;
            }
            // Get the order for the row count if coming from orders screen.
            order = Globals.getOrders().get(row);
            api.getScan(order, updateListener);
		}
        // Update the view if no bundle was set.
        else
        {
            updateListener.onExecuted();
        }
	}

    public void onStart()
    {
        super.onStart();
        Appboy.getInstance(FlashScan.this).openSession(FlashScan.this);
    }

    public void onStop()
    {
        super.onStop();
        Appboy.getInstance(FlashScan.this).closeSession(FlashScan.this);
    }

	// Calligraphy
	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
