package org.adaptlab.chpir.android.survey;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class AppUtil {
    private final static String TAG = "AppUtil";
    
    /*
     * Get the version code from the AndroidManifest
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (NameNotFoundException nnfe) {
            Log.e(TAG, "Error finding version code: " + nnfe);
        }
        return -1;
    }
}
