package org.adaptlab.chpir.android.survey.Location;

import org.adaptlab.chpir.android.survey.R;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

public class LocationServiceManager {
	private static final String TAG = "LocationServiceManager";
    public static final String ACTION_LOCATION = "org.adaptlab.chpir.android.survey.Location.LocationReceiver.ACTION_LOCATION";  
    private static LocationServiceManager sLocationServiceManager;
    private Context mAppContext;
    private LocationManager mLocationManager;
    private Location mLastLocation;
    private String mLatitude;
    private String mLongitude;
    
    private LocationServiceManager(Context appContext) {
        mAppContext = appContext;
        mLocationManager = (LocationManager)mAppContext.getSystemService(Context.LOCATION_SERVICE);
    }
    
    public static LocationServiceManager get(Context c) {
        if (sLocationServiceManager == null) {
        	sLocationServiceManager = new LocationServiceManager(c.getApplicationContext());
        }
        return sLocationServiceManager;
    }

    private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast(mAppContext, 0, broadcast, flags);
    }

    public void startLocationUpdates() {
        String provider = LocationManager.GPS_PROVIDER;
        Location lastKnown = mLocationManager.getLastKnownLocation(provider);
        if (lastKnown != null) {
            lastKnown.setTime(System.currentTimeMillis());
            broadcastLocation(lastKnown);
        }
        PendingIntent pi = getLocationPendingIntent(true);
        if (mLocationManager.isProviderEnabled(provider)) {
        	mLocationManager.requestLocationUpdates(provider, 0, 0, pi);
        }
    }
    
    public void stopLocationUpdates() {
        PendingIntent pi = getLocationPendingIntent(false);
        if (pi != null) {
            mLocationManager.removeUpdates(pi);
            pi.cancel();
        }
    }
    
    public boolean isTrackingSurvey() {
        return getLocationPendingIntent(false) != null;
    }
    
    private void broadcastLocation(Location location) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        broadcast.putExtra(LocationManager.KEY_LOCATION_CHANGED, location);
        mAppContext.sendBroadcast(broadcast);
    }
    
    public BroadcastReceiver mLocationReceiver = new LocationReceiver() {
        @Override
        protected void onLocationReceived(Context context, Location loc) {
            mLastLocation = loc;
            updateLocation();
        }
        
        @Override
        protected void onProviderEnabledChanged(boolean enabled) {
            int toastText = enabled ? R.string.gps_enabled : R.string.gps_disabled;
            Toast.makeText(mAppContext, toastText, Toast.LENGTH_LONG).show();
        }
    };
    
    private void updateLocation() {
    	if (mLastLocation != null) {
            mLatitude = Double.toString(mLastLocation.getLatitude());
            mLongitude = Double.toString(mLastLocation.getLongitude());
        }
    }
    
    public String getLatitude() {
    	return mLatitude;
    }
    
    public String getLongitude() {
    	return mLongitude;
    }
}
