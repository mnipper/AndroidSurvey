package org.adaptlab.chpir.android.survey.Location;

import org.adaptlab.chpir.android.survey.R;
import org.adaptlab.chpir.android.survey.SurveyActivity;

import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationService implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	private static final String APPTAG = "SurveyLocation";
	private static final String SHARED_PREFERENCES = "com.example.android.location.SHARED_PREFERENCES";
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final int MILLISECONDS_PER_SECOND = 1000;
	private static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	private static final int FAST_CEILING_IN_SECONDS = 1;
	private static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	private static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* FAST_CEILING_IN_SECONDS;
	private static final String EMPTY_STRING = new String();
	private static final String KEY_UPDATES_REQUESTED =
            "com.example.android.location.KEY_UPDATES_REQUESTED";


	private LocationClient mLocationClient;
	private LocationRequest mLocationRequest;
	private String mConnectionStatus;
	private String mConnectionState;
	private String mLatitudeLongitude;
	private Resources mResource;
	private SurveyActivity mActivity;
	boolean mUpdatesRequested = false;
	SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

	public LocationService(SurveyActivity activity) {
		mActivity = activity;
		onStartUp();
	}

	private void onStartUp() {
		mResource = mActivity.getResources();
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest
				.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
        mPrefs = mActivity.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
		mUpdatesRequested = false;
		mLocationClient = new LocationClient(mActivity, this, this);
	}
	
	public void start() {
        mLocationClient.connect();
    }

	public void stop() {
		if (mLocationClient.isConnected()) {
			stopPeriodicUpdates();
		}
		mLocationClient.disconnect();
	}
	
	public void resume() {
        if (mPrefs.contains(KEY_UPDATES_REQUESTED)) {
            mUpdatesRequested = mPrefs.getBoolean(KEY_UPDATES_REQUESTED, false);
        } else {
            mEditor.putBoolean(KEY_UPDATES_REQUESTED, false);
            mEditor.commit();
        }
    }
	
	public void pause() {
        mEditor.putBoolean(KEY_UPDATES_REQUESTED, mUpdatesRequested);
        mEditor.commit();
    }

	private void startPeriodicUpdates() {
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
		mConnectionState = mResource.getString(R.string.location_requested);
	}

	private void stopPeriodicUpdates() {
		mLocationClient.removeLocationUpdates(this);
		mConnectionState = mResource.getString(R.string.location_updates_stopped);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (result.hasResolution()) {
			try {
				result.startResolutionForResult(mActivity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (IntentSender.SendIntentException e) {
				e.printStackTrace();
			}
		} else {
			Log.i(APPTAG, result.getErrorCode() + "");
		}
	}

	@Override
	public void onConnected(Bundle bundle) {
		mConnectionStatus = mResource.getString(R.string.connected);
		if (mUpdatesRequested) {
			startPeriodicUpdates();
		}
	}

	@Override
	public void onDisconnected() {
		mConnectionStatus = mResource.getString(R.string.disconnected);
	}

	@Override
	public void onLocationChanged(Location location) {
		mConnectionStatus = mResource.getString(R.string.location_updated);
		mLatitudeLongitude = getLatitudeLongitude(mActivity, location);
	}

	public static String getLatitudeLongitude(Context context,
			Location currentLocation) {
		if (currentLocation != null) {
			return context.getString(R.string.latitude_longitude,
					currentLocation.getLatitude(),
					currentLocation.getLongitude());
		} else {
			return EMPTY_STRING;
		}
	}
	
	private boolean servicesConnected() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d(APPTAG, mResource.getString(R.string.play_services_available));
            return true;
        } else {
        	Log.d(APPTAG, resultCode + "");
            return false;
        }
    }
	
	public String getLocation() {
        if (servicesConnected()) {
            Location currentLocation = mLocationClient.getLastLocation();
            mLatitudeLongitude = getLatitudeLongitude(mActivity, currentLocation);
        }
        return mLatitudeLongitude;
    }

}
