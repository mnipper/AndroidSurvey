package org.adaptlab.chpir.android.activerecordcloudsync;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.adaptlab.chpir.android.survey.AppUtil;
import org.adaptlab.chpir.android.survey.R;

import android.content.Context;
import android.util.Log;

public class ActiveRecordCloudSync {
    private static final String TAG="ActiveRecordCloudSync";
    private static Map<String, Class<? extends ReceiveModel>> mReceiveTables =
            new LinkedHashMap<String, Class<? extends ReceiveModel>>();
    private static Map<String, Class<? extends SendModel>> mSendTables =
            new LinkedHashMap<String, Class<? extends SendModel>>();
    
    private static String mEndPoint;        // The remote API endpoint url
    private static String mAccessToken;     // API Access Key
    private static int mVersionCode;        // App version code from Manifest
    private static String mConsentEndPoint;
    
    /**
     * Add a ReceiveTable.  A ReceiveTable is an active record model class that extends the
     * ReceiveModel abstract class.
     * 
     * @param tableName
     * @param receiveTable
     */
    public static void addReceiveTable(String tableName, Class<? extends ReceiveModel> receiveTable) {
        mReceiveTables.put(tableName, receiveTable);
    }
    
    public static Map<String, Class<? extends ReceiveModel>> getReceiveTables() {
        return mReceiveTables;
    }
    
    public static void addSendTable(String tableName, Class<? extends SendModel> sendTable) {
        mSendTables.put(tableName, sendTable);
    }
    
    public static void setEndPoint(String endPoint) {
        if (AppUtil.DEBUG) Log.i(TAG, "Api End point is: " + endPoint);

        char lastChar = endPoint.charAt(endPoint.length() - 1);
        if (lastChar != '/') endPoint = endPoint + "/";
        
        mEndPoint = endPoint;
    }
    
    public static String getEndPoint() {
        return mEndPoint;
    }
    
    public static void setConsentEndPoint(String endPoint) {
        if (endPoint != null) {
	    	char lastChar = endPoint.charAt(endPoint.length() - 1);
	        if (lastChar != '/') endPoint = endPoint + "/";
	        
	        mConsentEndPoint = endPoint;   
        }
    }
    
    public static String getConsentEndPoint() {
        return mConsentEndPoint;
    }
    
    public static void syncReceiveTables(Context context) {
        NetworkNotificationUtils.showNotification(context, android.R.drawable.stat_sys_download, R.string.sync_notification_text);
        for (Map.Entry<String, Class<? extends ReceiveModel>> entry : mReceiveTables.entrySet()) {
            if (AppUtil.DEBUG) Log.i(TAG, "Syncing " + entry.getValue() + " from remote table " + entry.getKey());
            HttpFetchr httpFetchr = new HttpFetchr(entry.getKey(), entry.getValue());
            httpFetchr.fetch();
        }
        NetworkNotificationUtils.showNotification(context, android.R.drawable.stat_sys_download_done, R.string.sync_notification_complete_text);
    }
    
    public static void syncSendTables(Context context) {
        NetworkNotificationUtils.showNotification(context, android.R.drawable.stat_sys_download, R.string.sync_notification_text);
        for (Map.Entry<String, Class<? extends SendModel>> entry : mSendTables.entrySet()) {
            if (AppUtil.DEBUG) Log.i(TAG, "Syncing " + entry.getValue() + " to remote table " + entry.getKey());
            HttpPushr httpPushr = new HttpPushr(entry.getKey(), entry.getValue());
            httpPushr.push();
        }
        NetworkNotificationUtils.showNotification(context, android.R.drawable.stat_sys_download_done, R.string.sync_notification_complete_text);
    }
    
    public static boolean isApiAvailable() {
        if (getPingAddress() == null) return true;
        int responseCode = ping(getPingAddress(), 10000);
        if (responseCode == 426) return true; // Api is available but an app upgrade is required
        return (200 <= responseCode && responseCode < 300);
    }

    /*
     * Check to see if this version of the application meets the
     * minimum standard to interact with API.
     */
    public static boolean isVersionAcceptable() {
        int responseCode = ping(getPingAddress(), 10000);
        return responseCode != 426;  // Http Status Code 426 = upgrade required     
    }
    
    public static void setAccessToken(String token) {
        mAccessToken = token;
    }
    
    public static String getAccessToken() {
        return mAccessToken;
    }
    
    public static void setVersionCode(int code) {
        mVersionCode = code;
    }
    
    /*
     * Version code from AndroidManifest
     */
    public static int getVersionCode() {
        return mVersionCode;
    }
    
    /*
     * Append to all api calls.
     * Ensure that the access token is valid and the version code is up to date
     * before allowing an update.
     */
    public static String getParams() {
        return "?access_token=" + getAccessToken() + "&version_code=" + getVersionCode();
    }
    
    private static String getPingAddress() {
        if (!getReceiveTables().keySet().isEmpty()) {
            return getEndPoint() + getReceiveTables().keySet().iterator().next();
        }
        return getEndPoint();
    }

    private static int ping(String url, int timeout) {
        if (url == null) return -1;
        url = url + getParams();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            if (AppUtil.DEBUG) Log.i(TAG, "Received response code " + responseCode + " for api endpoint");
            return responseCode;
        } catch (IOException exception) {
            return -1;
        }
    }
}
