package org.adaptlab.chpir.android.activerecordcloudsync;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class HttpFetchr {
    private static final String TAG = "HttpFetchr";
    private Class<? extends ReceiveModel> mReceiveTableClass;
    private String mRemoteTableName;
    
    public HttpFetchr(String remoteTableName, Class<? extends ReceiveModel> receiveTableClass) {
        mReceiveTableClass = receiveTableClass;
        mRemoteTableName = remoteTableName;
    }
  
    public void fetch() {
        if (ActiveRecordCloudSync.getEndPoint() == null) {
            return;
        }

        JSONArray jsonArray = getData();        
        if (jsonArray != null) {
	        for (int i = 0; i < jsonArray.length(); i++) {
				try {
					ReceiveModel tableInstance = mReceiveTableClass.newInstance();
					tableInstance.createObjectFromJSON(jsonArray.getJSONObject(i));
				} catch (JSONException je) {
					Log.e(TAG, "Failed to parse items", je);
				} catch (InstantiationException ie) {
					Log.e(TAG, "Failed to instantiate receive table", ie);
				} catch (IllegalAccessException iae) {
					Log.e(TAG, "Failed to access receive table", iae);
				}
	        }
        }
        
    }
        
    private JSONArray getData() {
    	HttpClient client = new DefaultHttpClient();
    	HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
    	HttpResponse response;
    	JSONArray jsonArray = null;
    	try {
    		String url = ActiveRecordCloudSync.getEndPoint() + mRemoteTableName + ActiveRecordCloudSync.getParams();
    		HttpGetWithEntity get = new HttpGetWithEntity(url);
    		StringEntity entity = new StringEntity(ActiveRecordCloudSync.getSyncEntityParams(), CharEncoding.UTF_8);
    		entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
    		get.setEntity(entity);
    		response = client.execute(get);
    		
    		if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300) {
    			InputStream in = response.getEntity().getContent();
    			ByteArrayOutputStream out = new ByteArrayOutputStream();
    			int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                String jsonString = new String(out.toByteArray());
                jsonArray = new JSONArray(jsonString);
    		}
    	} catch (Exception e) {
            Log.e(TAG, "Cannot establish connection", e);
        }
    	return jsonArray;
    }

}
