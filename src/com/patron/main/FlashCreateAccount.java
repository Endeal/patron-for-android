package com.patron.main;

import java.lang.Exception;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ProgressBar;
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
		Button buttonCancel = (Button)findViewById(R.id.createAccountButtonCancel);
		Button buttonSubmit = (Button)findViewById(R.id.createAccountButtonSubmit);

		// Set the actions for each button.
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
				ProgressBar progressIndicator = new ProgressBar(view.getContext());
				layout.addView(progressIndicator);
			}
		});
	}
}
