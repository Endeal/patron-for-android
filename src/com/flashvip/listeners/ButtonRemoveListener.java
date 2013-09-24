package com.flashvip.listeners;

import android.view.View;

import android.view.View.OnClickListener;
import android.widget.Toast;

import com.flashvip.main.FlashCart;
import com.flashvip.main.Globals;
import com.flashvip.main.CartProduct;

public class ButtonRemoveListener implements OnClickListener
{	
	private FlashCart activity;
	private CartProduct product;
	
	public ButtonRemoveListener(FlashCart activity, CartProduct product)
	{
		this.activity = activity;
		this.product = product;
	}
	
	public void onClick(View v)
	{
		if (Globals.getCartProducts() != null)
		{
			Globals.removeProductFromCart(product);
			Toast toast = Toast.makeText(activity,
					"Order removed from cart.", Toast.LENGTH_SHORT);
			toast.show();
		}
		activity.updateListViewCart();
	}
}