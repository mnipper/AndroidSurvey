package org.adaptlab.chpir.android.activerecordcloudsync;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import android.util.Log;

public class ActiveRecordCloudSync {
    private static final String TAG="ActiveRecordCloudSync";
    private static Map<String, Class<? extends ReceiveModel>> mReceiveTables =
            new LinkedHashMap<String, Class<? extends ReceiveModel>>();
    private static Map<String, Class<? extends SendModel>> mSendTables =
            new LinkedHashMap<String, Class<? extends SendModel>>();
    
    private static String mEndPoint;        // The remote API endpoint url
    private static String mAccessToken;     // API Access Key
    
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
        mEndPoint = endPoint;
    }
    
    public static String getEndPoint() {
        return mEndPoint;
    }
    
    public static void syncReceiveTables() {
        for (Map.Entry<String, Class<? extends ReceiveModel>> entry : mReceiveTables.entrySet()) {
            Log.i(TAG, "Syncing " + entry.getValue() + " from remote table " + entry.getKey());
            HttpFetchr httpFetchr = new HttpFetchr(entry.getKey(), entry.getValue());
            httpFetchr.fetch();
        }
    }
    
    public static void syncSendTables() {
        for (Map.Entry<String, Class<? extends SendModel>> entry : mSendTables.entrySet()) {
            Log.i(TAG, "Syncing " + entry.getValue() + " to remote table " + entry.getKey());
            HttpPushr httpPushr = new HttpPushr(entry.getKey(), entry.getValue());
            httpPushr.push();
        }
    }
    
    public static boolean isApiAvailable() {
        return ping(getPingAddress(), 10000);
    }
    
    public static void setAccessToken(String token) {
        mAccessToken = token;
    }
    
    public static String getAccessToken() {
        return mAccessToken;
    }
    
    public static String accessTokenUrlParam() {
        return "?access_token=" + getAccessToken();
    }
    
    private static String getPingAddress() {
        if (!getReceiveTables().keySet().isEmpty()) {
            return getEndPoint() + getReceiveTables().keySet().iterator().next();
        }
        return getEndPoint();
    }

    private static boolean ping(String url, int timeout) {
        if (url == null) return true;
        url = url.replaceFirst("https", "http");
        url = url + accessTokenUrlParam();
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            Log.i(TAG, "Received response code " + responseCode + " for api endpoint");
            return (200 <= responseCode && responseCode <= 399);
        } catch (IOException exception) {
            return false;
        }
    }
}
