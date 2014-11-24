package com.patron.main;

import java.lang.Exception;
import java.lang.Runnable;
import java.net.URL;
import java.net.MalformedURLException;

import android.os.Bundle;
import android.app.Activity;
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

import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.Logger;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

import com.patron.system.Loadable;
import com.patron.lists.ListLinks;
import com.patron.db.LoginConnector;
import com.patron.db.ServiceConnector;

import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
import org.brickred.socialauth.Profile;

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

		// Initialize device ID
		/*
		AirshipConfigOptions options = AirshipConfigOptions.loadDefaultOptions(this);
        UAirship.takeOff(this, options);
        PushManager.enablePush();
        String apid = PushManager.shared().getAPID();
        Globals.setDeviceId(apid);
		PushManager.shared().setIntentReceiver(FlashIntentReceiver.class);
		System.out.println(apid);
		*/
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
		ImageButton buttonPatron = (ImageButton)findViewById(R.id.loginButtonPatron);
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
}
