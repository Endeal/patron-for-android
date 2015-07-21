package me.endeal.patron.activity;

import java.lang.Exception;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appboy.Appboy;

import com.appsee.Appsee;

import me.endeal.patron.lists.ListLinks;
import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.model.Patron;
import me.endeal.patron.R;
import me.endeal.patron.system.ApiExecutor;
import me.endeal.patron.system.Globals;

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
				DatePickerDialog dialog = new DatePickerDialog(view.getContext(), dateListener,
					CreateAccountActivity.year, CreateAccountActivity.month, CreateAccountActivity.day);
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
		public void onClick(View view)
		{
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
                public void onExecuted()
                {
                    layout.removeView(progressIndicator);
                    /*
                    if (Globals.getUser() != null)
                    {
                        Globals.setEmail(email);
                        Globals.setPassword(password);
                        Globals.setProvider("patron");
                        Intent intent = new Intent(context, FlashMenu.class);
                        context.startActivity(intent);
                        Activity activity = (Activity)context;
                        activity.finish();
                    }
                    */
                }
            };

            // Validate the fields
            if (firstName.length() == 0 || lastName.length() == 0 ||
                    email.length() == 0 || password.length() == 0)
            {
                Snackbar.make(coordinatorLayout, "Please fill out all the fields", Snackbar.LENGTH_SHORT).show();
                listener.onExecuted();
                return;
            }
            if (!password.equals(confirm))
            {
                Snackbar.make(coordinatorLayout, "The password and confirmation do not match", Snackbar.LENGTH_SHORT).show();
                listener.onExecuted();
                return;
            }
            if (password.length() < 6)
            {
                Snackbar.make(coordinatorLayout, "Password must be at least 6 characters", Snackbar.LENGTH_SHORT).show();
                listener.onExecuted();
                return;
            }
            Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
            Matcher m = p.matcher(fieldEmail.getText().toString());
            boolean matchFound = m.matches();
            if (!matchFound)
            {
                Snackbar.make(coordinatorLayout, "Please enter a valid e-mail address", Snackbar.LENGTH_SHORT).show();
                listener.onExecuted();
                return;
            }

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            layout.addView(progressIndicator, params);
            submitting = true;

            //ApiExecutor api = new ApiExecutor();
            //api.createAccount(firstName, lastName, email, password, birthday, listener);
		}
	}

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}