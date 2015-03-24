package com.patron.main;

import dalvik.system.DexFile;

import java.io.IOException;
import java.lang.Exception;
import java.lang.Runnable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.*;
import com.facebook.widget.*;
import com.facebook.model.*;
import com.facebook.Session.StatusCallback;

import com.patron.db.LoginConnector;
import com.patron.listeners.OnApiExecutedListener;
import com.patron.listeners.OnTaskCompletedListener;
import com.patron.lists.ListLinks;
import com.patron.R;
import com.patron.social.FacebookFragment;
import com.patron.social.OnSocialTaskCompletedListener;
import com.patron.social.SocialExecutor;
import static com.patron.social.SocialExecutor.Network;
import com.patron.system.ApiExecutor;
import com.patron.system.Globals;
import com.patron.system.Loadable;

import org.apache.http.HttpEntity;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;

import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FlashLogin extends FragmentActivity
{

    private FacebookFragment fragment;
    private UiLifecycleHelper helper;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception)
        {
            onSessionStateChange(session, state, exception);
        }
    };

    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data)
        {
            System.out.println(error.toString());
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data)
        {
            System.out.println("Success");
        }
    };

	// Activity methods
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        helper = new UiLifecycleHelper(this, callback);
        helper.onCreate(savedInstanceState);
        if (savedInstanceState == null)
        {
            fragment = new FacebookFragment();
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
        }
        else
        {
            fragment = (FacebookFragment)getSupportFragmentManager().findFragmentById(android.R.id.content);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        helper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        helper.onSaveInstanceState(outState);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        super.onActivityResult(requestCode, resultCode, data);
        helper.onActivityResult(requestCode, resultCode, data, dialogCallback);

        /*
        super.onActivityResult(requestCode, resultCode, data);
        socialExecutor.onActivityResult(requestCode, resultCode, data);
        */
	}

    @Override
    public void onPause()
    {
        super.onPause();
        helper.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        helper.onDestroy();
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception)
    {
        if (session.isOpened())
        {
            System.out.println("ACCESS TOKEN:" + session.getAccessToken());
        }
        else if (session.isClosed())
        {
            System.out.println("LOGGED OUT OF FACEBOOK");
        }

    }

	// Calligraphy
	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
