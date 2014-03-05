package org.adaptlab.chpir.android.survey.Tasks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.ActiveRecordCloudSync;
import org.adaptlab.chpir.android.activerecordcloudsync.NetworkNotificationUtils;
import org.adaptlab.chpir.android.activerecordcloudsync.SendModel;
import org.adaptlab.chpir.android.survey.Models.ResponsePhoto;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import com.activeandroid.query.Select;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class UploadImagesTask extends AsyncTask<Void, Void, Void> {
	private static final String TAG = "UploadImagesTask";
	private static final String mRemoteTableName = "response_images";
	private Context mContext;
	
	public UploadImagesTask(Context context) {
		mContext = context;
	}
	
	@Override
	protected Void doInBackground(Void... arg0) {
		if (NetworkNotificationUtils.checkForNetworkErrors(mContext)) {
			Log.i(TAG, "Starting background task...upload images...");
			uploadFile();
		}
		return null;
	}
	
	private void uploadFile() {
		if (ActiveRecordCloudSync.getEndPoint() == null) {
            Log.i(TAG, "ActiveRecordCloudSync end point is not set!");
            return;
        }
		List<? extends SendModel> allElements = getImages();
		for (SendModel element : allElements) {
			
			HttpURLConnection connection = null;
			DataOutputStream outputStream = null;
			DataInputStream inputStream = null;

			String pathToOurFile = ((ResponsePhoto) element).getPicture().getAbsolutePath();
			String urlServer = ActiveRecordCloudSync.getEndPoint()+ mRemoteTableName + ActiveRecordCloudSync.getParams();
			String lineEnd = "\r\n";
			String twoHyphens = "--";
			String boundary =  "*****";
			
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1*1024*1024;
			
            if (!element.isSent() && element.readyToSend()) {
            	try {
            		
            		FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );

            		URL url = new URL(urlServer);
            		connection = (HttpURLConnection) url.openConnection();

            		// Allow Inputs & Outputs
            		connection.setDoInput(true);
            		connection.setDoOutput(true);
            		connection.setUseCaches(false);

            		// Enable POST method
            		connection.setRequestMethod("POST");

            		connection.setRequestProperty("Connection", "Keep-Alive");
            		connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

            		outputStream = new DataOutputStream( connection.getOutputStream() );
            		outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            		outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
            		outputStream.writeBytes(lineEnd);

            		bytesAvailable = fileInputStream.available();
            		bufferSize = Math.min(bytesAvailable, maxBufferSize);
            		buffer = new byte[bufferSize];

            		// Read file
            		bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            		while (bytesRead > 0)
            		{
            		outputStream.write(buffer, 0, bufferSize);
            		bytesAvailable = fileInputStream.available();
            		bufferSize = Math.min(bytesAvailable, maxBufferSize);
            		bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            		}

            		outputStream.writeBytes(lineEnd);
            		outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            		// Responses from the server (code and message)
            		int serverResponseCode = connection.getResponseCode();
            		String serverResponseMessage = connection.getResponseMessage();
            		Log.i(TAG, serverResponseCode+"");
            		Log.i(TAG, serverResponseMessage);

            		fileInputStream.close();
            		outputStream.flush();
            		outputStream.close();
            		
            	} catch (Exception e) {
                    Log.e(TAG, "Cannot establish connection", e);
                }
            }
		}
	}

	private List<? extends SendModel> getImages() {
		return new Select().from(ResponsePhoto.class).orderBy("Id ASC").execute();
	}

}
