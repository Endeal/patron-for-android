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

import com.facebook.widget.LoginButton;

import com.patron.db.LoginConnector;
import com.patron.listeners.OnApiExecutedListener;
import com.patron.listeners.OnTaskCompletedListener;
import com.patron.lists.ListLinks;
import com.patron.R;
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
	private boolean submitting;
    private Button buttonSubmit;
    private EditText editTextEmail;
    private EditText editTextPassword;
	private ProgressBar progressBar;
    private SocialExecutor socialExecutor;
    private GoogleApiClient googleClient;
    private ConnectionResult connectionResult;
    private static final int RC_SIGN_IN = 0;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	// Activity methods
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);
        buttonSubmit = (Button)findViewById(R.id.loginButtonSubmit);

		// Get the layout elements.
        editTextEmail = (EditText)findViewById(R.id.loginEditTextEmail);
        editTextPassword = (EditText)findViewById(R.id.loginEditTextPassword);
		buttonSubmit = (Button)findViewById(R.id.loginButtonSubmit);
        Button buttonResetPassword = (Button)findViewById(R.id.loginButtonResetPassword);
        Button buttonCreateAccount = (Button)findViewById(R.id.loginButtonCreateAccount);
		Button buttonFacebook = (Button)findViewById(R.id.loginButtonFacebook);
		Button buttonTwitter = (Button)findViewById(R.id.loginButtonTwitter);
		Button buttonGooglePlus = (Button)findViewById(R.id.loginButtonGoogle);

        // Prepopulate Login info
        editTextEmail.setText("mpacquiao@gmail.com");
        editTextPassword.setText("batman");
        submitting = false;

        // Login button actions
        buttonSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (!submitting)
                {
                    submitting = true;
                    final Activity activity = (Activity)view.getContext();
                    String email = editTextEmail.getText().toString();
                    String password = editTextPassword.getText().toString();
                    ApiExecutor executor = new ApiExecutor();
                    executor.loginPatron(email, password, new OnApiExecutedListener() {
                        @Override
                        public void onExecuted()
                        {
                            if (!Globals.hasUser())
                            {
                                Toast.makeText(activity, "Failed to log in.", Toast.LENGTH_SHORT).show();
                                submitting = false;
                                return;
                            }
                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                activity, buttonSubmit, "loginButton");
                            Bundle bundle = options.toBundle();
                            Intent intent = new Intent(activity, FlashMenu.class);
                            ActivityCompat.startActivity(activity, intent, bundle);
                            activity.finish();
                        }
                    });
                }
            }
        });

        // Reset password button actions
        buttonResetPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                Builder builder = new Builder(view.getContext());
                View dialogRequestPasswordView = inflater.inflate(R.layout.dialog_request_password, null);
                builder.setView(dialogRequestPasswordView);
                final AlertDialog dialogRequestPassword = builder.create();
                dialogRequestPassword.show();
            }
        });

        // Create account button actions
        buttonCreateAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(view.getContext(), FlashCreateAccount.class);
                startActivity(intent);
            }
        });

        // Login with Google+
        socialExecutor = new SocialExecutor(this, savedInstanceState, Network.GOOGLE_PLUS, Network.FACEBOOK, Network.TWITTER);
        buttonGooglePlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                socialExecutor.signIn(Network.GOOGLE_PLUS, null);
            }
        });

        // Login with Facebook
        buttonFacebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                socialExecutor.signIn(Network.FACEBOOK, null);
            }
        });

        // Login with Twitter
        buttonTwitter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                socialExecutor.signIn(Network.TWITTER, new OnSocialTaskCompletedListener() {
                    @Override
                    public void onComplete()
                    {
                        socialExecutor.getEmail(Network.TWITTER);
                    }
                });
            }
        });
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        super.onActivityResult(requestCode, resultCode, data);
        socialExecutor.onActivityResult(requestCode, resultCode, data);
	}

	// Calligraphy
	@Override
	protected void attachBaseContext(Context newBase)
	{
		super.attachBaseContext(new CalligraphyContextWrapper(newBase));
	}
}
