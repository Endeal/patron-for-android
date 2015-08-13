package com.endeal.patron.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ImageView;

import com.appboy.Appboy;

import com.appsee.Appsee;

import com.squareup.picasso.Picasso;

import com.endeal.patron.adapters.FunderAdapter;
import com.endeal.patron.adapters.NavigationAdapter;
import com.endeal.patron.dialogs.CardDialog;
import com.endeal.patron.dialogs.UpdateAccountDialog;
import com.endeal.patron.listeners.DrawerNavigationListener;
import com.endeal.patron.listeners.OnApiExecutedListener;
import com.endeal.patron.model.Credential;
import com.endeal.patron.R;
import com.endeal.patron.social.OnSocialTaskCompletedListener;
import com.endeal.patron.social.SocialExecutor;
import static com.endeal.patron.social.SocialExecutor.Network;
import com.endeal.patron.system.ApiExecutor;
import com.endeal.patron.system.Globals;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private Button buttonUpdateAccount;
    private Button buttonLogout;
    private SocialExecutor executor;
    private DrawerNavigationListener drawerToggle;
    private FunderAdapter adapter;

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
        drawerToggle = new DrawerNavigationListener(this, drawerLayoutNavigation, toolbar, R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawerLayoutNavigation.setDrawerListener(drawerToggle);
        drawerLayoutNavigation.setScrimColor(getResources().getColor(R.color.scrim));
        final RecyclerView recyclerViewNavigation = (RecyclerView)findViewById(R.id.navigationRecyclerViewNavigation);
        final TextView textViewDrawerTitle = (TextView)findViewById(R.id.navigationTextViewDrawerTitle);
        final TextView textViewDrawerSubtitle = (TextView)findViewById(R.id.navigationTextViewDrawerSubtitle);
        final ImageView imageViewDrawerVendor = (ImageView)findViewById(R.id.navigationImageViewDrawerVendor);
        textViewDrawerTitle.setText(Globals.getPatron().getIdentity().getFirstName() + " " + Globals.getPatron().getIdentity().getLastName());
        if (Globals.getVendor() != null)
        {
            textViewDrawerSubtitle.setText(Globals.getVendor().getName());
            Picasso.with(this).load(Globals.getVendor().getPicture()).into(imageViewDrawerVendor);
        }
        else
        {
            textViewDrawerSubtitle.setText("No vendor selected");
        }
        NavigationAdapter navigationAdapter = new NavigationAdapter(this);
        GridLayoutManager layoutManagerNavigation = new GridLayoutManager(getApplicationContext(), 1);
        recyclerViewNavigation.setLayoutManager(layoutManagerNavigation);
        recyclerViewNavigation.setAdapter(navigationAdapter);
        drawerToggle.syncState();

        // Set up funders recycler
        final GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        adapter = new FunderAdapter(this, Globals.getPatron().getFunders());
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.settingsRecyclerViewFunders);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Check if networks are signed in.
        buttonUpdateAccount = (Button)findViewById(R.id.settingsButtonUpdateAccount);
        buttonLogout = (Button)findViewById(R.id.settingsButtonLogout);

        buttonUpdateAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                UpdateAccountDialog dialog = new UpdateAccountDialog(view.getContext());
                dialog.show();
            }
        });

        update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        if (item.getItemId() == R.id.add)
        {
            CardDialog dialog = new CardDialog(this);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface)
                {
                    adapter.setFunders(Globals.getPatron().getFunders());
                    adapter.notifyDataSetChanged();
                }
            });
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onStart()
    {
        super.onStart();
        Appboy.getInstance(SettingsActivity.this).openSession(SettingsActivity.this);
    }

    public void onStop()
    {
        super.onStop();
        Appboy.getInstance(SettingsActivity.this).closeSession(SettingsActivity.this);
    }

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}

  // TextView methods
  public void viewPrivacyPolicy(View view)
  {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.endeal.me/products/patron/privacy-policy"));
    startActivity(intent);
  }

  public void viewTermsOfService(View view)
  {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.endeal.me/products/patron/terms-of-service"));
    startActivity(intent);
  }

    public void update()
    {
        buttonLogout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Credential credential = new Credential(null, null, null);
                Globals.setCredential(credential);
                Globals.setOrder(null);
                Globals.setOrders(null);
                Globals.setPatron(null);
                Globals.setVendor(null);
                Globals.setVendors(null);
                Activity activity = (Activity)view.getContext();
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });
    }

}
