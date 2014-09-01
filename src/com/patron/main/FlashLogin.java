package com.patron.main;

import java.lang.Exception;
import java.net.URL;
import java.net.MalformedURLException;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageButton;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.patron.system.Loadable;
import com.patron.lists.ListLinks;
import com.patron.db.LoginConnector;

import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthError;

public class FlashLogin extends ActionBarActivity implements Loadable
{
	private boolean submitting;
	private ProgressBar progressBar;
	private SocialAuthAdapter socialAuthAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);
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
	}

	@Override
	public void update()
	{
		Intent intent = new Intent(this, FlashHome.class);
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
		Button buttonCreateAccount = (Button)findViewById(R.id.loginButtonCreateAccount);
		ImageButton buttonPatron = (ImageButton)findViewById(R.id.loginButtonPatron);
		ImageButton buttonFacebook = (ImageButton)findViewById(R.id.loginButtonFacebook);
		ImageButton buttonTwitter = (ImageButton)findViewById(R.id.loginButtonTwitter);
		ImageButton buttonGooglePlus = (ImageButton)findViewById(R.id.loginButtonGooglePlus);
		socialAuthAdapter = new SocialAuthAdapter(new ResponseListener());
		final ButtonLoginPatronDoneListener listener = new ButtonLoginPatronDoneListener(this);

		// Configure the adapter with the API keys & secrets.
		// String permissions = "{\"data\": [{\"installed\": 1,\"read_stream\": 1,\"read_mailbox\": 1,\"publish_actions\": 1,\"user_groups\": 1,\"user_events\": 1}]}";
		String secretGooglePlus = "1lfGaHWYDN7SF7__Z9mDvbdZ";
		try
		{
			socialAuthAdapter.addConfig(Provider.FACEBOOK, "648356855200728", "261824005ee0bc926a82070e7bc9c575", "");
			socialAuthAdapter.addConfig(Provider.TWITTER, "zzj56RtJAssnuE9sSUl1NoeeT", "4QOqpgbwzZ15SLGINwUI8RNLyT98c7pZRbW3K7xt96aPx6mRdE", "");
			socialAuthAdapter.addConfig(Provider.GOOGLEPLUS, "342319301171-llpu8ildros0smcdmtdbpgg9ccsck6pl.apps.googleusercontent.com",
				secretGooglePlus, "openid");

			socialAuthAdapter.addCallBack(Provider.GOOGLEPLUS, "http://localhost");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// Set the actions for each button.
		buttonCreateAccount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(view.getContext(), FlashCreateAccount.class);
				startActivity(intent);
			}
		});
		buttonPatron.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
				LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View dialogView = inflater.inflate(R.layout.dialog_login_patron, null);
				builder.setView(dialogView);
				Button buttonCancel = (Button)dialogView.findViewById(R.id.dialogLoginPatronButtonCancel);
				Button buttonDone = (Button)dialogView.findViewById(R.id.dialogLoginPatronButtonDone);
				final EditText fieldEmail = (EditText)dialogView.findViewById(R.id.dialogLoginPatronFieldEmail);
				final EditText fieldPassword = (EditText)dialogView.findViewById(R.id.dialogLoginPatronFieldPassword);
				final AlertDialog dialog = builder.create();

				listener.setDialog(dialog);
				listener.setFieldEmail(fieldEmail);
				listener.setFieldPassword(fieldPassword);

				// Set the cancel and done listeners.
				buttonCancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view)
					{
						dialog.dismiss();
					}
				});

				buttonDone.setOnClickListener(listener);

				// Show the dialog.
				dialog.show();
				// fieldEmail.requestFocus();
				// view.getContext().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
			}
		});
		buttonFacebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				socialAuthAdapter.authorize(view.getContext(), Provider.FACEBOOK);
			}
		});
		buttonTwitter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				socialAuthAdapter.authorize(view.getContext(), Provider.TWITTER);
			}
		});
		buttonGooglePlus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{
				socialAuthAdapter.authorize(view.getContext(), Provider.GOOGLEPLUS);
			}
		});
	}
	
	private final class ResponseListener implements DialogListener
	{
		@Override
		public void onComplete(Bundle bundle)
		{
			System.out.println("Success FROGS!");
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

				System.out.println("POOP2EMAIL:" + email + "\nPOOP2PASS:" + password);
				LoginConnector loginConnector = new LoginConnector(activity, email, password);
				try
				{
					loginConnector.execute(new URL(ListLinks.LINK_ACCOUNT_LOGIN));
				}
				catch (MalformedURLException e)
				{
					e.printStackTrace();
				}
				dialog.dismiss();
			}
		}
	}
}
