package org.adaptlab.chpir.android.survey.Tasks;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.adaptlab.chpir.android.activerecordcloudsync.ActiveRecordCloudSync;
import org.adaptlab.chpir.android.activerecordcloudsync.NetworkNotificationUtils;
import org.adaptlab.chpir.android.survey.AppUtil;
import org.adaptlab.chpir.android.survey.Models.Image;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadImagesTask extends AsyncTask<Void, Void, Void> {
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
    	ACCESS_TOKEN = AppUtil.getAdminSettingsInstance().getApiKey();
    	ActiveRecordCloudSync.setAccessToken(ACCESS_TOKEN);
        ActiveRecordCloudSync.setVersionCode(AppUtil.getVersionCode(mContext));
        
        for (Image img : Image.getAll()) {
        	String[] imageUrl = img.getPhotoUrl().split("/");
        	String url = ActiveRecordCloudSync.getEndPoint() + "images/" + imageUrl[2] + "/" + ActiveRecordCloudSync.getParams();
        	Log.i(TAG, "Image url: " + url);
        	String filename = UUID.randomUUID().toString() + ".jpg";
			FileOutputStream filewriter = null;
        	try {
        		byte[] imageBytes = getUrlBytes(url);
        		filewriter = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
        		filewriter.write(imageBytes);
        		img.setBitmapPath(filename);
        		img.save();
        		Log.i(TAG, "image saved in " + filename);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (filewriter != null)
						filewriter.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
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
