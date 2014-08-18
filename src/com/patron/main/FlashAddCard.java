package com.patron.main;

import java.lang.Exception;
import java.util.ArrayList;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.widget.EditText;

import com.patron.system.Loadable;

public class FlashAddCard extends ActionBarActivity implements Loadable
{
	private boolean submitting = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_add_card);
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
		Button buttonSubmit = (Button)findViewById(R.id.addCardButtonSubmit);
		final RelativeLayout layout = (RelativeLayout)findViewById(R.id.addCardLayoutMain);
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

					Intent intent = new Intent(view.getContext(), FlashHome.class);
					startActivity(intent);
				}
			}
		});
	}
}
