/**
 * The Cart screen.
 */
package me.endeal.patron.activity;

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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import com.appboy.Appboy;

import com.appsee.Appsee;

import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;

import me.endeal.patron.adapters.NavigationAdapter;
import me.endeal.patron.dialogs.CardDialog;
import me.endeal.patron.dialogs.EditCartDialog;
import me.endeal.patron.listeners.DrawerNavigationListener;
import me.endeal.patron.listeners.FunderButtonListener;
import me.endeal.patron.listeners.RetrievalMethodButtonListener;
import me.endeal.patron.listeners.RetrievalButtonListener;
import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.model.*;
import me.endeal.patron.system.ApiExecutor;
import me.endeal.patron.system.Globals;
import me.endeal.patron.R;
import me.endeal.patron.view.NavigationListView;
import static me.endeal.patron.model.Retrieval.Method;
import static me.endeal.patron.view.NavigationListView.Hierarchy;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ReviewActivity extends AppCompatActivity
{
    private DrawerNavigationListener drawerToggle;
    private CoordinatorLayout coordinatorLayout;
    private boolean editTextChange;
    private TextView textViewTip;
    private EditText editTextTip;
    private Button buttonTotal;
    private DiscreteSeekBar discreteSeekBarTip;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_review);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.reviewCoordinatorLayoutMain);
        Toolbar toolbar = (Toolbar)findViewById(R.id.reviewToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Set up the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.reviewDrawerNavigation);
        drawerToggle = new DrawerNavigationListener(this, drawerLayoutNavigation, toolbar, R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawerLayoutNavigation.setDrawerListener(drawerToggle);
        drawerLayoutNavigation.setScrimColor(getResources().getColor(R.color.scrim));
        final RecyclerView recyclerViewNavigation = (RecyclerView)findViewById(R.id.navigationRecyclerViewNavigation);
        final TextView textViewDrawerTitle = (TextView)findViewById(R.id.navigationTextViewDrawerTitle);
        final TextView textViewDrawerSubtitle = (TextView)findViewById(R.id.navigationTextViewDrawerSubtitle);
        final ImageView imageViewDrawerVendor = (ImageView)findViewById(R.id.navigationImageViewDrawerVendor);
        textViewDrawerTitle.setText(Globals.getPatron().getIdentity().getFirstName() + " " + Globals.getPatron().getIdentity().getLastName());
        if (Globals.getVendor() != null)
        {
            textViewDrawerSubtitle.setText(Globals.getVendor().getName());
            Picasso.with(this).load(Globals.getVendor().getPicture()).into(imageViewDrawerVendor);
        }
        else
        {
            textViewDrawerSubtitle.setText("No vendor selected");
        }
        NavigationAdapter navigationAdapter = new NavigationAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerViewNavigation.setLayoutManager(layoutManager);
        recyclerViewNavigation.setAdapter(navigationAdapter);
        drawerToggle.syncState();

        if (Globals.getOrder() == null)
        {
            finish();
            return;
        }
        final Order order = Globals.getOrder();

        // Get layout elements.
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout)findViewById(R.id.reviewCoordinatorLayoutMain);
        textViewTip = (TextView)findViewById(R.id.reviewTextViewTip);
        discreteSeekBarTip = (DiscreteSeekBar)findViewById(R.id.reviewDiscreteSeekBarTip);
        editTextTip = (EditText)findViewById(R.id.reviewEditTextTip);
        final EditText editTextComment = (EditText)findViewById(R.id.reviewEditTextComment);
        final ImageButton imageButtonRetrieval = (ImageButton)findViewById(R.id.reviewImageButtonRetrieval);
        final ImageButton imageButtonVoucher = (ImageButton)findViewById(R.id.reviewImageButtonVoucher);
        final Button buttonRetrieval = (Button)findViewById(R.id.reviewButtonRetrieval);
        final Button buttonFunder = (Button)findViewById(R.id.reviewButtonFunder);
        buttonTotal = (Button)findViewById(R.id.reviewButtonTotal);
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
                    buttonTotal.setText("Total: " + order.getTotalPrice().toString());
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
        String textFunder = "";
        if (order.getFunder() == null)
        {
            textFunder = textFunder + "Payment: N/A";
        }
        else
        {
            textFunder = order.getFunder().toString();
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
        final Activity activity = this;
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view)
            {
                final AlertDialog confirm = new AlertDialog.Builder(view.getContext())
                    .setTitle("Confirm purchase")
                    .setMessage("Are you sure you want to spend " + order.getTotalPrice().toString() + " to purchase this order?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which)
                        {
                            final Dialog loading = new Dialog(view.getContext());
                            loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            loading.setContentView(R.layout.dialog_loading);
                            loading.show();
                            loading.setCancelable(false);
                            loading.setCanceledOnTouchOutside(false);
                            order.setComment(editTextComment.getText().toString());
                            ApiExecutor executor = new ApiExecutor();
                            executor.addOrder(order, new OnApiExecutedListener() {
                                @Override
                                public void onExecuted(ApiResult result)
                                {
                                    if (result.getStatusCode() != 201)
                                    {
                                        loading.dismiss();
                                        Snackbar.make(coordinatorLayout, result.getMessage(), Snackbar.LENGTH_SHORT).show();
                                        return;
                                    }
                                    else
                                    {
                                        loading.dismiss();
                                        Context context = view.getContext();
                                        Intent intent = new Intent(context, OrdersActivity.class);
                                        context.startActivity(intent);
                                        activity.finish();
                                    }
                                }
                            });
                        }
                    }).create();
                final AlertDialog alert = new AlertDialog.Builder(view.getContext())
                    .setTitle("No Debit/Credit Card Added")
                    .setMessage("You must add a debit/credit card to purchase this order. Add one now?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which)
                        {
                            Dialog dialog = new CardDialog(view.getContext());
                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface)
                                {
                                    if (Globals.getOrder().getFunder() == null && Globals.getPatron().getFunders() != null &&
                                        Globals.getPatron().getFunders().size() > 0)
                                    {
                                        Globals.getOrder().setFunder(Globals.getPatron().getFunders().get(0));
                                        buttonFunder.setText(order.getFunder().toString());
                                    }
                                }
                            });
                            dialog.show();
                        }
                    }).create();
                final AlertDialog empty = new AlertDialog.Builder(view.getContext())
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
                else
                {
                    confirm.show();
                }
            }
        });
	}

    public void onStart()
    {
        super.onStart();
        Appboy.getInstance(ReviewActivity.this).openSession(ReviewActivity.this);
    }

    public void onStop()
    {
        super.onStop();
        Appboy.getInstance(ReviewActivity.this).closeSession(ReviewActivity.this);
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
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface)
                {
                    Order order = Globals.getOrder();
                    textViewTip.setText("Tip: " + order.getTip().toString() + " (" + order.getTipPercent() + "%)");
                    buttonTotal.setText("Total: " + order.getTotalPrice().toString());
                    discreteSeekBarTip.setProgress(order.getTipPercent());
                    double tipValue = (double)(order.getTip().getValue() / 100.0);
                    editTextTip.setText(tipValue + "", TextView.BufferType.EDITABLE);
                }
            });
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
