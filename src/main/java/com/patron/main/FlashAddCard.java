package com.patron.main;

import java.lang.Exception;
import java.lang.Runnable;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.content.Context;
import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.widget.EditText;

import com.balancedpayments.android.Balanced;
import com.balancedpayments.android.Card;
import com.balancedpayments.android.BankAccount;
import com.balancedpayments.android.exception.*;

import com.patron.listeners.OnApiExecutedListener;
import com.patron.R;
import com.patron.system.ApiExecutor;
import com.patron.system.Loadable;
import com.patron.db.AddCardConnector;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashAddCard extends Activity implements Loadable
{
	private boolean submitting = false;
	private RelativeLayout layout;
	private ProgressBar progressIndicator;
	public static int expirationMonth = 1;
	public static int expirationYear = 1970;
	private String screen;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.screen = null;
		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			String value = extras.getString("activity");
			this.screen = value;
		}
		setContentView(R.layout.layout_add_card);
		layout = (RelativeLayout)findViewById(R.id.addCardLayoutMain);
		init();
	}

	@Override
	public void beginLoading()
	{

	}

	@Override
	public void load()
	{
	}

	@Override
	public void endLoading()
	{
		layout.removeView(progressIndicator);
		submitting = false;
	}

	@Override
	public void update()
	{
		Intent intent;
		if (screen != null && screen.equals("cart"))
		{
			intent = new Intent(this, FlashCart.class);
		}
		else
		{
			intent = new Intent(this, FlashSettings.class);
		}
		startActivity(intent);
	}

	@Override
	public void message(String msg)
	{
		final String message = msg;
		runOnUiThread(new Runnable() {
  			public void run()
  			{
    			Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
  			}
		});
	}

	// Button actions
	public void init()
	{
		// Get the layout elements.
		final EditText fieldName = (EditText)findViewById(R.id.addCardEditTextName);
		final EditText fieldNumber = (EditText)findViewById(R.id.addCardEditTextNumber);
		final EditText fieldCode = (EditText)findViewById(R.id.addCardEditTextCode);
		final Spinner fieldExpirationMonth = (Spinner)findViewById(R.id.addCardSpinnerExpirationMonth);
		final Spinner fieldExpirationYear = (Spinner)findViewById(R.id.addCardSpinnerExpirationYear);
		final Button buttonSubmit = (Button)findViewById(R.id.addCardButtonSubmit);

		// Set field's to mock data.
		fieldName.setText("James Whiteman");
		fieldNumber.setText("4833160029475107");
		fieldCode.setText("768");

		// Spinner actions
		fieldExpirationMonth.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView parent, View view, int position, long id)
			{
				FlashAddCard.expirationMonth = position + 1;
			}
			@Override
			public void onNothingSelected(AdapterView parent)
			{
				FlashAddCard.expirationMonth = 1;
			}
		});
		fieldExpirationYear.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView parent, View view, int position, long id)
			{
				FlashAddCard.expirationYear = position + 2014;
			}
			@Override
			public void onNothingSelected(AdapterView parent)
			{
				FlashAddCard.expirationYear = 1970;
			}
		});

		// Submission actions.
		buttonSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				if (!submitting)
				{
					// Show the loading graphic
					progressIndicator = new ProgressBar(view.getContext());
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
					params.addRule(RelativeLayout.CENTER_HORIZONTAL);
					params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					layout.addView(progressIndicator, params);
					submitting = true;

					// Get the data from the form.
					final String name = fieldName.getText().toString();
					final String number = fieldNumber.getText().toString();
					final String code = fieldCode.getText().toString();
					final int month = FlashAddCard.expirationMonth;
					final int year = FlashAddCard.expirationYear;
					final View v = view;

					// Add the card to the user.
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run()
						{
							ApiExecutor executor = new ApiExecutor();
							executor.addCard(name, number, code, month, year, v.getContext(), new OnApiExecutedListener() {
								@Override
								public void onExecuted()
								{
									submitting = false;
									layout.removeView(progressIndicator);
								}
							});
						}
					});
	        thread.start();
				}
			}
		});
	}

	public FlashAddCard getActivity()
	{
		return this;
	}

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
