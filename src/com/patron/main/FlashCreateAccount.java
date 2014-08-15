package com.patron.main;

import java.lang.Exception;
import java.util.Calendar;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ProgressBar;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.patron.system.Loadable;

import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthError;

public class FlashCreateAccount extends ActionBarActivity implements Loadable
{
	private boolean submitting = false;
	public static int year;
	public static int month;
	public static int day;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_create_account);
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
	}

	@Override
	public void update()
	{
	}

	@Override
	public void message(String msg)
	{
	}

	// Button actions
	public void init()
	{
		// Get the layout elements.
		final RelativeLayout layout = (RelativeLayout)findViewById(R.id.createAccountLayoutMain);
		final Button buttonBirthday = (Button)findViewById(R.id.createAccountButtonBirthday);
		Button buttonCancel = (Button)findViewById(R.id.createAccountButtonCancel);
		Button buttonSubmit = (Button)findViewById(R.id.createAccountButtonSubmit);

		// Set the default birthday text.
		final Calendar date = Calendar.getInstance();
		FlashCreateAccount.day = date.get(Calendar.DAY_OF_MONTH);
		FlashCreateAccount.month = date.get(Calendar.MONTH);
		FlashCreateAccount.year = date.get(Calendar.YEAR);
		buttonBirthday.setText((FlashCreateAccount.month + 1) + " / " + FlashCreateAccount.day + " / " + FlashCreateAccount.year);

		// Set the actions for each button.
		buttonBirthday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int day)
					{
						buttonBirthday.setText((month + 1) + " / " + day + " / " + year);
						FlashCreateAccount.year = year;
						FlashCreateAccount.month = month;
						FlashCreateAccount.day = day;
					}
				};
				DatePickerDialog dialog = new DatePickerDialog(view.getContext(), listener,
					FlashCreateAccount.year, FlashCreateAccount.month, FlashCreateAccount.day);
				dialog.show();
			}
		});
		buttonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(view.getContext(), FlashLogin.class);
				startActivity(intent);
			}
		});
		buttonSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				if (!submitting)
				{
					ProgressBar progressIndicator = new ProgressBar(view.getContext());
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
					params.addRule(RelativeLayout.CENTER_HORIZONTAL);
					params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
					layout.addView(progressIndicator, params);
					submitting = true;

					Intent intent = new Intent(view.getContext(), FlashPayment.class);
					startActivity(intent);
				}
			}
		});
	}
}
