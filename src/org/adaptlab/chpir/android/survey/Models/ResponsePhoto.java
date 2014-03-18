package org.adaptlab.chpir.android.survey.Models;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;

import org.adaptlab.chpir.android.activerecordcloudsync.SendModel;
import org.adaptlab.chpir.android.survey.AppUtil;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "ResponsePhotos")
public class ResponsePhoto extends SendModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String TAG = "ResponsePhoto";
	@Column(name = "SentToRemote")
	private boolean mSent;
	@Column(name = "Response", onDelete = Column.ForeignKeyAction.SET_NULL)
	private Response mResponse;
	@Column(name = "PicturePath")
	private String mPicturePath;
	@Column(name = "ResponseUUID")
	private String mResponseUUID;
		
	public ResponsePhoto() {
		super();
		mSent = false;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
        
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("response_uuid",  mResponseUUID);
            jsonObject.put("picture_data", getEncodedImage());  
            json.put("response_image", jsonObject);
        } catch (JSONException je) {
            Log.e(TAG, "JSON exception", je);
        }
        return json;
	}
	
	public String getEncodedImage() {
		String encodedImage = "";
		if (getPicturePath() != null) {
			String filepath = AppUtil.getContext().getFileStreamPath(getPicturePath()).getAbsolutePath();
			Bitmap bitmap = BitmapFactory.decodeFile(filepath);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
			byte[] pictureBytes = outputStream.toByteArray();
			encodedImage = Base64.encodeToString(pictureBytes, Base64.DEFAULT);
		}
		return encodedImage;
	}

	@Override
	public boolean isSent() {
		return mSent;
	}

	@Override
	public boolean readyToSend() {
		if (getResponse() == null) {
			if (getEncodedImage() == null) {
				return false;
			} else  {
				return true;
			}
		} else {
			return getResponse().getSurvey().readyToSend(); 
		}
	}
	
	public void setResponse(Response response) {
		mResponse = response;
	}
	
	public void setResponseUUID(String uuid) {
		mResponseUUID = uuid;
	}
	
	public Response getResponse() {
		return mResponse;
	}
	
	public void setPicturePath(String path) {
		mPicturePath = path;
	}
	
	public String getPicturePath() {
		return mPicturePath;
	}

	@Override
	public void setAsSent() {  
		mSent = true;
        this.delete();
        Log.d(TAG, getAll().size() + " response photos left on device");
	}
	
	public static List<ResponsePhoto> getAll() {
        return new Select().from(ResponsePhoto.class).orderBy("Id ASC").execute();
    }

}
