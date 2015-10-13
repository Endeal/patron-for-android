package com.endeal.patron.activity;

import java.lang.Exception;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ListView;

import com.appboy.Appboy;

import com.appsee.Appsee;

import com.squareup.picasso.Picasso;

import com.endeal.patron.adapters.NavigationAdapter;
import com.endeal.patron.listeners.DrawerNavigationListener;
import com.endeal.patron.R;
import com.endeal.patron.system.Globals;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class VouchersActivity extends AppCompatActivity
{
    private DrawerNavigationListener drawerToggle;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vouchers);
        Toolbar toolbar = (Toolbar)findViewById(R.id.vouchersToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Globals.getPatron() == null)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

		// Set up the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.vouchersDrawerNavigation);
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
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerViewNavigation.setLayoutManager(layoutManager);
        recyclerViewNavigation.setAdapter(navigationAdapter);
        drawerToggle.syncState();
	}

    public void onStart()
    {
        super.onStart();
        Appboy.getInstance(VouchersActivity.this).openSession(VouchersActivity.this);
    }

    public void onStop()
    {
        super.onStop();
        Appboy.getInstance(VouchersActivity.this).closeSession(VouchersActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
