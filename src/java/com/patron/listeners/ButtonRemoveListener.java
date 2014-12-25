package com.patron.listeners;

import android.view.View;
import android.view.View.OnClickListener;

import com.patron.model.Fragment;
import com.patron.system.Globals;
import com.patron.system.Loadable;

public class ButtonRemoveListener implements OnClickListener
{	
	private Loadable activity;
	private Fragment fragment;
	
	public ButtonRemoveListener(Loadable activity, Fragment fragment)
	{
		this.activity = activity;
		this.fragment = fragment;
	}
	
	public void onClick(View v)
	{
		if (Globals.getOrder() != null &&
				Globals.getOrder().getFragments() != null)
		{
			Globals.getOrder().getFragments().remove(fragment);
		}
		activity.update();
	}
}