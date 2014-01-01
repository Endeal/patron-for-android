package com.flashvip.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.flashvip.main.FlashMenu;
import com.flashvip.model.Vendor;
import com.flashvip.system.Globals;

public class ListItemVendorListener implements OnItemClickListener
{
	public void onItemClick(AdapterView<?> adapter, View v, int item,
			long row)
	{
		Vendor vendor = Globals.getVendors().get(item);
		Globals.setVendor(vendor);
		Activity activity = (Activity)v.getContext();
		Intent intent = new Intent(v.getContext(), FlashMenu.class);
		activity.startActivity(intent);
	}
}