package com.patron.main;

import dalvik.system.DexFile;

import java.io.IOException;
import java.lang.Exception;
import java.lang.Runnable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.patron.system.ApiExecutor;
import com.patron.system.Loadable;
import com.patron.listeners.OnTaskCompletedListener;
import com.patron.lists.ListLinks;
import com.patron.db.LoginConnector;
import com.patron.db.ServiceConnector;

import org.apache.http.HttpEntity;

import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
import org.brickred.socialauth.Profile;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;

public class FlashLogin extends Activity implements Loadable
{
	private boolean submitting;
	private ProgressBar progressBar;
	private SocialAuthAdapter socialAuthAdapter;
	private ImageButton buttonPatron;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	// Activity methods
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);
		init();
	}

	/*
	* Handle results returned to the FragmentActivity
	* by Google Play services
	*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// Decide what to do based on the original request code
		switch (requestCode)
		{
			case CONNECTION_FAILURE_RESOLUTION_REQUEST :
			/*
			* If the result code is Activity.RESULT_OK, try
			* to connect again
			*/
			switch (resultCode) {
				case Activity.RESULT_OK :
				/*
				* Try the request again
				*/
				break;
			}
		}
	}

	// Loadable Inherited methods
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
	}

	@Override
	public void update()
	{
		ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
			this, buttonPatron, "loginButton");
		Bundle bundle = options.toBundle();
		Intent intent = new Intent(this, FlashVendors.class);
		this.finish();
		ActivityCompat.startActivity(this, intent, bundle);
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
		buttonPatron = (ImageButton)findViewById(R.id.loginButtonPatron);
		ImageButton buttonFacebook = (ImageButton)findViewById(R.id.loginButtonFacebook);
		ImageButton buttonTwitter = (ImageButton)findViewById(R.id.loginButtonTwitter);
		ImageButton buttonGooglePlus = (ImageButton)findViewById(R.id.loginButtonGooglePlus);
		final ButtonLoginPatronDoneListener listener = new ButtonLoginPatronDoneListener(this);

		// Configure the adapter with the API keys & secrets.
		// String permissions = "{\"data\": [{\"installed\": 1,\"read_stream\": 1,\"read_mailbox\": 1,\"publish_actions\": 1,\"user_groups\": 1,\"user_events\": 1}]}";

		buttonPatron.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				if (submitting)
				{
					message("Submitting login, please wait for response...");
					return;
				}
				final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
				final LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View dialogView = inflater.inflate(R.layout.dialog_login_patron, null);
				builder.setView(dialogView);
				Button buttonDone = (Button)dialogView.findViewById(R.id.dialogLoginPatronButtonDone);
				Button buttonRequestPassword = (Button)dialogView.findViewById(R.id.dialogLoginPatronButtonRequestPassword);
				Button buttonCreateAccount = (Button)dialogView.findViewById(R.id.dialogLoginPatronButtonCreateAccount);
				final EditText fieldEmail = (EditText)dialogView.findViewById(R.id.dialogLoginPatronFieldEmail);
				final EditText fieldPassword = (EditText)dialogView.findViewById(R.id.dialogLoginPatronFieldPassword);

				// Prepopulate Login info
				fieldEmail.setText("mpacquiao@gmail.com");
				fieldPassword.setText("batman");

				final AlertDialog dialog = builder.create();

				listener.setDialog(dialog);
				listener.setFieldEmail(fieldEmail);
				listener.setFieldPassword(fieldPassword);

				buttonDone.setOnClickListener(listener);

				buttonRequestPassword.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view)
					{
						View dialogRequestPasswordView = inflater.inflate(R.layout.dialog_request_password, null);
						builder.setView(dialogRequestPasswordView);
						final AlertDialog dialogRequestPassword = builder.create();
						dialogRequestPassword.show();
					}
				});

				buttonCreateAccount.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view)
					{
						dialog.dismiss();
						Intent intent = new Intent(view.getContext(), FlashCreateAccount.class);
						startActivity(intent);
					}
				});

				// Show the dialog.
				dialog.show();
			}
		});
		buttonFacebook.setOnClickListener(new ButtonServiceListener(this, Provider.FACEBOOK));
		buttonTwitter.setOnClickListener(new ButtonServiceListener(this, Provider.TWITTER));
		buttonGooglePlus.setOnClickListener(new ButtonServiceListener(this, Provider.GOOGLEPLUS));
	}

	private boolean servicesConnected()
	{
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode)
		{
			// In debug mode, log the status
			System.out.println("Google Play services is available.");
			// Continue
			return true;
			// Google Play services was not available for some reason.
			// resultCode holds the error code.
		}
		else
		{
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			// If Google Play services can provide an error dialog
			if (errorDialog != null)
			{
				errorDialog.show();
				/*
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				// Set the dialog in the DialogFragment
				errorFragment.setDialog(errorDialog);
				// Show the error dialog in the DialogFragment
				errorFragment.show(getSupportFragmentManager(), "Location Updates");
				*/
			}
			return false;
		}
	}

	private class ButtonServiceListener implements OnClickListener
	{
		private Loadable activity;
		private Provider provider;

		public ButtonServiceListener(Loadable activity, Provider provider)
		{
			this.activity = activity;
			this.provider = provider;
		}

		@Override
		public void onClick(View view)
		{
			if (submitting)
			{
				message("Submitting login, please wait for response...");
				return;
			}
			submitting = true;

			// Authorize the user.
			try
			{
				socialAuthAdapter = new SocialAuthAdapter(new ResponseListener(activity));
				socialAuthAdapter.addConfig(Provider.FACEBOOK, "648356855200728", "261824005ee0bc926a82070e7bc9c575", "");
				socialAuthAdapter.addConfig(Provider.TWITTER, "zzj56RtJAssnuE9sSUl1NoeeT", "4QOqpgbwzZ15SLGINwUI8RNLyT98c7pZRbW3K7xt96aPx6mRdE", "");
				socialAuthAdapter.addConfig(Provider.GOOGLEPLUS, "342319301171-llpu8ildros0smcdmtdbpgg9ccsck6pl.apps.googleusercontent.com",
					"1lfGaHWYDN7SF7__Z9mDvbdZ", "openid");
				socialAuthAdapter.addCallBack(Provider.GOOGLEPLUS, "http://localhost");
				socialAuthAdapter.authorize(view.getContext(), provider);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			/*final Activity host = (Activity)view.getContext();
			host.runOnUiThread(new Runnable() {
				public void run() {
					ServiceConnector serviceConnector = new ServiceConnector(activity, host);
					serviceConnector.execute(provider);
				}
			});*/
		}
	}

	private class ButtonLoginPatronDoneListener implements OnClickListener
	{
		private AlertDialog dialog;
		private EditText fieldEmail;
		private EditText fieldPassword;
		private Loadable activity;

		public ButtonLoginPatronDoneListener(Loadable activity)
		{
			this.activity = activity;
		}

		public void setDialog(AlertDialog dialog)
		{
			this.dialog = dialog;
		}

		public void setFieldEmail(EditText fieldEmail)
		{
			this.fieldEmail = fieldEmail;
		}

		public void setFieldPassword(EditText fieldPassword)
		{
			this.fieldPassword = fieldPassword;
		}

		@Override
		public void onClick(View view)
		{
			if (!submitting)
			{
				submitting = true;
				String email = fieldEmail.getText().toString();
				String password = fieldPassword.getText().toString();
				ApiExecutor executor = new ApiExecutor(new OnTaskCompletedListener() {
					public void onComplete(Map<URI, HttpEntity> data)
					{
						ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
						this, buttonPatron, "loginButton");
						Bundle bundle = options.toBundle();
						Intent intent = new Intent(this, FlashVendors.class);
						this.finish();
						ActivityCompat.startActivity(this, intent, bundle);
					}
				});
				executor.loginPatron(email, password);
				/*
				LoginConnector loginConnector = new LoginConnector(activity, email, password);
				try
				{
					loginConnector.execute(new URL(ListLinks.LINK_ACCOUNT_LOGIN));
				}
				catch (MalformedURLException e)
				{
					e.printStackTrace();
				}
				*/
				dialog.dismiss();
			}
		}
	}

	private final class ResponseListener implements DialogListener
	{
		private Loadable activity;

		public ResponseListener(Loadable activity)
		{
			this.activity = activity;
		}

		@Override
		public void onComplete(Bundle bundle)
		{
			try
			{
				ServiceConnector serviceConnector = new ServiceConnector(activity, socialAuthAdapter);
				serviceConnector.execute(new URL(ListLinks.LINK_ACCOUNT_LOGIN));
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void onError(SocialAuthError error)
		{
		}

		@Override
		public void onCancel()
		{
		}

		@Override
		public void onBack()
		{
		}
	}

	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment
	{
		private Dialog dialog;
		public ErrorDialogFragment()
		{
			super();
			dialog = null;
		}
		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			this.dialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			return dialog;
		}
	}
}
