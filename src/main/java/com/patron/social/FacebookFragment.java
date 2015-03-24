package com.patron.social;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import com.facebook.widget.LoginButton;

import java.io.IOException;
import java.lang.Exception;
import java.lang.Runnable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
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
import com.patron.main.FlashCreateAccount;
import com.patron.main.FlashMenu;
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

public class FacebookFragment extends Fragment
{
    private LoginButton facebookButton;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.layout_login, container, false);
        facebookButton = new LoginButton(getActivity());
        facebookButton.setReadPermissions(Arrays.asList("email", "user_birthday", "public_profile"));
        buttonSubmit = (Button)view.findViewById(R.id.loginButtonSubmit);

		// Get the layout elements.
        editTextEmail = (EditText)view.findViewById(R.id.loginEditTextEmail);
        editTextPassword = (EditText)view.findViewById(R.id.loginEditTextPassword);
		buttonSubmit = (Button)view.findViewById(R.id.loginButtonSubmit);
        Button buttonResetPassword = (Button)view.findViewById(R.id.loginButtonResetPassword);
        Button buttonCreateAccount = (Button)view.findViewById(R.id.loginButtonCreateAccount);
		Button buttonFacebook = (Button)view.findViewById(R.id.loginButtonFacebook);
		Button buttonTwitter = (Button)view.findViewById(R.id.loginButtonTwitter);
		Button buttonGooglePlus = (Button)view.findViewById(R.id.loginButtonGoogle);

        // Prepopulate Login info
        editTextEmail.setText("mpacquiao@gmail.com");
        editTextPassword.setText("batman");
        submitting = false;

        // Login button actions
        buttonSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View clickView)
            {
                if (!submitting)
                {
                    submitting = true;
                    final Activity activity = (Activity)clickView.getContext();
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
                            //ActivityCompat.startActivity(activity, intent, bundle);
														activity.startActivity(intent);
                            activity.finish();
                        }
                    });
                }
            }
        });

        // Reset password button actions
        buttonResetPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View clickView)
            {
                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                Builder builder = new Builder(clickView.getContext(), R.style.DialogMain);
                View dialogRequestPasswordView = inflater.inflate(R.layout.dialog_request_password, null);
                builder.setView(dialogRequestPasswordView);
                final AlertDialog dialogRequestPassword = builder.create();
                dialogRequestPassword.show();
            }
        });

        // Create account button actions
        buttonCreateAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View clickView)
            {
                Intent intent = new Intent(clickView.getContext(), FlashCreateAccount.class);
                startActivity(intent);
            }
        });

        // Login with Google+
        /*
        socialExecutor = new SocialExecutor(this, savedInstanceState, Network.GOOGLE_PLUS, Network.FACEBOOK, Network.TWITTER);

        buttonGooglePlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                socialExecutor.signIn(Network.GOOGLE_PLUS, null);
            }
        });
        */

        // Login with Facebook
        /*
        buttonFacebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                socialExecutor.signIn(Network.FACEBOOK, null);
            }
        });
        */
        buttonFacebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View clickView)
            {
                System.out.println("FB LOGIN BUTTON PRESSED");
                facebookButton.performClick();
            }
        });

        // Login with Twitter
        /*
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
        */
        return view;
    }

    public LoginButton getLoginButton()
    {
        return facebookButton;
    }
}
