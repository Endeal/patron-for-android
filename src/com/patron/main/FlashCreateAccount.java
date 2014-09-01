package com.patron.main;

import java.lang.Exception;
import java.util.Calendar;
import java.net.URL;
import java.net.MalformedURLException;

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
import android.widget.Toast;

import com.patron.system.Loadable;
import com.patron.lists.ListLinks;
import com.patron.db.CreateAccountConnector;
import com.patron.model.User;
import com.patron.system.Globals;

import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthError;

public class FlashCreateAccount extends ActionBarActivity implements Loadable
{
	private boolean submitting = false;

	private EditText fieldFirstName;
	private EditText fieldLastName;
	private EditText fieldEmail;
	private EditText fieldPassword;
	private EditText fieldConfirm;
	private ProgressBar progressIndicator;

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
		submitting = false;
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.createAccountLayoutMain);
		layout.removeView(progressIndicator);
	}

	@Override
	public void update()
	{
		User user = new User(fieldFirstName.getText().toString(), fieldLastName.getText().toString(),
			fieldEmail.getText().toString(), fieldPassword.getText().toString(),
			FlashCreateAccount.year + "-" + (FlashCreateAccount.month + 1) + "-" + FlashCreateAccount.day);
		Globals.setUser(user);
		Intent intent = new Intent(this, FlashPayment.class);
		this.finish();
		startActivity(intent);
	}

	@Override
	public void message(String msg)
	{
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
	}

	// Button actions
	public void init()
	{
		// Get the layout elements.
		final Button buttonBirthday = (Button)findViewById(R.id.createAccountButtonBirthday);
		Button buttonSubmit = (Button)findViewById(R.id.createAccountButtonSubmit);
		progressIndicator = new ProgressBar(this);
		fieldFirstName = (EditText)findViewById(R.id.createAccountFieldFirst);
		fieldLastName = (EditText)findViewById(R.id.createAccountFieldLast);
		fieldEmail = (EditText)findViewById(R.id.createAccountFieldEmail);
		fieldPassword = (EditText)findViewById(R.id.createAccountFieldPassword);
		fieldConfirm = (EditText)findViewById(R.id.createAccountFieldConfirm);

		// Prepopulate fields.
		fieldFirstName.setText("John");
		fieldLastName.setText("Miller9");
		fieldEmail.setText("johnmiller9@gmail.com");
		fieldPassword.setText("batman");
		fieldConfirm.setText("batman");

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
		buttonSubmit.setOnClickListener(new ButtonCreateAccountListener(this));
	}

	private class ButtonCreateAccountListener implements OnClickListener
	{
		private Loadable activity;

		public ButtonCreateAccountListener(Loadable activity)
		{
			this.activity = activity;
		}

		@Override
		public void onClick(View view)
		{
			if (!submitting)
			{
				if (!fieldPassword.getText().toString().equals(fieldConfirm.getText().toString()))
				{
					activity.message("The password and confirmation do not match.");
					activity.endLoading();
					return;
				}

				final RelativeLayout layout = (RelativeLayout)findViewById(R.id.createAccountLayoutMain);
				
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
				params.addRule(RelativeLayout.CENTER_HORIZONTAL);
				params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
				layout.addView(progressIndicator, params);
				submitting = true;

				fieldFirstName = (EditText)findViewById(R.id.createAccountFieldFirst);
				fieldLastName = (EditText)findViewById(R.id.createAccountFieldLast);
				fieldEmail = (EditText)findViewById(R.id.createAccountFieldEmail);
				fieldPassword = (EditText)findViewById(R.id.createAccountFieldPassword);
				fieldConfirm = (EditText)findViewById(R.id.createAccountFieldConfirm);

				CreateAccountConnector connector = new CreateAccountConnector(activity, fieldFirstName.getText().toString(),
					fieldLastName.getText().toString(), fieldEmail.getText().toString(), fieldPassword.getText().toString(),
					FlashCreateAccount.year + "-" + (FlashCreateAccount.month + 1) + "-" + FlashCreateAccount.day);
				try
				{
					connector.execute(new URL(ListLinks.LINK_ACCOUNT_ADD));
				}
				catch (MalformedURLException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
