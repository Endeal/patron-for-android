package com.patron.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;

import com.patron.listeners.DrawerNavigationListener;
import com.patron.listeners.ListItemSettingsFunderListener;
import com.patron.listeners.OnApiExecutedListener;
import com.patron.R;
import com.patron.social.SocialExecutor;
import static com.patron.social.SocialExecutor.Network;
import com.patron.view.ListViewFunders;
import com.patron.view.NavigationListView;
import static com.patron.view.NavigationListView.Hierarchy;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashSettings extends FragmentActivity
{
	// Activity methods.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);

		// Set up the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.settingsDrawerNavigation);
		NavigationListView listNavigation = (NavigationListView) findViewById(R.id.settingsListNavigation);
		DrawerNavigationListener drawerNavigationListener = new DrawerNavigationListener(this);
		drawerLayoutNavigation.setDrawerListener(drawerNavigationListener);
		listNavigation.setHierarchy(drawerNavigationListener, drawerLayoutNavigation, Hierarchy.SETTINGS);

        // Check if networks are signed in.
        SocialExecutor executor = new SocialExecutor(this, savedInstanceState, Network.FACEBOOK,
                Network.TWITTER, Network.GOOGLE_PLUS);
        boolean facebookSignedIn = executor.signedIn(Network.FACEBOOK);
        boolean twitterSignedIn = executor.signedIn(Network.TWITTER);
        boolean googlePlusSignedIn = executor.signedIn(Network.GOOGLE_PLUS);

        // Identify the correct text for the social network statuses.
        String textFacebook = "";
        String textTwitter = "";
        String textGooglePlus = "";
        if (facebookSignedIn)
        {
            textFacebook = "Facebook: " + executor.getEmail(Network.FACEBOOK);
        }
        else
        {
            textFacebook = "Facebook: Disconnected";
        }

        if (twitterSignedIn)
        {
            textTwitter = "Twitter: Connected";
        }
        else
        {
            textTwitter = "Twitter: Disconnected";
        }

        if (googlePlusSignedIn)
        {
            textGooglePlus = "Google+: " + executor.getEmail(Network.GOOGLE_PLUS);
        }
        else
        {
            textGooglePlus = "Google+: Disconnected";
        }

        // Edit the social network text to correct status.
        TextView textViewFacebook = (TextView)findViewById(R.id.settingsTextViewNetworkFacebook);
        TextView textViewTwitter = (TextView)findViewById(R.id.settingsTextViewNetworkTwitter);
        TextView textViewGooglePlus = (TextView)findViewById(R.id.settingsTextViewNetworkGooglePlus);
        Button buttonAddCard = (Button)findViewById(R.id.settingsButtonAddCard);
        Button buttonAddBankAccount = (Button)findViewById(R.id.settingsButtonAddBankAccount);
        textViewFacebook.setText(textFacebook);
        textViewTwitter.setText(textTwitter);
        textViewGooglePlus.setText(textGooglePlus);

        // Set up the funders list.
        final ListViewFunders funders = (ListViewFunders) findViewById(R.id.settingsListViewFunders);
        funders.update();
        OnApiExecutedListener updater = new OnApiExecutedListener() {
            @Override
            public void onExecuted()
            {
                funders.update();
            }
        };
        funders.setOnItemClickListener(new ListItemSettingsFunderListener(updater));
        buttonAddCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Activity activity = (Activity)view.getContext();
                Intent intent = new Intent(activity, FlashAddCard.class);
                activity.startActivity(intent);
            }
        });
        buttonAddBankAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Activity activity = (Activity)view.getContext();
                Intent intent = new Intent(activity, FlashAddBankAccount.class);
                activity.startActivity(intent);
            }
        });
    }

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}

}
