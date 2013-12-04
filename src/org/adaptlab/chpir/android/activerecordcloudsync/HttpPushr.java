package org.adaptlab.chpir.android.activerecordcloudsync;

import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import com.activeandroid.query.Select;
import android.util.Log;

public class HttpPushr {
    private static final String TAG = "HttpPushr";
    private Class<? extends SendModel> mSendTableClass;
    private String mRemoteTableName;

    public HttpPushr(String remoteTableName,
            Class<? extends SendModel> sendTableClass) {
        mSendTableClass = sendTableClass;
        mRemoteTableName = remoteTableName;
    }

    public void push() {
        if (ActiveRecordCloudSync.getEndPoint() == null) {
            Log.i(TAG, "ActiveRecordCloudSync end point is not set!");
            return;
        }

        List<? extends SendModel> allElements = new Select()
                .from(mSendTableClass).orderBy("Id ASC").execute();

        for (SendModel element : allElements) {
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams
                    .setConnectionTimeout(client.getParams(), 10000); // Timeout limit

            HttpResponse response;

            if (!element.isSent() && element.readyToSend()) {
                try {
                    HttpPost post = new HttpPost(
                            ActiveRecordCloudSync.getEndPoint()
                                    + mRemoteTableName);
                    StringEntity se = new StringEntity(element.toJSON()
                            .toString());
                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
                            "application/json"));
                    post.setEntity(se);
                    Log.i(TAG, "Sending post request: "
                            + element.toJSON().toString());
                    response = client.execute(post);

                    /* Checking for successful response */
                    if (response.getStatusLine().getStatusCode() >= 200
                            && response.getStatusLine().getStatusCode() < 300) {
                        Log.i(TAG,
                                "Received OK HTTP status for "
                                        + element.toJSON());
                        InputStream in = response.getEntity().getContent();
                        element.setAsSent();
                        element.save();
                    } else {
                        Log.e(TAG, "Received BAD HTTP status code " + response.getStatusLine().getStatusCode()
                                + " for " + element.toJSON());
                    }

                } catch (Exception e) {
                    Log.e(TAG, "Cannot establish connection", e);
                }

            }
        }

    }
}
