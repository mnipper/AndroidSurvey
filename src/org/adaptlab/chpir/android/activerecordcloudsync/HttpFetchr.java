package org.adaptlab.chpir.android.activerecordcloudsync;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

public class HttpFetchr {
    private static final String TAG = "HttpFetchr";
    private static final String ENDPOINT = "endpointapiurl";
    private static final String LAST_ID_API_PARAM = "last_id";
    private static final String RESULTS_API_PARAM = "results";
    private ReceiveTable mReceiveTable;
    
    public HttpFetchr(ReceiveTable receiveTable) {
        mReceiveTable = receiveTable;
    }
    
    public String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
    
    public ArrayList<ReceiveTable> fetch() {
        ArrayList<ReceiveTable> items = new ArrayList<ReceiveTable>();
        
        try {
            String url = Uri.parse(ENDPOINT).buildUpon()
                    .appendQueryParameter(LAST_ID_API_PARAM, mReceiveTable.lastId().toString())
                    .build().toString();
            String jsonString = getUrl(url);
            JSONObject jsonResult = new JSONObject(jsonString);
            JSONArray jsonArray = jsonResult.getJSONArray(RESULTS_API_PARAM);
            
            for (int i = 0; i < jsonArray.length(); i++) {
                mReceiveTable.createObjectFromJSON(jsonArray.getJSONObject(i));
            }
            
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse items", je);            
        }
        return items;
    }
    
    private byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
            
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
}
