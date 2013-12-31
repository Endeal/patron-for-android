package com.flashvip.main;

import java.net.MalformedURLException;
import java.net.URL;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.flashvip.db.ScanConnector;
import com.flashvip.lists.ListLinks;
import com.flashvip.model.Order;
import com.flashvip.system.Globals;
import com.flashvip.system.Loadable;

public class FlashScan extends ActionBarActivity implements Loadable
{
	// The layout elements.
	private ImageView imageCode;
	private View viewLoading;
	private View viewScan;
	private Order order;
	
	// Activity
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
		{
			String orderRow = bundle.getString("orderRow");
			int row = Integer.parseInt(orderRow);
			order = Globals.getCodes().get(row).getOrder();
		}
		LayoutInflater inflater = LayoutInflater.from(this);
		viewLoading = inflater.inflate(R.layout.misc_loading, null);
		viewScan = inflater.inflate(R.layout.layout_scan, null);
		setContentView(viewLoading);
		beginLoading();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_main, menu);
	    return super.onCreateOptionsMenu(menu);
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
    public void onWindowFocusChanged(boolean hasFocus)
    {
    	super.onWindowFocusChanged(hasFocus);
    }
	
	// Loading
	public void beginLoading()
	{
		imageCode = (ImageView) viewScan.findViewById(R.id.codeImageImageViewCode);
		load();
	}
	
	public void load()
	{
		URL url = null;
		try
		{
			try
			{
				url = new URL(ListLinks.LINK_GET_SCAN);
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			ScanConnector codeImageConnector = new ScanConnector(this, order);
			codeImageConnector.execute(url);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void endLoading()
	{
		if (Globals.getScan() != null)
		{
			setContentView(viewScan);
			
			ViewTreeObserver vto = viewScan.getViewTreeObserver(); 
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
				@Override 
				public void onGlobalLayout()
				{
					// Set and resize the image.
					int margin = 20;
					imageCode.setImageBitmap(Globals.getScan());
					LinearLayout layout = (LinearLayout)viewScan.findViewById(R.id.scanLayoutMain);
					LayoutParams params = new LayoutParams(layout.getWidth() - margin,
							layout.getWidth() - margin);
					params.setMargins(margin, margin, margin, margin);
					imageCode.setLayoutParams(params);
					
					// Set the order text.
					String text = order.getOrderText();
					TextView textView = (TextView)viewScan.findViewById(R.id.scanTextOrder);
					textView.setText(text);
				} 
			});
		}
		else
		{
			setContentView(R.layout.misc_no_scan);
		}
		update();
	}
	
	public void update()
	{
	}
	
	public void message(String msg)
	{
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
}
