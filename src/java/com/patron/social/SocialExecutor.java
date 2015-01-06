package com.patron.social;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.facebook.widget.LoginButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.people.Person;

import com.patron.system.Patron;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.Twitter;

import io.fabric.sdk.android.Fabric;

import java.lang.NullPointerException;
import java.util.Arrays;

public class SocialExecutor
{
    // Social Executor
    private FragmentActivity activity;
    private Bundle bundle;

    // Google+
    private GoogleApiClient googleClient;
    private Person googlePlusUser;
    private ConnectionResult googleResult;
    private static final int CODE_GOOGLE_SIGN_IN = 0;
    private boolean googleSigningIn;
    private boolean googleIntentInProgress;

    // Facebook
    private LoginButton facebookButton;

    // Twitter
    private TwitterLoginButton twitterButton;


    public static enum Network
    {
        FACEBOOK,
        TWITTER,
        GOOGLE_PLUS
    }

    public SocialExecutor(FragmentActivity fragmentActivity, Bundle bundle, Network... networks)
    {
        googleSigningIn = false;
        googleIntentInProgress = false;
        this.activity = fragmentActivity;
        this.bundle = bundle;

        // Google+
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity) != ConnectionResult.SUCCESS)
        {
            Toast.makeText(activity, "Google Play Services not available.", Toast.LENGTH_SHORT).show();
        }
        else if (Arrays.asList(networks).contains(Network.GOOGLE_PLUS))
        {
            Plus.PlusOptions.Builder builder = new Plus.PlusOptions.Builder();
            builder = builder.addActivityTypes("http://schemas.google.com/AddActivity");
            Plus.PlusOptions options = builder.build();
            googleClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(new ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle)
                    {
                        googleSigningIn = false;
                        Toast.makeText(activity, "Signed in with Google+", Toast.LENGTH_SHORT).show();
                        Plus.PeopleApi.loadVisible(googleClient, null).setResultCallback(new ResultCallback() {
                            @Override
                            public void onResult(com.google.android.gms.common.api.Result result)
                            {
                                if (Plus.PeopleApi.getCurrentPerson(googleClient) != null)
                                {
                                    Person currentPerson = Plus.PeopleApi.getCurrentPerson(googleClient);
                                    googlePlusUser = currentPerson;
                                    String personName = currentPerson.getDisplayName();
                                    String[] parts = personName.split(" ");
                                    String firstName = parts[0];
                                    String lastName = parts[parts.length - 1];
                                }
                            }
                        });
                    }

                    @Override
                    public void onConnectionSuspended(int cause)
                    {
                        googleClient.connect();
                    }
                }).addOnConnectionFailedListener(new OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result)
                    {
                        if (!result.hasResolution())
                        {
                            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),
                                activity, 0).show();
                            return;
                        }
                        if (!googleIntentInProgress)
                        {
                            googleResult = result;
                            if (googleSigningIn)
                            {

                            }
                        }
                    }
                }).addApi(Plus.API, options)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
            googleClient.connect();
        }

        // Facebook
        if (Arrays.asList(networks).contains(Network.FACEBOOK))
        {
            facebookButton = new LoginButton(activity);
            FacebookFragment mainFragment = null;
            if (bundle == null)
            {
                // Add the fragment on initial activity setup
                mainFragment = new FacebookFragment();
                //activity.getSupportFragmentManager().beginTransaction().add(android.R.id.content, mainFragment).commit();
            }
            else
            {
                // Or set the fragment from restored state info
                mainFragment = (FacebookFragment)activity.getSupportFragmentManager().findFragmentById(android.R.id.content);
            }
        }

        // Twitter
        if (Arrays.asList(networks).contains(Network.TWITTER))
        {
            twitterButton = new TwitterLoginButton(activity);
            twitterButton.setCallback(new Callback<TwitterSession>() {
               @Override
               public void success(Result<TwitterSession> result) {
                   Toast.makeText(activity, "Signed in with Twitter.", Toast.LENGTH_SHORT).show();
               }

               @Override
               public void failure(TwitterException exception) {
                   Toast.makeText(activity, "Failed to sign in with Twitter.", Toast.LENGTH_SHORT).show();
               }
            });
        }
    }

    public static void onCreate(Application application, String twitterKey, String twitterSecret)
    {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(twitterKey, twitterSecret);
        Fabric.with(application, new Twitter(authConfig));
    }

    public void onActivityResult(int requestCode, int responseCode, Intent intent)
    {
        if (requestCode == CODE_GOOGLE_SIGN_IN)
        {
            if (responseCode != Activity.RESULT_OK)
            {
                googleSigningIn = false;
            }
            googleSigningIn = false;
            if (!googleClient.isConnecting())
            {
                googleClient.connect();
            }
        }
        if (twitterButton != null)
        {
            twitterButton.onActivityResult(requestCode, responseCode, intent);
        }
    }

    public void signIn(Network network)
    {
        if (network == Network.GOOGLE_PLUS)
        {
            if (!googleClient.isConnecting())
            {
                googleSigningIn = true;
                try
                {
                    googleIntentInProgress = true;
                    googleResult.startResolutionForResult(activity, CODE_GOOGLE_SIGN_IN);
                }
                catch (NullPointerException e)
                {
                    googleIntentInProgress = false;
                    googleClient.connect();
                }
                catch (SendIntentException e)
                {
                    googleIntentInProgress = false;
                    googleClient.connect();
                }
            }
        }
        if (network == Network.FACEBOOK)
        {
            facebookButton.performClick();
        }
        if (network == Network.TWITTER)
        {
            twitterButton.performClick();
        }
    }

    public String getFirstName(Network network)
    {
        return "";
    }

    public String getLastName(Network network)
    {
        return "";
    }

    public String getEmail(Network network)
    {
        return "";
    }

    public void shareLink(String message, String action, String desktopLink, String deepLink)
    {
        PlusShare.Builder builder = new PlusShare.Builder(activity);
        builder.addCallToAction(action, Uri.parse(desktopLink), deepLink);
        builder.setContentUrl(Uri.parse(desktopLink));
        builder.setContentDeepLinkId(deepLink, null, null, null);
        builder.setText(message);
        activity.startActivityForResult(builder.getIntent(), 0);
    }

    public void shareText(String message)
    {
        // Google+
        Intent shareIntent = new PlusShare.Builder(activity)
            .setType("text/plain")
            .setText(message)
            .getIntent();
        activity.startActivityForResult(shareIntent, 0);

        // Twitter
        TweetComposer.Builder builder = new TweetComposer.Builder(activity).text(message);
        builder.show();
    }

    public void postText(String message, Network network)
    {

    }

    public void postRichData(Network network)
    {

    }
}

