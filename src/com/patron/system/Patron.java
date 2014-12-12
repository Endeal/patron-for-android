package com.patron.system;

import com.parse.Parse;
import com.parse.PushService;

import android.app.Application;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.ConnectionResult;

import com.noveogroup.android.log.Log;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import com.patron.main.FlashScan;

public class Patron extends Application
{
	private static Context context;
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	public static final String MIXPANEL_TOKEN = "a9c5fb2c27780b459248ef663090aa53";
	/**
	Initialize the library with your Mixpanel project token, MIXPANEL_TOKEN, and a reference
 	to your application context.
 	*/
 	MixpanelAPI mixpanel = MixpanelAPI.getInstance(context, MIXPANEL_TOKEN);
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		context = this;
		Parse.initialize(this, "l6vtkQv0R6uPJrXSCn2fiAMRjMFrH6iSA3FQtvkN", "TFzHYXvM4LbuyfqtQsigWaz1j9stCuvcho1zZyge");
		// Also in this method, specify a default Activity to handle push notifications
  		PushService.setDefaultPushCallback(this, FlashScan.class);
		Log.debug("Parse library initialized successfully");
		Log.info("Patron application successfully created");
	}

	@Override
	protected void onDestroy()
	{
    	mixpanel.flush();
    	super.onDestroy();
	}
	
	public static Context getContext()
	{
		return context;
	}

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment
    {
        private Dialog mDialog;
        public ErrorDialogFragment()
        {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            return mDialog;
        }
    }

    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */
                    break;
                }
        }
     }

    private boolean servicesConnected()
    {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode)
        {
            // In debug mode, log the status
            System.out.println("Google Play services is available.");
            // Continue
            return true;
        	// Google Play services was not available for some reason.
        	// resultCode holds the error code.
        }
        else
        {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null)
            {
                // Create a new DialogFragment for the error dialog
               ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getSupportFragmentManager(), "Location Updates");
            }
        }
    }
}
