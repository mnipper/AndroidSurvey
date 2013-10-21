package org.adaptlab.chpir.android.activerecordcloudsync;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class PollService extends IntentService {
    private static final String TAG = "PollService";

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intnet: " + intent);
    }

}
