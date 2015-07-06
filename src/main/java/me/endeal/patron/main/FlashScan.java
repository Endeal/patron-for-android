package me.endeal.patron.main;

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

import com.appsee.Appsee;

import me.endeal.patron.listeners.DrawerNavigationListener;
import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.lists.ListLinks;
import me.endeal.patron.model.Order;
import me.endeal.patron.R;
import me.endeal.patron.system.ApiExecutor;
import me.endeal.patron.system.Globals;
import me.endeal.patron.system.Loadable;
import me.endeal.patron.system.PatronApplication;
import me.endeal.patron.view.NavigationListView;
import static me.endeal.patron.view.NavigationListView.Hierarchy;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashScan extends Activity
{
    private Order order;

	// Activity
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//Appsee.start("41ea93b81fd7435b8e8d20ea2ba36b66");
		setContentView(R.layout.layout_scan);
		Bundle bundle = getIntent().getExtras();
        final ApiExecutor api = new ApiExecutor();
        final Context context = this;

        // Set up the UI elements.
		final ImageView imageCode = (ImageView)findViewById(R.id.codeImageImageViewCode);
        final LinearLayout layout = (LinearLayout)findViewById(R.id.scanLayoutMain);
        final TextView textView = (TextView)findViewById(R.id.scanTextOrder);
        final TextView textViewStatus = (TextView)findViewById(R.id.scanTextViewStatus);

        // Set the status text color

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

                // If the scan is null, remove view from stack
                if (Globals.getScan() == null)
                {
                    Activity activity = (Activity)context;
                    activity.finish();
                    return;
                }
                // If the order is null, start the orders screen
                if (order == null)
                {
                  Activity activity = (Activity)context;
                  Intent intent = new Intent(context, FlashOrders.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  context.startActivity(intent);
                  activity.finish();
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
                if (text.toLowerCase().equals("waiting"))
                {
                    textViewStatus.setTextColor(Color.YELLOW);
                }
                else if (text.toLowerCase().equals("ready"))
                {
                    textViewStatus.setTextColor(Color.GREEN);
                }
                else
                {
                    textViewStatus.setTextColor(Color.RED);
                }
            }
        };

        // Go to the login screen if the user has no credentials.
        if (Globals.getEmail() == null || Globals.getEmail().equals("") ||
        Globals.getPassword() == null || Globals.getPassword().equals(""))
        {
          Intent intent = new Intent(this, FlashLogin.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent);
          finish();
          return;
        }

		if (bundle != null)
		{
			final String orderRow = bundle.getString("orderRow");
      final String orderId = bundle.getString("orderId");
      if (orderRow != null && orderRow.length() > 0)
      {
			     int row = Integer.parseInt(orderRow);

            // Get the order for the row count if coming from orders screen.
            order = Globals.getOrders().get(row);
            api.getScan(order, updateListener);
      }
      // Login and update the orders if coming from push notification.
      if (orderId != null && orderId.length() > 0)
      {
        api.login(Globals.getEmail(), Globals.getPassword(), Globals.getProvider(), new OnApiExecutedListener() {
          @Override
          public void onExecuted()
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
          }
        });
      }
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
