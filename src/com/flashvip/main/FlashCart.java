/**
 * The Cart screen.
 */
package com.flashvip.main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.flashvip.bind.CartProductBinder;
import com.flashvip.db.AddOrderConnector;
import com.flashvip.listeners.ButtonFinishListener;
import com.flashvip.lists.ListLinks;
import com.flashvip.model.Fragment;
import com.flashvip.system.Globals;
import com.flashvip.system.Loadable;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class FlashCart extends ActionBarActivity implements Loadable
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
    
	// The layout elements.
	Button buttonFinish;
	ListView listCart;
	View viewLoading;
	View viewCart;
	View viewNone;
	
	// Activity Methods
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
        // Set up the PayPal Service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, CONFIG_ENVIRONMENT);
        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
        //intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, Globals.getVendor().getPaypal());
        startService(intent);
		
		LayoutInflater inflater = LayoutInflater.from(this);
		viewLoading = inflater.inflate(R.layout.misc_loading, null);
		viewCart = inflater.inflate(R.layout.layout_cart, null);
		viewNone = inflater.inflate(R.layout.misc_no_items_cart, null);
		setContentView(viewLoading);
		beginLoading();
	}
	
	@Override
	public void onDestroy()
	{
	    stopService(new Intent(this, PayPalService.class));
	    super.onDestroy();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch (item.getItemId())
    	{
    	case R.id.menuItemSettings:
    		Intent intentSettings = new Intent(this, FlashSettings.class);
    		startActivity(intentSettings);
    		return true;
    	case R.id.menuItemHelp:
    		Intent intentHelp = new Intent(this, FlashHelp.class);
    		startActivity(intentHelp);
    		return true;
    	default:
    		return false;
    	}
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null)
            {
                try
                {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.
                    setContentView(viewLoading);
        			AddOrderConnector addOrderConnector = new AddOrderConnector(this);
        			URL url;
        			try
        			{
        				url = new URL(ListLinks.LINK_ADD_ORDER);
        				addOrderConnector.execute(url);
        			}
        			catch (MalformedURLException e)
        			{
        				Toast toast = Toast.makeText(this, "Connection error.", Toast.LENGTH_SHORT);
        				toast.show();
        			}

                }
                catch (JSONException e)
                {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED)
        {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID)
        {
            Log.i("paymentExample", "An invalid payment was submitted. Please see the docs.");
        }
    }
	
	// Getters & Setters
	public Activity getActivity()
	{
		return this;
	}
	
	// Layout
	public void beginLoading()
	{
		listCart = (ListView) viewCart.findViewById(R.id.cartListItems);
		buttonFinish = (Button) viewCart.findViewById(R.id.cartButtonFinish);
		buttonFinish.setOnClickListener(new ButtonFinishListener(this));
		load();
	}
	
	public void load()
	{
		endLoading();
	}
	
	public void endLoading()
	{
		if (Globals.getOrder() != null &&
				Globals.getOrder().getFragments() != null &&
				Globals.getOrder().getFragments().size() > 0)
		{
			setContentView(viewCart);
		}
		else
		{
			setContentView(viewNone);
		}
		update();
	}
	
	public void update()
	{
		// Set main list items to a list of drinks.
    	if (Globals.getOrder().getFragments() != null &&
    			!Globals.getOrder().getFragments().isEmpty())
    	{
    		List<Map<String, Fragment>> products = new ArrayList<Map<String, Fragment>>();
    		
    		String[] from = {"name",
    				"price",
    				"quantity",
    				"categories",
    				"buttonRemove",
    				"layout"};
    		
    		int[] to = {R.id.cartListItemTextName,
    				R.id.cartListItemTextPrice,
    				R.id.cartListItemSpinnerQuantity,
    				R.id.cartListItemTextCategories,
    				R.id.cartListItemButtonRemove,
    				R.id.cartListItemLayout};
    		
    		for (int i = 0; i < Globals.getOrder().getFragments().size(); i++)
    		{	
        		Map<String, Fragment> mapping = new HashMap<String, Fragment>();
    			Fragment fragment = Globals.getOrder().getFragments().get(i);
    			mapping.put("name", fragment);
    			mapping.put("price", fragment);
    			mapping.put("quantity", fragment);
    			mapping.put("categories", fragment);
    			mapping.put("buttonRemove", fragment);
    			mapping.put("layout", fragment);
    			products.add(mapping);
    		}
    		SimpleAdapter adapter = new SimpleAdapter(this,
    				products, R.layout.list_item_cart, from, to);
    		adapter.setViewBinder(new CartProductBinder(this));
    		listCart.setAdapter(adapter);
    		
    		// Set the total price text view.
    		String text = (String) buttonFinish.getText();
    		text = "Close Tab: $" + Globals.getOrder().getPrice().toString();
    		buttonFinish.setText(text);
    	}
    	else
    	{
    		setContentView(viewNone);
    	}
	}
	
	public void orderFinished()
	{
		Intent intent = new Intent(this, FlashHome.class);
		startActivity(intent);
	}
	
	public void message(String msg)
	{
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
}
