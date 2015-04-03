package me.endeal.patron.main;

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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
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

import com.facebook.widget.LoginButton;

import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.listeners.OnTaskCompletedListener;
import me.endeal.patron.lists.ListLinks;
import me.endeal.patron.model.User;
import me.endeal.patron.R;
import me.endeal.patron.social.OnSocialTaskCompletedListener;
import me.endeal.patron.social.SocialExecutor;
import static me.endeal.patron.social.SocialExecutor.Network;
import me.endeal.patron.system.ApiExecutor;
import me.endeal.patron.system.Globals;
import me.endeal.patron.system.Loadable;

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

public class FlashLogin extends Activity
{
	private boolean submitting;
    private Button buttonSubmit;
    private EditText editTextEmail;
    private EditText editTextPassword;
	private ProgressBar progressBar;
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
            public void onExecuted()
            {
                // Add a loading indicator.
                ApiExecutor executor = new ApiExecutor();
                executor.login(email, password, provider, new OnApiExecutedListener() {
                    @Override
                    public void onExecuted()
                    {
                        layout.removeView(progressIndicator);
                        submitting = false;
                        if (Globals.getUser() == null)
                        {
                            return;
                        }
                        Appboy.getInstance(FlashLogin.this).changeUser(Globals.getUser().getId());
                        Appsee.setUserId(Globals.getUser().getId());
                        Globals.getUser().setProvider(provider);
                        Intent intent = new Intent(activity, FlashMenu.class);
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
                if (submitting)
                    return;
                submitting = true;
                layout.addView(progressIndicator, params);
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                provider = "";
                listener.onExecuted();
            }
        });

        // Automatically log in the user if credentials are saved.
        if (Globals.getEmail() != null && Globals.getPassword() != null &&
                !Globals.getEmail().equals("") && !Globals.getPassword().equals(""))
        {
            email = Globals.getEmail();
            password = Globals.getPassword();
            provider = Globals.getProvider();
            submitting = true;
            layout.addView(progressIndicator, params);
            listener.onExecuted();
        }

        // Reset password button actions
        buttonResetPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (submitting)
                    return;
                System.out.println("reset1");
                LayoutInflater inflater = LayoutInflater.from(activity);
                System.out.println("reset2");
                Builder builder = new Builder(activity, R.style.DialogMain);
                System.out.println("reset3");
                View dialogRequestPasswordView = inflater.inflate(R.layout.dialog_request_password, null);
                System.out.println("reset4");
                builder.setView(dialogRequestPasswordView);
                System.out.println("reset5");
                final AlertDialog dialogRequestPassword = builder.create();
                System.out.println("reset6");
                final EditText editTextDialogResetPasswordEmail = (EditText)dialogRequestPasswordView.findViewById(R.id.dialogResetPasswordEditTextEmail);
                System.out.println("reset7");
                Button buttonDialogResetPasswordSubmit = (Button)dialogRequestPasswordView.findViewById(R.id.dialogResetPasswordButtonSubmit);
                System.out.println("reset8");
                buttonDialogResetPasswordSubmit.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View buttonView)
                    {
                System.out.println("reset9");
                        ApiExecutor executor = new ApiExecutor();
                System.out.println("reset10");
                        String email = editTextDialogResetPasswordEmail.getText().toString();
                System.out.println("reset11");
                        executor.resetPassword(email, new OnApiExecutedListener() {
                            @Override
                            public void onExecuted()
                            {
                System.out.println("reset13");
                            }
                        });
                        dialogRequestPassword.dismiss();
                System.out.println("reset14");
                    }
                });
                System.out.println("reset15");
                dialogRequestPassword.show();
            }
        });

        // Create account button actions
        buttonCreateAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Activity activity = (Activity)view.getContext();
                Intent intent = new Intent(activity, FlashCreateAccount.class);
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
                        listener.onExecuted();
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
        Appboy.getInstance(FlashLogin.this).openSession(FlashLogin.this);
    }

    public void onStop()
    {
        super.onStop();
        Appboy.getInstance(FlashLogin.this).closeSession(FlashLogin.this);
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
