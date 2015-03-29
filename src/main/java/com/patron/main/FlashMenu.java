/**
 * The Menu screen.
 * @author James Whiteman
 */

package com.patron.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.HorizontalScrollView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Runnable;

import com.appboy.Appboy;

import com.patron.bind.NavigationBinder;
import com.patron.bind.ProductBinder;
import com.patron.db.ItemConnector;
import com.patron.listeners.ButtonCategoriesListener;
import com.patron.listeners.ButtonCheckoutListener;
import com.patron.listeners.ButtonFavoritesListener;
import com.patron.listeners.DrawerNavigationListener;
import com.patron.listeners.ListItemMenuAddListener;
import com.patron.listeners.OnMenuRefreshListener;
import com.patron.listeners.OnApiExecutedListener;
import com.patron.lists.ListFonts;
import com.patron.lists.ListLinks;
import com.patron.model.Category;
import com.patron.model.Item;
import com.patron.R;
import com.patron.system.ApiExecutor;
import com.patron.system.Globals;
import com.patron.system.Loadable;
import com.patron.view.ButtonCategory;
import com.patron.view.ButtonFavorites;
import com.patron.view.ButtonSearch;
import com.patron.view.HorizontalScrollViewFilters;
import com.patron.view.NavigationListView;
import static com.patron.view.NavigationListView.Hierarchy;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashMenu extends Activity
{
    private List<Button> buttonCategories;

	// Activity Methods
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_menu);

    // Set up the drawer layout.
    DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.menuDrawerNavigation);
    DrawerNavigationListener drawerNavigationListener = new DrawerNavigationListener(this);
    drawerLayoutNavigation.setDrawerListener(drawerNavigationListener);
    final NavigationListView listNavigation = (NavigationListView) findViewById(R.id.menuListNavigation);
    listNavigation.setHierarchy(drawerNavigationListener, drawerLayoutNavigation, Hierarchy.BUY);

    // Find the views.
    ListView listMenu = (ListView) findViewById(R.id.menuListItems);
    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.menuLayoutTypes);
    Button buttonSelectVendor = (Button) findViewById(R.id.menuButtonSelectVendor);
    Button buttonCheckout = (Button) findViewById(R.id.menuButtonCheckout);
    ButtonSearch buttonSearch = (ButtonSearch) findViewById(R.id.menuButtonSearch);
    ButtonFavorites buttonFavorites = (ButtonFavorites) findViewById(R.id.menuButtonFavorites);
    final SwipeRefreshLayout swipeRefreshLayoutItems = (SwipeRefreshLayout) findViewById(R.id.menuSwipeRefreshLayoutItems);
    swipeRefreshLayoutItems.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
        android.R.color.holo_orange_light, android.R.color.holo_red_light);

    // Set up loading indicator
    final RelativeLayout layout = (RelativeLayout)findViewById(R.id.menuLayoutContent);
    final ProgressBar progressIndicator = new ProgressBar(this);
    progressIndicator.setBackgroundColor(Color.TRANSPARENT);
    final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
    params.addRule(RelativeLayout.CENTER_IN_PARENT);

    // Listener to refresh the page on fetching the items for the vendor.
    final OnApiExecutedListener refreshListener = new OnMenuRefreshListener(swipeRefreshLayoutItems,
            listMenu, linearLayout, buttonSelectVendor);
    final OnApiExecutedListener removeIndicatorListener = new OnApiExecutedListener() {
        @Override
        public void onExecuted()
        {
            layout.removeView(progressIndicator);
        }
    };
    final OnApiExecutedListener stopRefreshListener = new OnApiExecutedListener() {
        @Override
        public void onExecuted()
        {
            swipeRefreshLayoutItems.setRefreshing(false);
        }
    };

        // Update the items on manual refresh.
        final ApiExecutor apiExecutor = new ApiExecutor();
        int startOffset = (int)Globals.convertDpToPixel(10, this);
        int endOffset = (int)Globals.convertDpToPixel(50, this);
        swipeRefreshLayoutItems.setProgressViewOffset(false, startOffset, endOffset);
		    swipeRefreshLayoutItems.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                // Add a loading indicator.
                swipeRefreshLayoutItems.setRefreshing(true);
                if (Globals.getVendor() == null)
                {
                    apiExecutor.selectNearestVendor(swipeRefreshLayoutItems.getContext(), new OnApiExecutedListener() {
                        @Override
                        public void onExecuted()
                        {
                            if (Globals.getVendor() == null)
                            {
                              Activity activity = (Activity)swipeRefreshLayoutItems.getContext();
                              /*Activity activity = (Activity)view.getContext();
                              ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                  activity, view, "findFailedSelectVendor");
                              Bundle bundle = options.toBundle();*/
                              Intent intent = new Intent(activity, FlashVendors.class);
                              //ActivityCompat.startActivity(activity, intent, bundle);
                              activity.startActivity(intent);
                              activity.finish();
                              swipeRefreshLayoutItems.setRefreshing(false);
                              return;
                            }
                            apiExecutor.getItems(Globals.getVendor().getId(), refreshListener, stopRefreshListener);
                        }
                    });
                }
                else
                {
                    apiExecutor.getItems(Globals.getVendor().getId(), refreshListener, stopRefreshListener);
                }
            }
        });

        // Update the items upon activity creation.
        layout.addView(progressIndicator, params);
        if (Globals.getVendor() == null)
        {
            apiExecutor.selectNearestVendor(this, new OnApiExecutedListener() {
                @Override
                public void onExecuted()
                {
                  if (Globals.getVendor() != null)
                  {
                    listNavigation.getTextViewHeader().setText(Globals.getVendor().getName() + "\n" + Globals.getPoints(Globals.getVendor().getId()) + " Points");
                    apiExecutor.getItems(Globals.getVendor().getId(), refreshListener, removeIndicatorListener);
                  }
                  else
                  {
                      removeIndicatorListener.onExecuted();
                    Activity activity = (Activity)swipeRefreshLayoutItems.getContext();
                    /*Activity activity = (Activity)view.getContext();
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, view, "findFailedSelectVendor");
                    Bundle bundle = options.toBundle();*/
                    Intent intent = new Intent(activity, FlashVendors.class);
                    //ActivityCompat.startActivity(activity, intent, bundle);
                    activity.startActivity(intent);
                    activity.finish();
                  }
                }
            });
        }
        else
        {
            apiExecutor.getItems(Globals.getVendor().getId(), refreshListener, removeIndicatorListener);
        }

        // Set up listeners
        buttonSearch.setListener(refreshListener);
        buttonFavorites.setListener(refreshListener);
		    buttonCheckout.setOnClickListener(new ButtonCheckoutListener(this));
        buttonSelectVendor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Activity activity = (Activity)view.getContext();
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, view, "selectVendorButton");
                Bundle bundle = options.toBundle();
                Intent intent = new Intent(activity, FlashVendors.class);
                //ActivityCompat.startActivity(activity, intent, bundle);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        // Create animation to show horizontal movement
        final ImageView imageViewHelpCategories = (ImageView) findViewById(R.id.menuImageViewHelpCategories);
        int images[] = { R.drawable.help_categories};
        animate(imageViewHelpCategories, images, 0,true);

        // Get rid of helper image once scrolled enough.
        HorizontalScrollViewFilters scrollViewFilters = (HorizontalScrollViewFilters)findViewById(R.id.menuHorizontalScrollViewTypes);
        scrollViewFilters.setImage(imageViewHelpCategories);
	}

    public List<Button> getButtonCategories()
    {
        return buttonCategories;
    }




  private void animate(final ImageView imageView, final int images[], final int imageIndex, final boolean forever) {

  //imageView <-- The View which displays the images
  //images[] <-- Holds R references to the images to display
  //imageIndex <-- index of the first image to show in images[]
  //forever <-- If equals true then after the last image it starts all over again with the first image resulting in an infinite loop. You have been warned.

      if (imageView.getVisibility() == View.GONE)
      {
          return;
      }

    int fadeInDuration = 1000; // Configure time values here
    int timeBetween = 10;
    int fadeOutDuration = 1000;

    imageView.setVisibility(View.INVISIBLE);    //Visible or invisible by default - this will apply when the animation ends
    imageView.setImageResource(images[imageIndex]);

    Animation fadeIn = new AlphaAnimation(0, 1);
    fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
    fadeIn.setDuration(fadeInDuration);

    Animation fadeOut = new AlphaAnimation(1, 0);
    fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
    fadeOut.setStartOffset(fadeInDuration + timeBetween);
    fadeOut.setDuration(fadeOutDuration);

    AnimationSet animation = new AnimationSet(false); // change to false
    animation.addAnimation(fadeIn);
    animation.addAnimation(fadeOut);
    animation.setRepeatCount(1);
    imageView.setAnimation(animation);

    animation.setAnimationListener(new AnimationListener() {
        public void onAnimationEnd(Animation animation) {
            if (images.length - 1 > imageIndex) {
                animate(imageView, images, imageIndex + 1,forever); //Calls itself until it gets to the end of the array
            }
            else {
                if (forever == true){
                animate(imageView, images, 0,forever);  //Calls itself to start the animation all over again in a loop if forever = true
                }
            }
        }
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub
        }
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub
        }
    });
}

    public void onStart()
    {
        super.onStart();
        Appboy.getInstance(FlashMenu.this).openSession(FlashMenu.this);
    }

    public void onStop()
    {
        super.onStop();
        Appboy.getInstance(FlashMenu.this).closeSession(FlashMenu.this);
    }

	// Calligraphy
	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
