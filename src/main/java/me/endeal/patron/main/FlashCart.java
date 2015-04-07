/**
 * The Cart screen.
 */
package me.endeal.patron.main;

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

import com.appboy.Appboy;

import com.appsee.Appsee;

import me.endeal.patron.listeners.ButtonCommentListener;
import me.endeal.patron.listeners.ButtonFinishListener;
import me.endeal.patron.listeners.ButtonPaymentListener;
import me.endeal.patron.listeners.ButtonStationListener;
import me.endeal.patron.listeners.ButtonTipListener;
import me.endeal.patron.listeners.ButtonReviewListener;
import me.endeal.patron.listeners.DrawerNavigationListener;
import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.listeners.OnCartRefreshListener;
import me.endeal.patron.system.Globals;
import me.endeal.patron.R;
import me.endeal.patron.view.NavigationListView;
import static me.endeal.patron.view.NavigationListView.Hierarchy;

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
        final Context context = this;
        OnApiExecutedListener buttonRefreshListener = new OnApiExecutedListener() {
            @Override
            public void onExecuted()
            {
                if (Globals.getOrder() == null || Globals.getOrder().getFragments() == null || Globals.getOrder().getFragments().size() == 0)
                {
                    //Intent intent = new Intent(context, FlashMenu.class);
										Activity activity = (Activity)context;
										activity.finish();
                    //context.startActivity(intent);
                    return;
                }
                // Station
                buttonStation.setText("Station:\n" + Globals.getOrder().getStation().getName());

                // Funder
                String number;
                if (Globals.getOrder().getFunder() == null)
                {
                    number = "?";
                    buttonPayment.setText("Payment:\n" + number);
                }
                else
                {
                	number = Globals.getOrder().getFunder().getNumber();
                	buttonPayment.setText("Payment:\n" + number.substring(number.length() - 4));
                }

                // Tip
                buttonTip.setText("Tip:\n$" + Globals.getOrder().getTip().toString());

                // Vouchers
                buttonCoupon.setText("Vouchers:\n" + Globals.getOrder().getCoupons().size());

                // Comment
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

    public void onStart()
    {
        super.onStart();
        Appboy.getInstance(FlashCart.this).openSession(FlashCart.this);
    }

    public void onStop()
    {
        super.onStop();
        Appboy.getInstance(FlashCart.this).closeSession(FlashCart.this);
    }

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}