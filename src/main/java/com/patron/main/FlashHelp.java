package com.patron.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.patron.listeners.DrawerNavigationListener;
import com.patron.R;
import com.patron.view.NavigationListView;
import static com.patron.view.NavigationListView.Hierarchy;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashHelp extends Activity
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

		// Set up the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) viewMain.findViewById(R.id.helpDrawerNavigation);
		NavigationListView listNavigation = (NavigationListView) viewMain.findViewById(R.id.helpListNavigation);
		DrawerNavigationListener drawerNavigationListener = new DrawerNavigationListener(this);
		drawerLayoutNavigation.setDrawerListener(drawerNavigationListener);
		listNavigation.setHierarchy(drawerNavigationListener, drawerLayoutNavigation, Hierarchy.SETTINGS);

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
}
