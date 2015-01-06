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
import com.patron.db.AddBankAccountConnector;

public class FlashAddBankAccount extends Activity implements Loadable
{
	private boolean submitting = false;
	private RelativeLayout layout;
	private ProgressBar progressIndicator;
	private static String accountType = "checking";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_add_bank_account);
		layout = (RelativeLayout)findViewById(R.id.addBankAccountLayoutMain);
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
		final EditText fieldName = (EditText)findViewById(R.id.addBankAccountFieldName);
		final EditText fieldNumber = (EditText)findViewById(R.id.addBankAccountFieldNumber);
		final EditText fieldRouting = (EditText)findViewById(R.id.addBankAccountFieldRouting);
		final Spinner fieldType = (Spinner)findViewById(R.id.addBankAccountSpinnerType);
		final Button buttonSubmit = (Button)findViewById(R.id.addBankAccountButtonSubmit);

		// Set field's to mock data.
		fieldName.setText("James Whiteman");
		fieldNumber.setText(" 000000130688770");
		fieldRouting.setText("322271627");

		// Spinner actions
		fieldType.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView parent, View view, int position, long id)
			{
				if (position != 0)
				{
					FlashAddBankAccount.accountType = "savings";
				}
				else
				{
					FlashAddBankAccount.accountType = "checking";
				}
			}
			@Override
			public void onNothingSelected(AdapterView parent)
			{
				FlashAddBankAccount.accountType = "checking";
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
					String routing = fieldRouting.getText().toString();
					String type = FlashAddBankAccount.accountType;

					// Add the card to the user.
					AddBankAccountConnector connector = new AddBankAccountConnector(getActivity(), name, number, type, routing,
						null, null, null, null);
					connector.execute(view.getContext());

					// Print out data for test's sake
					System.out.println("Name:" + name);
					System.out.println("Account Number:" + number);
					System.out.println("Routing Number:" + routing);
					System.out.println("Type:" + type);
				}
			}
		});
	}

	public FlashAddBankAccount getActivity()
	{
		return this;
	}
}
