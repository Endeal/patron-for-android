package com.flashvip.listeners;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.flashvip.main.FlashHelp;

public class DialogTutorialListener implements DialogInterface.OnClickListener
{
	private Context context;

	public DialogTutorialListener(Context context)
	{
		this.context = context;
	}

	public void onClick(DialogInterface dialog, int selection) {
		switch (selection)
		{
		case DialogInterface.BUTTON_POSITIVE:
			Intent intent = new Intent(context, FlashHelp.class);
			context.startActivity(intent);
			break;

		case DialogInterface.BUTTON_NEGATIVE:
			break;
		}
	}
}
