package com.flashvip.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewAnimator;

public class FlashHelp extends ActionBarActivity
{
	// Layout elements.
	private View viewMain;
	private ViewAnimator viewAnimator;
	private ImageButton imageButtonNext;
	private ImageButton imageButtonPrevious;
	private Button buttonStart;
	private TextView textPage;
	private int page;
	
	// Activity methods.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(this);
        viewMain = inflater.inflate(R.layout.layout_help, null);
        viewAnimator = (ViewAnimator)viewMain.findViewById(R.id.helpViewAnimatorMain);
        imageButtonNext = (ImageButton)viewMain.findViewById(R.id.helpImageButtonNext);
        imageButtonPrevious = (ImageButton)viewMain.findViewById(R.id.helpImageButtonPrevious);
        buttonStart = (Button)viewMain.findViewById(R.id.helpButtonStart);
        textPage = (TextView)viewMain.findViewById(R.id.helpTextPage);
        imageButtonPrevious.setVisibility(View.INVISIBLE);
        page = 1;
        
        // Set up the next button.
        imageButtonNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (page < 5)
				{
					page++;
					viewAnimator.setInAnimation(view.getContext(), R.anim.slide_in_right);
		        	viewAnimator.setOutAnimation(view.getContext(), R.anim.slide_out_left);
					viewAnimator.showNext();
					if (page == 5)
					{
						imageButtonNext.setVisibility(View.INVISIBLE);
					}
					else
					{
						imageButtonNext.setVisibility(View.VISIBLE);
					}
					textPage.setText(page + " / 5");
				}
				imageButtonPrevious.setVisibility(View.VISIBLE);
			}
		});
        
        // Set up the previous button.
        imageButtonPrevious.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (page > 1)
				{
					page--;
					viewAnimator.setInAnimation(view.getContext(), R.anim.slide_in_left);
					viewAnimator.setOutAnimation(view.getContext(), R.anim.slide_out_right);
					viewAnimator.showPrevious();
					if (page == 1)
					{
						imageButtonPrevious.setVisibility(View.INVISIBLE);
					}
					else
					{
						imageButtonPrevious.setVisibility(View.VISIBLE);
					}
					textPage.setText(page + " / 5");
				}
				imageButtonNext.setVisibility(View.VISIBLE);
			}
		});
        
        // Set up the start button.
        buttonStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), FlashVendors.class);
				view.getContext().startActivity(intent);
			}
		});
        
        setContentView(viewMain);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_help, menu);
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
    	default:
    		return false;
    	}
    }
	
	@Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
    	super.onWindowFocusChanged(hasFocus);
    }
    
}