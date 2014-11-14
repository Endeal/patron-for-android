/**
 * The Cart screen.
 */
package com.patron.main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.json.JSONException;

import com.patron.bind.CartProductBinder;
import com.patron.db.AddOrderConnector;
import com.patron.listeners.ButtonFinishListener;
import com.patron.lists.ListLinks;
import com.patron.model.Fragment;
import com.patron.model.Funder;
import com.patron.model.Station;
import com.patron.system.Globals;
import com.patron.lists.ListFonts;
import com.patron.system.Loadable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.graphics.Typeface;

public class FlashCart extends ActionBarActivity implements Loadable
{
	// The layout elements.
	Button buttonFinish;
	ListView listCart;
	View viewLoading;
	View viewCart;
	View viewNone;

	// The comment for the order.
	public static Station station = null;
	public static Funder funder = null;
	public static BigDecimal tip = null;
	public static List<Object> coupons = null;
	public static String comment = "";
	
	// Activity Methods
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
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
    
    public void finishOrder()
    {
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

		// Set default cart values.
		Station station = new Station("-1", "(none)");
		Funder funder = null;
		BigDecimal tip = new BigDecimal("0.00");
		List<Object> coupons = new ArrayList<Object>();
		String comment = "";
		if (Globals.getVendor().getStations() != null && Globals.getVendor().getStations().size() > 0)
		{
			station = Globals.getVendor().getStations().get(0);
		}
		if (Globals.getUser().getFunders() != null && Globals.getUser().getFunders().size() > 0)
		{
			funder = Globals.getUser(). getFunders().get(0);
		}
		FlashCart.station = station;
		FlashCart.funder  = funder;
		FlashCart.tip = tip;
		FlashCart.coupons = coupons;
		FlashCart.comment = comment;

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

    		// Set up the cart controls.
			Typeface typeface = Typeface.createFromAsset(getAssets(), ListFonts.FONT_MAIN_LIGHT);
    		Button buttonStation = (Button)findViewById(R.id.cartButtonStation);
    		Button buttonPayment = (Button)findViewById(R.id.cartButtonPayment);
    		Button buttonTip = (Button)findViewById(R.id.cartButtonTip);
    		Button buttonCoupon = (Button)findViewById(R.id.cartButtonCoupon);
    		Button buttonComment = (Button)findViewById(R.id.cartButtonComment);
			buttonStation.setTypeface(typeface);
			buttonPayment.setTypeface(typeface);
			buttonTip.setTypeface(typeface);
			buttonCoupon.setTypeface(typeface);
			buttonComment.setTypeface(typeface);
			buttonStation.setText("Station:\n" + FlashCart.station.getName());
			buttonPayment.setText("Payment:\n" + FlashCart.funder.getNumber());
			buttonTip.setText("Tip:\n$" + FlashCart.tip.toString());
			buttonCoupon.setText("Coupons:\n" + FlashCart.coupons.size());

			// Set up station button
			buttonStation.setOnClickListener(new OnClickListener() {
				public void onClick(View view)
				{
					final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
					final LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View dialogView = inflater.inflate(R.layout.dialog_stations, null);
					builder.setView(dialogView);
					ListView listStations = (ListView)dialogView.findViewById(R.id.dialogStationsListMain);
					Button buttonCancel = (Button)dialogView.findViewById(R.id.dialogStationsButtonCancel);
					final AlertDialog dialog = builder.create();
					String[] stations = new String[Globals.getVendor().getStations().size()];
					for (int i = 0; i < Globals.getVendor().getStations().size(); i++)
					{
						Station station = Globals.getVendor().getStations().get(i);
						stations[i] = station.getName();;
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, stations);
					if (listStations == null)
						System.out.println("POOPY");
					if (adapter == null)
						System.out.println("URINE");
					listStations.setAdapter(adapter);
					dialog.show();

					// Set the station when an item is clicked.
					listStations.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view, int position, long id)
						{
							FlashCart.station = Globals.getVendor().getStations().get(position);
							dialog.dismiss();
						}
					});

					// Set the button to close the dialog when canceled.
					buttonCancel.setOnClickListener(new OnClickListener() {
						public void onClick(View view)
						{
							dialog.dismiss();
						}
					});
				}
			});

			// Set up tip button
			buttonTip.setOnClickListener(new OnClickListener() {
				public void onClick(View view)
				{
					final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
					final LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View dialogView = inflater.inflate(R.layout.dialog_tip, null);
					builder.setView(dialogView);
					Button buttonDone = (Button)dialogView.findViewById(R.id.dialogTipButtonDone);
					final EditText fieldCustom = (EditText)dialogView.findViewById(R.id.dialogTipFieldCustom);
					final SeekBar seekBarPercent = (SeekBar)dialogView.findViewById(R.id.dialogTipSeekBarPercent);
					final TextView textPercent = (TextView)dialogView.findViewById(R.id.dialogTipTextPercent);
					final AlertDialog dialog = builder.create();
					seekBarPercent.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
						{
							BigDecimal value = Globals.getOrder().getPrice().multiply(new BigDecimal((progress / 100.0) + ""));
							value = value.setScale(2, RoundingMode.CEILING);
							fieldCustom.setText(value.toString());
							textPercent.setText(progress + "%");
						}
						public void onStartTrackingTouch(SeekBar seekBar) {}
						public void onStopTrackingTouch(SeekBar seekBar) {}
					});
					buttonDone.setOnClickListener(new OnClickListener() {
						public void onClick(View view)
						{
							FlashCart.tip = new BigDecimal(fieldCustom.getText().toString());
							dialog.dismiss();
							update();
						}
					});
					dialog.show();
				}
			});

			// Set up comment button
			buttonComment.setOnClickListener(new OnClickListener() {
				public void onClick(View view)
				{
					final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
					final LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View dialogView = inflater.inflate(R.layout.dialog_comment, null);
					builder.setView(dialogView);
					Button buttonDone = (Button)dialogView.findViewById(R.id.dialogCommentButtonDone);
					final EditText fieldComment = (EditText)dialogView.findViewById(R.id.dialogCommentFieldComment);
					final AlertDialog dialog = builder.create();
					buttonDone.setOnClickListener(new OnClickListener() {
						public void onClick(View view)
						{
							FlashCart.comment = fieldComment.getText().toString();
							dialog.dismiss();
							update();
						}
					});
					dialog.show();
				}
			});
    		
    		// Set the total price text view.
			typeface = Typeface.createFromAsset(getAssets(), ListFonts.FONT_MAIN_BOLD);
    		String text = (String) buttonFinish.getText();
    		text = "Pay Now: $" + Globals.getOrder().getPrice().toString();
    		buttonFinish.setTypeface(typeface);
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
