package org.adaptlab.chpir.android.survey.Tasks;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.adaptlab.chpir.android.activerecordcloudsync.ActiveRecordCloudSync;
import org.adaptlab.chpir.android.activerecordcloudsync.NetworkNotificationUtils;
import org.adaptlab.chpir.android.survey.AppUtil;
import org.adaptlab.chpir.android.survey.R;
import org.adaptlab.chpir.android.survey.Models.Image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadImagesTask extends AsyncTask<Void, Void, Void> {
    public final static String TEMP_IMAGE_API_END_POINT = "http://10.0.3.2:3000";
    public static String ACCESS_TOKEN;
    private final static String TAG = "ImageDownloader";

	private Context mContext;
	
	public DownloadImagesTask(Context context) {
		mContext = context;
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		if (NetworkNotificationUtils.checkForNetworkErrors(mContext)) {
			downloadImages();
		}
		return null;
	}
	
	public void downloadImages() {
    	ACCESS_TOKEN = mContext.getResources().getString(R.string.backend_api_key);
    	ActiveRecordCloudSync.setAccessToken(ACCESS_TOKEN);
        ActiveRecordCloudSync.setVersionCode(AppUtil.getVersionCode(mContext));
        for (Image img : Image.getAll()) {
        	String url = TEMP_IMAGE_API_END_POINT + img.getPhotoUrl() + ActiveRecordCloudSync.getParams();
        	try {
        		byte[] bitmapBytes = getUrlBytes(url);
        		final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
        		Log.i(TAG, "Bitmap Created");
        		img.setBitmap(bitmap);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
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
