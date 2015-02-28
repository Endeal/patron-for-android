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

import com.patron.R;
import com.patron.system.Loadable;
import com.patron.db.AddCardConnector;

public class FlashAddCard extends Activity implements Loadable
{
	private boolean submitting = false;
	private RelativeLayout layout;
	private ProgressBar progressIndicator;
	public static int expirationMonth = 1;
	public static int expirationYear = 1970;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_add_card);
		layout = (RelativeLayout)findViewById(R.id.addCardLayoutMain);
		init();
	}

	@Override
	public void beginLoading()
	{
		progressIndicator = new ProgressBar(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layout.addView(progressIndicator, params);
		submitting = true;
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
		Intent intent = new Intent(this, FlashHome.class);
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
		final EditText fieldName = (EditText)findViewById(R.id.addCardFieldName);
		final EditText fieldNumber = (EditText)findViewById(R.id.addCardFieldNumber);
		final EditText fieldCode = (EditText)findViewById(R.id.addCardFieldCode);
		final Spinner fieldExpirationMonth = (Spinner)findViewById(R.id.addCardFieldExpirationMonth);
		final Spinner fieldExpirationYear = (Spinner)findViewById(R.id.addCardFieldExpirationYear);
		final EditText fieldAddress = (EditText)findViewById(R.id.addCardFieldAddress);
		final EditText fieldState = (EditText)findViewById(R.id.addCardFieldState);
		final EditText fieldCity = (EditText)findViewById(R.id.addCardFieldCity);
		final EditText fieldPostalCode = (EditText)findViewById(R.id.addCardFieldPostalCode);
		final Button buttonSubmit = (Button)findViewById(R.id.addCardButtonSubmit);

		// Set field's to mock data.
		fieldName.setText("James Whiteman");
		fieldNumber.setText("4833160029475107");
		fieldCode.setText("768");
		fieldAddress.setText("1500 Ralston Avenue");
		fieldState.setText("CA");
		fieldCity.setText("Belmont");
		fieldPostalCode.setText("94002");

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
					beginLoading();

					// Get the data from the form.
					String name = fieldName.getText().toString();
					String number = fieldNumber.getText().toString();
					String code = fieldCode.getText().toString();
					int month = FlashAddCard.expirationMonth;
					int year = FlashAddCard.expirationYear;
					String address = fieldAddress.getText().toString();
					String state = fieldState.getText().toString();
					String city = fieldCity.getText().toString();
					String postalCode = fieldPostalCode.getText().toString();

					// Add the card to the user.
					AddCardConnector connector = new AddCardConnector(getActivity(), name, number, code, month,
						year, address, state, city, postalCode);
					connector.execute(view.getContext());
				}
			}
		});
	}

	public FlashAddCard getActivity()
	{
		return this;
	}
}
