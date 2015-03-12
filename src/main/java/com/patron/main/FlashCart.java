/**
 * The Cart screen.
 */
package com.patron.main;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import com.patron.listeners.ButtonCommentListener;
import com.patron.listeners.ButtonFinishListener;
import com.patron.listeners.ButtonPaymentListener;
import com.patron.listeners.ButtonStationListener;
import com.patron.listeners.ButtonTipListener;
import com.patron.listeners.ButtonReviewListener;
import com.patron.listeners.DrawerNavigationListener;
import com.patron.listeners.OnApiExecutedListener;
import com.patron.listeners.OnCartRefreshListener;
import com.patron.system.Globals;
import com.patron.R;
import com.patron.view.NavigationListView;
import static com.patron.view.NavigationListView.Hierarchy;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashCart extends Activity
{
	// Activity Methods
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_cart);

		// Set up the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.cartDrawerNavigation);
		NavigationListView listNavigation = (NavigationListView) findViewById(R.id.cartListNavigation);
		DrawerNavigationListener drawerNavigationListener = new DrawerNavigationListener(this);
		drawerLayoutNavigation.setDrawerListener(drawerNavigationListener);
		listNavigation.setHierarchy(drawerNavigationListener, drawerLayoutNavigation, Hierarchy.BUY);

        // Get layout elements.
		ListView listCart = (ListView) findViewById(R.id.cartListItems);
		final Button buttonFinish = (Button) findViewById(R.id.cartButtonFinish);
        final Button buttonStation = (Button) findViewById(R.id.cartButtonStation);
        final Button buttonPayment = (Button) findViewById(R.id.cartButtonPayment);
        final Button buttonTip = (Button) findViewById(R.id.cartButtonTip);
        final Button buttonCoupon = (Button) findViewById(R.id.cartButtonCoupon);
        final Button buttonComment = (Button) findViewById(R.id.cartButtonComment);
        final Button buttonReview = (Button) findViewById(R.id.cartButtonReview);

        // Set button text.
        OnApiExecutedListener buttonRefreshListener = new OnApiExecutedListener() {
            @Override
            public void onExecuted()
            {
                buttonStation.setText("Station:\n" + Globals.getOrder().getStation().getName());
                String number = Globals.getOrder().getFunder().getNumber();
                buttonPayment.setText("Payment:\n" + number.substring(number.length() - 4));
                buttonTip.setText("Tip:\n$" + Globals.getOrder().getTip().toString());
                buttonCoupon.setText("Coupons:\n" + Globals.getOrder().getCoupons().size());
                String commented = "No";
                if (Globals.getOrder().getComment().length() > 0)
                {
                    commented = "Yes";
                }
                buttonComment.setText("Comment:\n" + commented);
                String text = (String) buttonFinish.getText();
                text = "Pay Now: $" + Globals.getOrder().getTotalPrice().toString();
                buttonFinish.setText(text);
            }
        };
        buttonRefreshListener.onExecuted();

        // Set refresh listener.
        OnCartRefreshListener cartRefreshListener = new OnCartRefreshListener(listCart, buttonRefreshListener);
        cartRefreshListener.onExecuted();

        // Set up button listeners.
        buttonStation.setOnClickListener(new ButtonStationListener(cartRefreshListener));
        buttonPayment.setOnClickListener(new ButtonPaymentListener(cartRefreshListener));
        buttonTip.setOnClickListener(new ButtonTipListener(cartRefreshListener));
        buttonComment.setOnClickListener(new ButtonCommentListener(cartRefreshListener));
        buttonReview.setOnClickListener(new ButtonReviewListener(cartRefreshListener));
        buttonFinish.setOnClickListener(new ButtonFinishListener());
        buttonCoupon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(view.getContext(), "You don't have any vouchers.", Toast.LENGTH_SHORT).show();
            }
        });
	}

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
