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
import com.patron.bind.PaymentBinder;
import com.patron.db.AddOrderConnector;
import com.patron.listeners.ButtonFinishListener;
import com.patron.lists.ListLinks;
import com.patron.model.Fragment;
import com.patron.model.Funder;
import com.patron.model.Station;
import com.patron.model.Order;
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

import java.lang.Exception;
import com.google.gson.Gson;

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

		try
		{
			Gson gson = new Gson();
			System.out.println("ON CREATE:" + gson.toJson(Globals.getOrder().getFragments()));
		}
		catch (Exception e)
		{
			System.out.println("ERROR JSONING ORDER!");
		}

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
		try
		{
			Gson gson = new Gson();
			System.out.println("BEGIN LOADING:" + gson.toJson(Globals.getOrder()));
		}
		catch (Exception e)
		{
			System.out.println("ERROR JSONING ORDER!");
		}

		listCart = (ListView) viewCart.findViewById(R.id.cartListItems);
		buttonFinish = (Button) viewCart.findViewById(R.id.cartButtonFinish);
		buttonFinish.setOnClickListener(new ButtonFinishListener(this));
		endLoading();
	}
	
	public void load()
	{
	}
	
	public void endLoading()
	{
		try
		{
			Gson gson = new Gson();
			System.out.println("END LOADING:" + gson.toJson(Globals.getOrder()));
		}
		catch (Exception e)
		{
			System.out.println("ERROR JSONING ORDER!");
		}

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

		try
		{
			Gson gson = new Gson();
			System.out.println("UPON UPDATING:" + gson.toJson(Globals.getOrder()));
		}
		catch (Exception e)
		{
			System.out.println("ERROR JSONING ORDER!");
		}
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
			buttonStation.setText("Station:\n" + Globals.getOrder().getStation().getName());
			String number = Globals.getOrder().getFunder().getNumber();
			buttonPayment.setText("Payment:\n" + number.substring(number.length() - 4));
			buttonTip.setText("Tip:\n$" + Globals.getOrder().getTip().toString());
			buttonCoupon.setText("Coupons:\n" + Globals.getOrder().getCoupons().size());
			String commented = "No";
			if (Globals.getOrder().getComment().length() > 0)
			{
				commented = "Yes";
			}
			buttonComment.setText("Comment:\n" + commented);

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
					listStations.setAdapter(adapter);
					dialog.show();

					// Set the station when an item is clicked.
					listStations.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view, int position, long id)
						{
							Station station = Globals.getVendor().getStations().get(position);
							Globals.getOrder().setStation(station);
							dialog.dismiss();
							update();
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

			// Set up payment button
			buttonPayment.setOnClickListener(new OnClickListener() {
				public void onClick(View view)
				{
					final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
					final LayoutInflater inflater = (LayoutInflater)view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View dialogView = inflater.inflate(R.layout.dialog_payment, null);
					builder.setView(dialogView);
					ListView listPayment = (ListView)dialogView.findViewById(R.id.dialogPaymentListMain);
					Button buttonCancel = (Button)dialogView.findViewById(R.id.dialogPaymentButtonCancel);
					final AlertDialog dialog = builder.create();

					// Set up the payment dialog list
					List<Map<String, String>> payments = new ArrayList<Map<String, String>>();
					String[] from = {"number", "bankName", "type", "address"};
					int[] to = {R.id.paymentListItemTextNumber,R.id.paymentListItemTextBankName,
						R.id.paymentListItemTextType, R.id.paymentListItemTextAddress};
					for (int i = 0; i < Globals.getUser().getFunders().size(); i++)
					{	
						Map<String, String> mapping = new HashMap<String, String>();
						Funder funder = Globals.getUser().getFunders().get(i);
						mapping.put("number", funder.getNumber());
						mapping.put("bankName", funder.getBankName());
						mapping.put("type", funder.getType().substring(0, 1).toUpperCase() + funder.getType().substring(1));
						if (funder.getAddress() == null || funder.getCity() == null || funder.getState() == null)
							mapping.put("address", "");
						else
							mapping.put("address", funder.getAddress() + ", " + funder.getCity() + " " + funder.getState());
						payments.add(mapping);
					}
					SimpleAdapter adapter = new SimpleAdapter(view.getContext(), payments, R.layout.list_item_payment, from, to);
					adapter.setViewBinder(new PaymentBinder());
					listPayment.setAdapter(adapter);
					dialog.show();

					listPayment.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id)
						{
							if (Globals.getUser() != null && Globals.getUser().getFunders() != null &&
								Globals.getUser().getFunders().size() > position)
							{
								Funder funder = Globals.getUser().getFunders().get(position);
								Globals.getOrder().setFunder(funder);
								dialog.dismiss();
								update();
							}
						}
					});

					// Set up the payment dialog cancel button
					buttonCancel.setOnClickListener(new OnClickListener() {
						@Override
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
					if (!Globals.getOrder().getTip().toString().equals("0.00"))
					{
						fieldCustom.setText(Globals.getOrder().getTip().toString());
					}
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
							try  
  							{  
    							double d = Double.parseDouble(fieldCustom.getText().toString());
    							tip = new BigDecimal(d);
  							}  
  							catch(NumberFormatException nfe)  
  							{  
    							tip = new BigDecimal("0.00");
  							}
							tip = tip.setScale(2, RoundingMode.CEILING);
							Globals.getOrder().setTip(tip);
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
					if (Globals.getOrder().getComment().length() > 0)
					{
						fieldComment.setText(Globals.getOrder().getComment());
					}
					buttonDone.setOnClickListener(new OnClickListener() {
						public void onClick(View view)
						{
							Globals.getOrder().setComment(fieldComment.getText().toString());
							dialog.dismiss();
							update();
						}
					});
					dialog.show();
				}
			});
    		
    		// Set up pay button
			typeface = Typeface.createFromAsset(getAssets(), ListFonts.FONT_MAIN_BOLD);
    		String text = (String) buttonFinish.getText();
    		text = "Pay Now: $" + Globals.getOrder().getPrice().toString();
    		buttonFinish.setTypeface(typeface);
    		buttonFinish.setText(text);
    		buttonFinish.setOnClickListener(new OnClickListener() {
    			public void onClick(View view)
    			{
    				try
					{
						Gson gson = new Gson();
						System.out.println("BEFORE CLICK FINISHES:" + gson.toJson(Globals.getOrder()));
					}
					catch (Exception e)
					{
						System.out.println("ERROR JSONING ORDER!");
					}

    				finishOrder();

    				try
					{
						Gson gson = new Gson();
						System.out.println("AFTER CLICK FINISHES:" + gson.toJson(Globals.getOrder()));
					}
					catch (Exception e)
					{
						System.out.println("ERROR JSONING ORDER!");
					}
    			}
    		});
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
