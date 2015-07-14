package me.endeal.patron.main;

import java.lang.Exception;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.view.LayoutInflater;
import android.app.AlertDialog;
import android.widget.EditText;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.appboy.Appboy;

import com.appsee.Appsee;

import me.endeal.patron.listeners.DrawerNavigationListener;
import me.endeal.patron.R;
import me.endeal.patron.system.Globals;
import me.endeal.patron.view.NavigationListView;
import static me.endeal.patron.view.NavigationListView.Hierarchy;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashVouchers extends AppCompatActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vouchers);
        Toolbar toolbar = (Toolbar)findViewById(R.id.vouchersToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Set up the navigation drawer.
		DrawerLayout drawerLayoutNavigation = (DrawerLayout) findViewById(R.id.vouchersDrawerNavigation);
		NavigationListView listNavigation = (NavigationListView) findViewById(R.id.vouchersListNavigation);
        DrawerNavigationListener drawerToggle = new DrawerNavigationListener(this, drawerLayoutNavigation, toolbar, R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawerLayoutNavigation.setDrawerListener(drawerToggle);
		listNavigation.setHierarchy(drawerToggle, drawerLayoutNavigation, Hierarchy.VOUCHERS);
        drawerLayoutNavigation.setScrimColor(getResources().getColor(R.color.scrim));
        drawerToggle.syncState();
	}

    public void onStart()
    {
        super.onStart();
        Appboy.getInstance(FlashVouchers.this).openSession(FlashVouchers.this);
    }

    public void onStop()
    {
        super.onStop();
        Appboy.getInstance(FlashVouchers.this).closeSession(FlashVouchers.this);
    }

	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
