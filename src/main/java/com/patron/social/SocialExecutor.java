package com.patron.social;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.facebook.internal.SessionTracker;
import com.facebook.model.GraphUser;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.SharedPreferencesTokenCachingStrategy;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
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
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.Twitter;

import io.fabric.sdk.android.Fabric;

import java.io.IOException;
import java.lang.NullPointerException;
import java.lang.Runnable;
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
    private UiLifecycleHelper facebookHelper;
    private FacebookFragment facebookFragment;

    // Twitter
    private TwitterLoginButton twitterButton;

    public static enum Network
    {
        FACEBOOK,
        TWITTER,
        GOOGLE_PLUS
    }

    public SocialExecutor(FragmentActivity activity, Bundle bundle, Network... networks)
    {
        googleSigningIn = false;
        googleIntentInProgress = false;
        this.activity = activity;
        this.bundle = bundle;

        // Google+

        // Facebook
        if (Arrays.asList(networks).contains(Network.FACEBOOK))
        {
            facebookFragment = null;
            if (bundle == null)
            {
                // Add the fragment on initial activity setup
                facebookFragment = new FacebookFragment();
                activity.getSupportFragmentManager().beginTransaction().add(android.R.id.content, facebookFragment).hide(facebookFragment).commit();
            }
            else
            {
                // Or set the fragment from restored state info
                facebookFragment = (FacebookFragment)activity.getSupportFragmentManager().findFragmentById(android.R.id.content);
            }
            facebookHelper = new UiLifecycleHelper(activity, null);
            facebookHelper.onCreate(bundle);
        }

        // Twitter
        if (Arrays.asList(networks).contains(Network.TWITTER))
        {
            twitterButton = new TwitterLoginButton(activity);
        }
    }

    public static void onCreate(Application application, String twitterKey, String twitterSecret)
    {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(twitterKey, twitterSecret);
        Fabric.with(application, new Twitter(authConfig));
    }

    public void onPause()
    {
        facebookHelper.onPause();
    }

    public void onResume()
    {
        facebookHelper.onResume();
    }

    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        facebookHelper.onSaveInstanceState(savedInstanceState);
    }

    public void onDestroy()
    {
        facebookHelper.onDestroy();
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
        facebookHelper.onActivityResult(requestCode, responseCode, intent, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data)
            {
                Toast.makeText(activity, "Facebook result unsuccessful.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data)
            {
                Toast.makeText(activity, "Facebook result successful.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean signedIn(Network network)
    {
        switch (network)
        {
            // Google+
            case GOOGLE_PLUS:
                return (googleClient != null && googleClient.isConnected());
            // Facebook
            case FACEBOOK:
                Session facebookSession = Session.getActiveSession();
                return (facebookSession != null && facebookSession.isOpened());
            // Twitter
            case TWITTER:
                TwitterSession twitterSession = Twitter.getSessionManager().getActiveSession();
                return (twitterSession != null && twitterSession.getAuthToken() != null);
        }
        return false;
    }

    public void signIn(Network network, OnSocialTaskCompletedListener listener)
    {
        if (network == Network.GOOGLE_PLUS)
        {
            if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity) != ConnectionResult.SUCCESS)
            {
                Toast.makeText(activity, "Google Play Services not available.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                if (googleClient == null)
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
                }
                else if (!googleClient.isConnecting())
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
        }
        if (network == Network.FACEBOOK)
        {
            activity.getSupportFragmentManager().beginTransaction().show(facebookFragment).commit();
            facebookButton = new LoginButton(facebookFragment.getActivity());
            facebookButton.setFragment(facebookFragment);
            facebookButton.setReadPermissions(Arrays.asList("public_profile", "email", "birthday"));
            Session session = Session.getActiveSession();
            StatusCallback statusCallback = new StatusCallback() {
                @Override
                public void call(Session session, SessionState state, Exception exception)
                {
                    System.out.println("FACEBOOK CALLED BACK");
                    if (state.isOpened())
                    {
                        System.out.println("LOGGED IN WITH FACEBOOK");
                    }
                    if (state.isClosed())
                    {
                        System.out.println("LOGGED OUT OF FACEBOOK");
                    }
                }
            };
            if (session != null)
            {
                session.openForRead(new Session.OpenRequest(facebookFragment).setPermissions(Arrays.asList("public_profile", "email", "birthday")).setCallback(statusCallback));

            }
            else
            {
                Session.openActiveSession(facebookFragment.getActivity(), facebookFragment, true, statusCallback);
            }
        }
        if (network == Network.TWITTER)
        {
            final OnSocialTaskCompletedListener socialTaskCompletedListener = listener;
            twitterButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                   Toast.makeText(activity, "Signed in with Twitter.", Toast.LENGTH_SHORT).show();
                   if (socialTaskCompletedListener != null)
                    {
                       socialTaskCompletedListener.onComplete();
                    }
               }

               @Override
               public void failure(TwitterException exception) {
                   Toast.makeText(activity, "Failed to sign in with Twitter.", Toast.LENGTH_SHORT).show();
               }
            });
            twitterButton.performClick();
        }
    }

    public void signOut(Network network, OnSocialTaskCompletedListener listener)
    {

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
        if (network == Network.TWITTER)
        {
            TwitterSession session = Twitter.getSessionManager().getActiveSession();
            TwitterAuthClient authClient = new TwitterAuthClient();
            authClient.requestEmail(session, new Callback() {
                @Override
                public void success(Result result)
                {
                    Toast.makeText(activity, "E-mail received!" + result.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(TwitterException exception)
                {
                    Toast.makeText(activity, "No e-mail address received.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return "";
    }

    public void shareLink(Network network, String message, String action, String desktopLink, String deepLink)
    {
        // Google+
        PlusShare.Builder builder = new PlusShare.Builder(activity);
        builder.addCallToAction(action, Uri.parse(desktopLink), deepLink);
        builder.setContentUrl(Uri.parse(desktopLink));
        builder.setContentDeepLinkId(deepLink, null, null, null);
        builder.setText(message);
        activity.startActivityForResult(builder.getIntent(), 0);
    }

    public void shareText(Network network, String message)
    {
        // Google+
        if (network == Network.GOOGLE_PLUS)
        {
            Intent shareIntent = new PlusShare.Builder(activity)
                .setType("text/plain")
                .setText(message)
                .getIntent();
            activity.startActivityForResult(shareIntent, 0);
        }

        // Facebook
        if (network == Network.FACEBOOK)
        {
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(activity)
                .setLink("http//example.com")
                .setName("Test")
                .setDescription("This is a test post")
                .setCaption("Caption")
                .build();
            facebookHelper.trackPendingDialogCall(shareDialog.present());
        }

        // Twitter
        if (network == Network.TWITTER)
        {
            TweetComposer.Builder builder = new TweetComposer.Builder(activity).text(message);
            builder.show();
        }
    }

    public void postRichData(Network network)
    {

    }

    public String getAccessToken(Network network)
    {
        switch (network)
        {
            case GOOGLE_PLUS:
                String accessToken = null;
                String[] scopes = new String[3];
                scopes[0] = "oauth2:server:client_id:342319301171-mhho1d9668o2l12j413lq5a8ij3vrnfp.apps.googleusercontent.com:api_scope:";
                scopes[1] = "https://www.googleapis.com/auth/plus.login ";
                scopes[2] = "https://www.googleapis.com/auth/plus.me ";
                scopes[3] = "profile";
                try
                {
                    accessToken = GoogleAuthUtil.getToken(activity, Plus.AccountApi.getAccountName(googleClient),
                        "oauth2:" + TextUtils.join(" ", scopes));
                }
                catch (IOException transientEx)
                {
                    // network or server error, the call is expected to succeed if you try again later.
                    // Don't attempt to call again immediately - the request is likely to
                    // fail, you'll hit quotas or back-off.
                }
                catch (UserRecoverableAuthException e)
                {
                    // Recover
                    accessToken = null;
                }
                catch (GoogleAuthException authEx)
                {
                    // Failure. The call is not expected to ever succeed so it should not be
                    // retried.
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
                return accessToken;
            case FACEBOOK:
                Session facebookSession = Session.getActiveSession();
                if (facebookSession.isOpened())
                {
                    return facebookSession.getAccessToken();
                }
                break;
            case TWITTER:
                TwitterSession twitterSession = Twitter.getSessionManager().getActiveSession();
                TwitterAuthToken authToken = twitterSession.getAuthToken();
                return authToken.toString();
        }
        return "";
    }
}

