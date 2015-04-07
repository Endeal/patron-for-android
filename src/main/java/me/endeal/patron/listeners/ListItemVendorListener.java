package me.endeal.patron.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import me.endeal.patron.main.FlashMenu;
import me.endeal.patron.model.Vendor;
import me.endeal.patron.system.Globals;

public class ListItemVendorListener implements OnItemClickListener
{
	public void onItemClick(AdapterView<?> adapter, View v, int item,
			long row)
	{
		Vendor vendor = Globals.getVendors().get(item);
		Globals.setVendor(vendor);
		/*
		Activity activity = (Activity)v.getContext();
		Intent intent = new Intent(v.getContext(), FlashMenu.class);
		activity.startActivity(intent);
		*/
		Activity activity = (Activity)v.getContext();
		activity.finish();
	}
}