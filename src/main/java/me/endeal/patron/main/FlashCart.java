/**
 * The Cart screen.
 */
package me.endeal.patron.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import com.appboy.Appboy;

import com.appsee.Appsee;

import java.math.BigDecimal;
import java.math.RoundingMode;

import me.endeal.patron.dialogs.CardDialog;
import me.endeal.patron.dialogs.EditCartDialog;
import me.endeal.patron.listeners.ButtonCommentListener;
import me.endeal.patron.listeners.ButtonFinishListener;
import me.endeal.patron.listeners.ButtonPaymentListener;
import me.endeal.patron.listeners.ButtonStationListener;
import me.endeal.patron.listeners.ButtonTipListener;
import me.endeal.patron.listeners.DrawerNavigationListener;
import me.endeal.patron.listeners.FunderButtonListener;
import me.endeal.patron.listeners.RetrievalMethodButtonListener;
import me.endeal.patron.listeners.RetrievalButtonListener;
import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.listeners.OnCartRefreshListener;
import me.endeal.patron.model.*;
import me.endeal.patron.system.Globals;
import me.endeal.patron.R;
import me.endeal.patron.view.NavigationListView;
import static me.endeal.patron.model.Retrieval.Method;
import static me.endeal.patron.view.NavigationListView.Hierarchy;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashCart extends AppCompatActivity
{
    private DrawerNavigationListener drawerToggle;
    private boolean editTextChange;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_review);
        Toolbar toolbar = (Toolbar)findViewById(R.id.reviewToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Set up the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.reviewDrawerNavigation);
		NavigationListView listNavigation = (NavigationListView) findViewById(R.id.reviewListNavigation);
        drawerToggle = new DrawerNavigationListener(this, drawerLayoutNavigation, toolbar, R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawerLayoutNavigation.setDrawerListener(drawerToggle);
		listNavigation.setHierarchy(drawerToggle, drawerLayoutNavigation, Hierarchy.BUY);
        drawerLayoutNavigation.setScrimColor(getResources().getColor(R.color.scrim));
        drawerToggle.syncState();

        if (Globals.getOrder() == null)
        {
            finish();
            return;
        }
        final Order order = Globals.getOrder();

        // Get layout elements.
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout)findViewById(R.id.reviewCoordinatorLayoutMain);
        final TextView textViewTip = (TextView)findViewById(R.id.reviewTextViewTip);
        final DiscreteSeekBar discreteSeekBarTip = (DiscreteSeekBar)findViewById(R.id.reviewDiscreteSeekBarTip);
        final EditText editTextTip = (EditText)findViewById(R.id.reviewEditTextTip);
        final EditText editTextComment = (EditText)findViewById(R.id.reviewEditTextComment);
        final ImageButton imageButtonRetrieval = (ImageButton)findViewById(R.id.reviewImageButtonRetrieval);
        final ImageButton imageButtonVoucher = (ImageButton)findViewById(R.id.reviewImageButtonVoucher);
        final Button buttonRetrieval = (Button)findViewById(R.id.reviewButtonRetrieval);
        final Button buttonFunder = (Button)findViewById(R.id.reviewButtonFunder);
        final Button buttonTotal = (Button)findViewById(R.id.reviewButtonTotal);
        final FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.reviewFloatingActionButtonPay);

        // Set tip slider
        editTextChange = false;
        textViewTip.setText("Tip: " + order.getTip().toString() + " (" + order.getTipPercent() + "%)");
        int percent = order.getTipPercent();
        discreteSeekBarTip.setProgress(percent);
        discreteSeekBarTip.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            public void onProgressChanged(DiscreteSeekBar seekBar, int progress, boolean fromUser)
            {
                if (editTextChange == true)
                    return;

                double percent = progress / 100.0;
                Price newTip = new Price((int)(order.getPrice().getValue() * percent), "USD");
                order.setTip(newTip);
                editTextTip.setText((double)(newTip.getValue() / 100.0) + "", TextView.BufferType.EDITABLE);

                textViewTip.setText("Tip: " + order.getTip().toString() + " (" + order.getTipPercent() + "%)");
                buttonTotal.setText("Total: " + order.getTotalPrice().toString());
            }
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {}
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {}
        });

        // Set tip field
        editTextTip.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s)
            {
                try
                {
                    double rawInput = Double.parseDouble(s.toString());
                    double fullInput = rawInput * 100.0;
                    int fullTipValue = (int)(fullInput);
                    Price tip = new Price(fullTipValue, "USD");
                    order.setTip(tip);
                    textViewTip.setText("Tip: " + order.getTip().toString() + " (" + order.getTipPercent() + "%)");
                    editTextChange = true;
                    discreteSeekBarTip.setProgress(order.getTipPercent());
                    editTextChange = false;
                }
                catch (NumberFormatException e)
                {
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }
          });

        // Set retrieval options available
        RetrievalButtonListener retrievalButtonListener = new RetrievalButtonListener(buttonRetrieval);
        buttonRetrieval.setOnClickListener(retrievalButtonListener);
        retrievalButtonListener.update();

        // Set retrieval types available
        RetrievalMethodButtonListener retrievalMethodButtonListener = new RetrievalMethodButtonListener(imageButtonRetrieval, order, retrievalButtonListener);
        imageButtonRetrieval.setOnClickListener(retrievalMethodButtonListener);
        retrievalMethodButtonListener.update();

        // Set funder button
        String textFunder = "Payment: ";
        if (order.getFunder() == null)
        {
            textFunder = textFunder + "N/A";
        }
        else
        {
            textFunder = textFunder + order.getFunder().getType() + " " + order.getFunder().getNumber();
        }
        buttonFunder.setText(textFunder);
        buttonFunder.setOnClickListener(new FunderButtonListener());

        // Set voucher icon
        imageButtonVoucher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                Snackbar.make(coordinatorLayout, "You do not have any vouchers", Snackbar.LENGTH_SHORT).show();
            }
        });
        // Set total button
        buttonTotal.setText("Total: " + order.getTotalPrice().toString());
        buttonTotal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                new AlertDialog.Builder(view.getContext())
                    .setTitle("Price Breakdown")
                    .setMessage(order.toString())
                    .setPositiveButton("Done", null).show();
            }
        });

        // Set pay button
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                AlertDialog alert = new AlertDialog.Builder(view.getContext())
                    .setTitle("No Debit/Credit Card Added")
                    .setMessage("You must add a debit/credit card to purchase this order. Add one now?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which)
                        {
                            Dialog dialog = new CardDialog(view.getContext());
                            dialog.show();
                        }
                    }).create();
                AlertDialog empty = new AlertDialog.Builder(view.getContext())
                    .setTitle("Empty order")
                    .setMessage("You must add items to your order for purchasing")
                    .setPositiveButton("Done", null).create();
                if (order == null || order.getFragments() == null || order.getFragments().size() <= 0)
                {
                    empty.show();
                }
                else if (order.getFunder() == null)
                {
                    alert.show();
                }
            }
        });

        /*
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
                buttonStation.setText("Station:\n" + Globals.getOrder().getRetrieval().getStation().getName());

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
                buttonCoupon.setText("Vouchers:\n" + Globals.getOrder().getVouchers().size());

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
        buttonFinish.setOnClickListener(new ButtonFinishListener());
        buttonCoupon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(view.getContext(), "You don't have any vouchers.", Toast.LENGTH_SHORT).show();
            }
        });
    */
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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_review, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        if (item.getItemId() == R.id.edit)
        {
            EditCartDialog dialog = new EditCartDialog(this);
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
