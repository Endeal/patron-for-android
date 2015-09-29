package com.endeal.patron.activity;

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
import android.graphics.Color;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appboy.Appboy;

import com.appsee.Appsee;

import com.endeal.patron.dialogs.ResetPasswordDialog;
import com.endeal.patron.listeners.OnApiExecutedListener;
import com.endeal.patron.listeners.OnTaskCompletedListener;
import com.endeal.patron.lists.ListLinks;
import com.endeal.patron.model.ApiResult;
import com.endeal.patron.model.Patron;
import com.endeal.patron.model.Credential;
import com.endeal.patron.R;
import com.endeal.patron.social.OnSocialTaskCompletedListener;
import com.endeal.patron.social.SocialExecutor;
import static com.endeal.patron.social.SocialExecutor.Network;
import com.endeal.patron.system.ApiExecutor;
import com.endeal.patron.system.Globals;

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

import com.google.gson.Gson;

import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity
{
	private boolean submitting;
    private Button buttonSubmit;
    private EditText editTextEmail;
    private EditText editTextPassword;
	private ProgressBar progressBar;
    private CoordinatorLayout coordinatorLayout;
    private SocialExecutor socialExecutor;
    private GoogleApiClient googleClient;
    private ConnectionResult connectionResult;
    private String email;
    private String password;
    private String provider;
    private static final int RC_SIGN_IN = 0;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	// Activity methods
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        final Activity activity = this;
		//Appsee.start("41ea93b81fd7435b8e8d20ea2ba36b66");
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
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.loginCoordinatorLayoutMain);

        // Loading indicator
        final RelativeLayout layout = (RelativeLayout)findViewById(R.id.loginRelativeLayoutLoading);
        final ProgressBar progressIndicator = new ProgressBar(this);
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(200,200);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        progressIndicator.setLayoutParams(params);
        progressIndicator.setBackgroundColor(Color.TRANSPARENT);

        // Prepopulate Login info
        submitting = false;

        // Login listener.
        final OnApiExecutedListener listener = new OnApiExecutedListener() {
            @Override
            public void onExecuted(ApiResult result)
            {
                // Add a loading indicator.
                ApiExecutor executor = new ApiExecutor();
                Credential credential = new Credential(email, password, provider);
                executor.login(credential, new OnApiExecutedListener() {
                    @Override
                    public void onExecuted(ApiResult result)
                    {
                        layout.removeView(progressIndicator);
                        submitting = false;
                        if (Globals.getPatron() == null)
                        {
                            View contentView = activity.findViewById(android.R.id.content);
                            Snackbar.make(contentView, "Failed to log in", Snackbar.LENGTH_SHORT).show();
                            Globals.setCredential(null);
                            return;
                        }
                        Gson gson = new Gson();
                        System.out.println(gson.toJson(Globals.getPatron()));
                        Appboy.getInstance(LoginActivity.this).changeUser(Globals.getPatron().getId());
                        Appsee.setUserId(Globals.getPatron().getId());
                        Intent intent = new Intent(activity, MenuActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }
                });
            }
        };

        // Login button actions
        buttonSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                InputMethodManager inputMethodManager = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (submitting)
                    return;
                submitting = true;
                layout.addView(progressIndicator, params);
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                provider = "endeal";
                listener.onExecuted(null);
            }
        });

        // Automatically log in the user if credentials are saved.
        if (Globals.getCredential() != null && Globals.getCredential().getIdentifier() != null && Globals.getCredential().getVerifier() != null &&
                !Globals.getCredential().getIdentifier().equals("") && !Globals.getCredential().getVerifier().equals(""))
        {
            email = Globals.getCredential().getIdentifier();
            password = Globals.getCredential().getVerifier();
            provider = Globals.getCredential().getProvider();
            submitting = true;
            layout.addView(progressIndicator, params);
            listener.onExecuted(null);
        }

        // Reset password button actions
        buttonResetPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ResetPasswordDialog dialog = new ResetPasswordDialog(view.getContext());
                dialog.show();
            }
        });

        // Create account button actions
        buttonCreateAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Activity activity = (Activity)view.getContext();
                Intent intent = new Intent(activity, CreateAccountActivity.class);
                intent.putExtra("email", editTextEmail.getText().toString());
                intent.putExtra("password", editTextPassword.getText().toString());
                activity.startActivity(intent);
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
                if (submitting)
                    return;
                submitting = true;
                layout.addView(progressIndicator, params);
                final Context context = view.getContext();
                final Activity activity = (Activity)context;
                socialExecutor.signIn(Network.FACEBOOK, new OnSocialTaskCompletedListener() {
                    @Override
                    public void onComplete()
                    {
                        email = socialExecutor.getId(Network.FACEBOOK);
                        password = socialExecutor.getAccessToken(Network.FACEBOOK);
                        provider = "fb";
                        listener.onExecuted(null);
                    }
                });
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

    public void onStart()
    {
        super.onStart();
        Appboy.getInstance(LoginActivity.this).openSession(LoginActivity.this);
    }

    public void onStop()
    {
        super.onStop();
        Appboy.getInstance(LoginActivity.this).closeSession(LoginActivity.this);
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
