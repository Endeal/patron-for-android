package me.endeal.patron.listeners;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import me.endeal.patron.listeners.OnApiExecutedListener;
import me.endeal.patron.system.ApiExecutor;

public class UserLocationListener implements LocationListener
{
    private Context context;
    private ApiExecutor apiExecutor;
    private OnApiExecutedListener[] listeners;

    public UserLocationListener(Context context, ApiExecutor apiExecutor, OnApiExecutedListener... listeners)
    {
        this.context = context;
        this.apiExecutor = apiExecutor;
        this.listeners = listeners;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        //Toast.makeText(context, "Location changed", Toast.LENGTH_SHORT).show();
        apiExecutor.callback(null, listeners);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle bundle)
    {
        //Toast.makeText(context, "Status changed", Toast.LENGTH_SHORT).show();
        apiExecutor.callback(null, listeners);
    }

    @Override
    public void onProviderEnabled(String provider)
    {
        //Toast.makeText(context, "Provider enabled", Toast.LENGTH_SHORT).show();
        apiExecutor.callback(null, listeners);
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        //Toast.makeText(context, "Provider disabled", Toast.LENGTH_SHORT).show();
        apiExecutor.callback(null, listeners);
    }
}
