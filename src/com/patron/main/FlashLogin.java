package com.patron.main;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.patron.system.Loadable;

public class FlashLogin extends ActionBarActivity implements Loadable
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);
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
	
}
