package com.endeal.patron.activity;

import java.util.ArrayList;
import java.lang.Exception;
import java.util.List;
import java.util.Calendar;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.net.URL;
import java.net.MalformedURLException;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.CoordinatorLayout;
import android.view.inputmethod.InputMethodManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appboy.Appboy;

import com.appsee.Appsee;

import com.endeal.patron.activity.MenuActivity;
import com.endeal.patron.lists.ListLinks;
import com.endeal.patron.listeners.OnApiExecutedListener;
import com.endeal.patron.model.*;
import com.endeal.patron.R;
import com.endeal.patron.system.ApiExecutor;
import com.endeal.patron.system.Globals;

import org.joda.time.DateTime;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CreateAccountActivity extends AppCompatActivity
{
	private boolean submitting = false;

	private EditText fieldFirstName;
	private EditText fieldLastName;
	private EditText fieldEmail;
	private EditText fieldPassword;
	private EditText fieldConfirm;
    private RelativeLayout layout;
    private CoordinatorLayout coordinatorLayout;
	private ProgressBar progressIndicator;

	public static int year;
	public static int month;
	public static int day;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_create_account);

		// Get the layout elements.
		final Button buttonBirthday = (Button)findViewById(R.id.createAccountButtonBirthday);
		final Button buttonSubmit = (Button)findViewById(R.id.createAccountButtonSubmit);
		progressIndicator = new ProgressBar(this);
		fieldFirstName = (EditText)findViewById(R.id.createAccountEditTextFirst);
		fieldLastName = (EditText)findViewById(R.id.createAccountEditTextLast);
		fieldEmail = (EditText)findViewById(R.id.createAccountEditTextEmail);
		fieldPassword = (EditText)findViewById(R.id.createAccountEditTextPassword);
		fieldConfirm = (EditText)findViewById(R.id.createAccountEditTextConfirm);
        layout = (RelativeLayout)findViewById(R.id.createAccountRelativeLayoutMain);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.createAccountCoordinatorLayoutMain);

		Intent intent = getIntent();
		String email = intent.getStringExtra("email");
		String password = intent.getStringExtra("password");
		if (email != null && email.length() > 0)
		{
			fieldEmail.setText(email);
		}
		if (password != null && password.length() > 0)
		{
			fieldPassword.setText(password);
		}

		// Set the default birthday text.
		final Calendar date = Calendar.getInstance();
		CreateAccountActivity.day = date.get(Calendar.DAY_OF_MONTH);
		CreateAccountActivity.month = date.get(Calendar.MONTH);
		CreateAccountActivity.year = date.get(Calendar.YEAR);
		buttonBirthday.setText((CreateAccountActivity.month + 1) + " / " + CreateAccountActivity.day + " / " + CreateAccountActivity.year);

		// Set the actions for each button.
		buttonBirthday.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int day)
					{
						buttonBirthday.setText((month + 1) + " / " + day + " / " + year);
						CreateAccountActivity.year = year;
						CreateAccountActivity.month = month;
						CreateAccountActivity.day = day;
					}
				};
				DatePickerDialog dialog = new DatePickerDialog(view.getContext(), R.style.DialogTheme, dateListener,
					CreateAccountActivity.year, CreateAccountActivity.month, CreateAccountActivity.day);
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion < android.os.Build.VERSION_CODES.LOLLIPOP)
                {
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                }
                dialog.getDatePicker().setMaxDate(new DateTime().getMillis());
                dialog.getDatePicker().setMinDate(new DateTime(1910, 1, 1, 0, 0).getMillis());
				dialog.show();
			}
		});
		buttonSubmit.setOnClickListener(new ButtonCreateAccountListener());
	}

    public void onStart()
    {
        super.onStart();
        Appboy.getInstance(CreateAccountActivity.this).openSession(CreateAccountActivity.this);
    }

    public void onStop()
    {
        super.onStop();
        Appboy.getInstance(CreateAccountActivity.this).closeSession(CreateAccountActivity.this);
    }

  // TextView methods
  public void viewPrivacyPolicy(View view)
  {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.endeal.me/patron/privacy-policy"));
    startActivity(intent);
  }

  public void viewTermsOfService(View view)
  {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.endeal.me/patron/terms-of-service"));
    startActivity(intent);
  }

	private class ButtonCreateAccountListener implements OnClickListener
	{
		@Override
		public void onClick(final View view)
		{
            InputMethodManager inputMethodManager = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
			if (submitting)
                return;

            final Context context = view.getContext();
            final String firstName = fieldFirstName.getText().toString();
            final String lastName = fieldLastName.getText().toString();
            final String email = fieldEmail.getText().toString();
            final String password = fieldPassword.getText().toString();
            final String confirm = fieldConfirm.getText().toString();
            final String birthday = CreateAccountActivity.year + "-" + (CreateAccountActivity.month + 1) + "-" + CreateAccountActivity.day;
            final OnApiExecutedListener listener = new OnApiExecutedListener() {
                @Override
                public void onExecuted(ApiResult result)
                {
                    layout.removeView(progressIndicator);
                    if (Globals.getPatron() != null)
                    {
                        Intent intent = new Intent(context, MenuActivity.class);
                        context.startActivity(intent);
                        Activity activity = (Activity)context;
                        activity.finish();
                    }
                    else
                    {
                        submitting = false;
                        Snackbar.make(coordinatorLayout, "Failed to create account, e-mail already in use", Snackbar.LENGTH_SHORT).show();
                    }
                }
            };

            // Validate the fields
            if (firstName.length() == 0 || lastName.length() == 0 ||
                    email.length() == 0 || password.length() == 0)
            {
                Snackbar.make(coordinatorLayout, "Please fill out all the fields", Snackbar.LENGTH_SHORT).show();
                submitting = false;
                layout.removeView(progressIndicator);
                return;
            }
            if (!password.equals(confirm))
            {
                Snackbar.make(coordinatorLayout, "The password and confirmation do not match", Snackbar.LENGTH_SHORT).show();
                submitting = false;
                layout.removeView(progressIndicator);
                return;
            }
            if (password.length() < 6)
            {
                Snackbar.make(coordinatorLayout, "Password must be at least 6 characters", Snackbar.LENGTH_SHORT).show();
                submitting = false;
                layout.removeView(progressIndicator);
                return;
            }
            Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
            Matcher m = p.matcher(fieldEmail.getText().toString());
            boolean matchFound = m.matches();
            if (!matchFound)
            {
                Snackbar.make(coordinatorLayout, "Please enter a valid e-mail address", Snackbar.LENGTH_SHORT).show();
                submitting = false;
                layout.removeView(progressIndicator);
                return;
            }

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            layout.addView(progressIndicator, params);
            submitting = true;

            // Create patron
            List<Credential> credentials = new ArrayList<Credential>();
            Credential credential = new Credential(email, password, "endeal");
            credentials.add(credential);
            DateTime rawBirthday = new DateTime(CreateAccountActivity.year, CreateAccountActivity.month + 1, CreateAccountActivity.day, 0, 0);
            long birthdayTimestamp = rawBirthday.getMillis();
            List<Location> locations = new ArrayList<Location>();
            Identity identity = new Identity(credentials, firstName, lastName, birthdayTimestamp, locations);
            List<Funder> funders = new ArrayList<Funder>();
            List<String> franchises = new ArrayList<String>();
            List<String> vendors = new ArrayList<String>();
            List<String> items = new ArrayList<String>();
            Patron patron = new Patron("", "", identity, funders, franchises, vendors, items);

            ApiExecutor api = new ApiExecutor();
            api.createAccount(patron, listener);
		}
	}

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
