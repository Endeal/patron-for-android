package me.endeal.patron.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.appboy.Appboy;

import com.appsee.Appsee;

import me.endeal.patron.listeners.ButtonUpdateAccountListener;
import me.endeal.patron.listeners.DrawerNavigationListener;
import me.endeal.patron.listeners.ListItemSettingsFunderListener;
import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.R;
import me.endeal.patron.social.OnSocialTaskCompletedListener;
import me.endeal.patron.social.SocialExecutor;
import static me.endeal.patron.social.SocialExecutor.Network;
import me.endeal.patron.system.ApiExecutor;
import me.endeal.patron.system.Globals;
import me.endeal.patron.view.ListViewFunders;
import me.endeal.patron.view.NavigationListView;
import static me.endeal.patron.view.NavigationListView.Hierarchy;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashSettings extends AppCompatActivity
{
    /*
    private TextView textViewFacebook;
    private TextView textViewTwitter;
    private TextView textViewGooglePlus;
    private Button buttonFacebook;
    private Button buttonTwitter;
    private Button buttonGooglePlus;
    private Button buttonAddCard;
    private Button buttonAddBankAccount;
    */
    private Toolbar toolbar;
    private Button buttonUpdateAccount;
    private Button buttonLogout;
    private SocialExecutor executor;

	// Activity methods.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_settings);
        toolbar = (Toolbar)findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Set up the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.settingsDrawerNavigation);
		NavigationListView listNavigation = (NavigationListView) findViewById(R.id.settingsListNavigation);
        DrawerNavigationListener drawerToggle = new DrawerNavigationListener(this, drawerLayoutNavigation, toolbar, R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawerLayoutNavigation.setDrawerListener(drawerToggle);
		listNavigation.setHierarchy(drawerToggle, drawerLayoutNavigation, Hierarchy.SETTINGS);
        drawerLayoutNavigation.setScrimColor(getResources().getColor(R.color.scrim));
        drawerToggle.syncState();

        // Check if networks are signed in.
        /*
        executor = new SocialExecutor(this, savedInstanceState, Network.FACEBOOK,
                Network.TWITTER, Network.GOOGLE_PLUS);
                */

        // Edit the social network text to correct status.
        /*
        textViewFacebook = (TextView)findViewById(R.id.settingsTextViewNetworkFacebook);
        textViewTwitter = (TextView)findViewById(R.id.settingsTextViewNetworkTwitter);
        textViewGooglePlus = (TextView)findViewById(R.id.settingsTextViewNetworkGooglePlus);
        buttonFacebook = (Button)findViewById(R.id.settingsButtonNetworkFacebook);
        buttonTwitter = (Button)findViewById(R.id.settingsButtonNetworkTwitter);
        buttonGooglePlus = (Button)findViewById(R.id.settingsButtonNetworkGooglePlus);
        buttonAddCard = (Button)findViewById(R.id.settingsButtonAddCard);
        */
        buttonUpdateAccount = (Button)findViewById(R.id.settingsButtonUpdateAccount);
        buttonLogout = (Button)findViewById(R.id.settingsButtonLogout);

        update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    public void onStart()
    {
        super.onStart();
        Appboy.getInstance(FlashSettings.this).openSession(FlashSettings.this);
    }

    public void onStop()
    {
        super.onStop();
        Appboy.getInstance(FlashSettings.this).closeSession(FlashSettings.this);
    }

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}

  // TextView methods
  public void viewPrivacyPolicy(View view)
  {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.endeal.me/patron/privacy-policy"));
    startActivity(intent);
  }

  public void viewTermsOfService(View view)
  {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.endeal.me/patron/terms-of-service"));
    startActivity(intent);
  }

  public void update()
  {
      /*
        boolean facebookSignedIn = executor.signedIn(Network.FACEBOOK);
        boolean twitterSignedIn = executor.signedIn(Network.TWITTER);
        boolean googlePlusSignedIn = executor.signedIn(Network.GOOGLE_PLUS);
        */

        // Identify the correct text for the social network statuses.
        /*
        String textFacebook = "";
        String textTwitter = "";
        String textGooglePlus = "";
        if (facebookSignedIn)
        {
            textFacebook = "Facebook: " + executor.getUsername(Network.FACEBOOK);
            buttonFacebook.setBackgroundResource(R.drawable.button_remove);
            buttonFacebook.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    final Context context = view.getContext();
                    executor.signOut(Network.FACEBOOK, new OnSocialTaskCompletedListener() {
                        @Override
                        public void onComplete()
                        {
                            Toast.makeText(context, "Signed out of Facebook", Toast.LENGTH_SHORT).show();
                            update();
                        }
                    });
                }
            });
        }
        else
        {
            textFacebook = "Facebook: Disconnected";
            buttonFacebook.setBackgroundResource(R.drawable.button_confirm);
            buttonFacebook.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    final Context context = view.getContext();
                    executor.signIn(Network.FACEBOOK, new OnSocialTaskCompletedListener() {
                        @Override
                        public void onComplete()
                        {
                            String id = executor.getId(Network.FACEBOOK);
                            String token = executor.getAccessToken(Network.FACEBOOK);
                            ApiExecutor api = new ApiExecutor();
                            api.login(id, token, "fb", new OnApiExecutedListener() {
                                @Override
                                public void onExecuted()
                                {
                                    Toast.makeText(context, "Signed out of Facebook", Toast.LENGTH_SHORT).show();
                                    update();
                                }
                            });
                        }
                    });
                }
            });
        }

        if (twitterSignedIn)
        {
            textTwitter = "Twitter: Connected";
            buttonTwitter.setBackgroundResource(R.drawable.button_remove);
            buttonTwitter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    final Context context = view.getContext();
                    executor.signOut(Network.TWITTER, new OnSocialTaskCompletedListener() {
                        @Override
                        public void onComplete()
                        {
                            Toast.makeText(context, "Signed out of Twitter", Toast.LENGTH_SHORT).show();
                            update();
                        }
                    });
                }
            });
        }
        else
        {
            textTwitter = "Twitter: Disconnected";
            buttonTwitter.setBackgroundResource(R.drawable.button_confirm);
            buttonTwitter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    final Context context = view.getContext();
                    executor.signIn(Network.TWITTER, new OnSocialTaskCompletedListener() {
                        @Override
                        public void onComplete()
                        {
                            Toast.makeText(context, "Signed in with Twitter", Toast.LENGTH_SHORT).show();
                            update();
                        }
                    });
                }
            });
        }

        if (googlePlusSignedIn)
        {
            textGooglePlus = "Google+: " + executor.getEmail(Network.GOOGLE_PLUS);
            buttonGooglePlus.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    final Context context = view.getContext();
                    executor.signOut(Network.GOOGLE_PLUS, new OnSocialTaskCompletedListener() {
                        @Override
                        public void onComplete()
                        {
                            Toast.makeText(context, "Signed out of Google+", Toast.LENGTH_SHORT).show();
                            update();
                        }
                    });
                }
            });
        }
        else
        {
            textGooglePlus = "Google+: Disconnected";
            buttonGooglePlus.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    final Context context = view.getContext();
                    executor.signIn(Network.GOOGLE_PLUS, new OnSocialTaskCompletedListener() {
                        @Override
                        public void onComplete()
                        {
                            Toast.makeText(context, "Signed in with Google+", Toast.LENGTH_SHORT).show();
                            update();
                        }
                    });
                }
            });
        }

        // Set the correct text for facebook
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
        */
        buttonUpdateAccount.setOnClickListener(new ButtonUpdateAccountListener(null));
        buttonLogout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Globals.setEmail(null);
                Globals.setPassword(null);
                Globals.setProvider(null);
                Globals.setOrder(null);
                Globals.setUser(null);
                Activity activity = (Activity)view.getContext();
                Intent intent = new Intent(activity, FlashLogin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });
  }

}
