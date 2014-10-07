package org.adaptlab.chpir.android.survey.Tasks;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.adaptlab.chpir.android.activerecordcloudsync.ActiveRecordCloudSync;
import org.adaptlab.chpir.android.activerecordcloudsync.NetworkNotificationUtils;
import org.adaptlab.chpir.android.survey.AppUtil;
import org.adaptlab.chpir.android.survey.R;
import org.adaptlab.chpir.android.survey.Models.AdminSettings;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class ApkUpdateTask extends AsyncTask<Void, Void, Void> {

	private static final String TAG = "ApkUpdateTask";
	private Context mContext;
	private int mApkId;
	private Integer mLatestVersion;
	private String mFileName;
	private File mFile;
	
	public ApkUpdateTask(Context context) {
		mContext = context;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		if (NetworkNotificationUtils.checkForNetworkErrors(mContext)) {
			checkLatestApk();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Void param) {
		if (mLatestVersion != null) {
			if (mLatestVersion > AppUtil.getVersionCode(mContext)) {
		        new AlertDialog.Builder(mContext)
				.setMessage(R.string.new_apk)
				.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() { 
					public void onClick(DialogInterface dialog, int button) {
						new DownloadApkTask().execute();
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {}
	            }).show();
	        } else {
	        	AppUtil.startPollService();
	        }
		}
	}

	private void checkLatestApk() {
		ActiveRecordCloudSync.setAccessToken(AdminSettings.getInstance().getApiKey());
		ActiveRecordCloudSync.setVersionCode(AppUtil.getVersionCode(mContext));
		String url = AdminSettings.getInstance().getApiUrl() + "android_updates" + ActiveRecordCloudSync.getParams();
		try {
			String jsonString = getUrl(url);
	        Log.i(TAG, "Got JSON String: " + jsonString);
	        if (jsonString != null) {
		        JSONObject obj = new JSONObject(jsonString);
		        mLatestVersion = obj.getInt("version");
		        mApkId = obj.getInt("id");
		        mFileName = obj.getString("apk_update_file_name");
		        Log.i(TAG, "Latest version is: " + mLatestVersion);
	        }
		} catch (ConnectException cre) {
            Log.e(TAG, "Connection was refused", cre);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (NullPointerException npe) {
            Log.e(TAG, "Url is null", npe);
        } catch (JSONException je) {
        	Log.e(TAG, "Failed to parse items", je); 
		}
	}
	
	private String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
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
	
	private class DownloadApkTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			if (NetworkNotificationUtils.checkForNetworkErrors(mContext)) {
				downloadLatestApk();
			}
			return null;
		}
		
		@Override 
		protected void onPostExecute(Void param) {
			Intent intent =new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(mFile), "application/vnd.android.package-archive");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
		}

		private void downloadLatestApk() {
			String url = AdminSettings.getInstance().getApiUrl() + "android_updates/" + mApkId + "/" + ActiveRecordCloudSync.getParams();
			File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		    mFile = new File(path, mFileName);
		    FileOutputStream filewriter = null;
        	try {
        		byte[] imageBytes = getUrlBytes(url);
    			filewriter = new FileOutputStream(mFile);
        		filewriter.write(imageBytes);
        		Log.i(TAG, "APK saved in " + mFile.getAbsolutePath());
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

}
