package com.flashvip.listeners;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.flashvip.main.FlashCart;
import com.flashvip.system.Globals;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PaymentActivity;

public class ButtonFinishListener implements View.OnClickListener
{
    /*
     * Set to PaymentActivity.ENVIRONMENT_SANDBOX to use your test credentials from https://developer.paypal.com
     * Set to PaymentActivity.ENVIRONMENT_NO_NETWORK to kick the tires without communicating to PayPal's servers.
     * Set to PaymentActivity.ENVIRONMENT_PRODUCTION to move real money.
     */
    private static final String CONFIG_ENVIRONMENT = PaymentActivity.ENVIRONMENT_SANDBOX;
    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AUaMXxAo8v_WKb36Necty4PXiHsZbz0im7yIIpxGm-T9XsY3r4GOs3lnJLxN";
    // when testing in sandbox, this is likely the -facilitator email address.
    
	FlashCart activity;
	
	public ButtonFinishListener(FlashCart activity)
	{
		this.activity = activity;
	}
	
	public void onClick(View view)
	{
		if (Globals.getOrder() != null &&
				Globals.getOrder().getFragments() != null &&
				!Globals.getOrder().getFragments().isEmpty())
		{
			/*
			OnClickListener dialogClickListener =new DialogFinishListener(activity, this);
			AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
			builder.setMessage("Are you sure?").setPositiveButton("Yes",
					dialogClickListener).setNegativeButton("No", dialogClickListener).show();
					*/
	        PayPalPayment purchase = new PayPalPayment(Globals.getOrder().getPrice().setScale(2), "USD", 
	        		Globals.getOrder().getOrderText());
	        Intent intent = new Intent(activity, PaymentActivity.class);
	        intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, CONFIG_ENVIRONMENT);
	        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
	        intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, Globals.getVendor().getPaypal());
	        
	        // It's important to repeat the clientId here so that the SDK has it if Android restarts your 
	        // app midway through the payment UI flow.
	        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "AUaMXxAo8v_WKb36Necty4PXiHsZbz0im7yIIpxGm-T9XsY3r4GOs3lnJLxN");
	        intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, Globals.getDeviceId());
	        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, purchase);
	        
	        activity.startActivityForResult(intent, 0);
		}
		else
		{
			Toast error = Toast.makeText(view.getContext(),
					"No drinks added to tab yet.",
					Toast.LENGTH_SHORT);
			error.show();
		}
	}
}
