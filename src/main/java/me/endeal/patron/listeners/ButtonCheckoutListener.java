package me.endeal.patron.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import me.endeal.patron.main.FlashCart;
import me.endeal.patron.system.Globals;
import me.endeal.patron.system.Patron;

import java.math.BigDecimal;

public class ButtonCheckoutListener implements OnClickListener
{
	Activity activity;

	public ButtonCheckoutListener(Activity activity)
	{
		this.activity = activity;
	}

	public void onClick(View v)
	{
        // Start the new activity.
		if (Globals.getOrder() != null && Globals.getOrder().getFragments() != null &&
            !Globals.getOrder().getFragments().isEmpty())
		{
            // Set the default tip.
            Context context = Patron.getContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences("me.endeal.patron", Context.MODE_PRIVATE);
            String defaultTip = sharedPreferences.getString("tip", "0.00");
            BigDecimal newTip = Globals.getOrder().getPrice().multiply(new BigDecimal(defaultTip)).setScale(2, BigDecimal.ROUND_DOWN);
            Globals.getOrder().setTip(newTip);

			Intent intent = new Intent(activity, FlashCart.class);
			activity.startActivity(intent);
		}
		else
		{
			Toast toast = Toast.makeText(v.getContext(), "No items have been added to your cart.", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}
